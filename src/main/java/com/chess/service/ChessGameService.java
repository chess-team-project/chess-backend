package com.chess.service;

import com.chess.model.GameState;
import com.chess.model.GameStatus;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для работы с шахматными играми через chesslib
 */
@Service
public class ChessGameService {

    /**
     * Создает новую игру со стартовой позицией
     */
    public GameState createNewGame(String whitePlayerId, String blackPlayerId) {
        Board board = new Board();
        
        GameState gameState = new GameState();
        gameState.setId(UUID.randomUUID().toString());
        gameState.setFen(board.getFen());
        gameState.setTurn(board.getSideToMove());
        gameState.setStatus(GameStatus.IN_PROGRESS);
        gameState.setWhitePlayerId(whitePlayerId);
        gameState.setBlackPlayerId(blackPlayerId);
        
        return gameState;
    }

    /**
     * Создает игру из FEN строки
     */
    public GameState createGameFromFen(String fen, String whitePlayerId, String blackPlayerId) {
        Board board = new Board();
        board.loadFromFen(fen);
        
        GameState gameState = new GameState();
        gameState.setId(UUID.randomUUID().toString());
        gameState.setFen(board.getFen());
        gameState.setTurn(board.getSideToMove());
        gameState.setStatus(determineGameStatus(board));
        gameState.setWhitePlayerId(whitePlayerId);
        gameState.setBlackPlayerId(blackPlayerId);
        
        return gameState;
    }

    /**
     * Выполняет ход и обновляет состояние игры
     */
    public GameState makeMove(GameState currentState, String fromSquare, String toSquare) {
        Board board = new Board();
        board.loadFromFen(currentState.getFen());
        
        // Конвертируем строки в Square
        Square from = Square.valueOf(fromSquare.toUpperCase());
        Square to = Square.valueOf(toSquare.toUpperCase());
        
        Move move = new Move(from, to);
        
        if (!board.isMoveLegal(move, true)) {
            throw new IllegalArgumentException("Недопустимый ход: " + fromSquare + "-" + toSquare);
        }
        
        board.doMove(move);
        
        // Обновляем состояние
        GameState updatedState = new GameState();
        updatedState.setId(currentState.getId());
        updatedState.setFen(board.getFen());
        updatedState.setTurn(board.getSideToMove());
        updatedState.setStatus(determineGameStatus(board));
        updatedState.setCreatedAt(currentState.getCreatedAt());
        updatedState.setLastMoveAt(java.time.Instant.now());
        updatedState.setWhitePlayerId(currentState.getWhitePlayerId());
        updatedState.setBlackPlayerId(currentState.getBlackPlayerId());
        updatedState.setDrawOfferFrom(null); // Сбрасываем предложение ничьей после хода
        
        return updatedState;
    }

    /**
     * Определяет статус игры на основе состояния доски
     */
    private GameStatus determineGameStatus(Board board) {
        if (board.isMated()) {
            return GameStatus.CHECKMATE;
        } else if (board.isStaleMate()) {
            return GameStatus.STALEMATE;
        } else if (board.isDraw()) {
            return GameStatus.DRAW;
        } else {
            return GameStatus.IN_PROGRESS;
        }
    }

    /**
     * Обновляет состояние игры на основе текущего FEN
     * (полезно для синхронизации состояния)
     */
    public GameState updateGameState(GameState currentState) {
        Board board = new Board();
        board.loadFromFen(currentState.getFen());
        
        GameState updatedState = new GameState();
        updatedState.setId(currentState.getId());
        updatedState.setFen(board.getFen());
        updatedState.setTurn(board.getSideToMove());
        updatedState.setStatus(determineGameStatus(board));
        updatedState.setCreatedAt(currentState.getCreatedAt());
        updatedState.setLastMoveAt(currentState.getLastMoveAt());
        updatedState.setWhitePlayerId(currentState.getWhitePlayerId());
        updatedState.setBlackPlayerId(currentState.getBlackPlayerId());
        updatedState.setDrawOfferFrom(currentState.getDrawOfferFrom());
        
        return updatedState;
    }

    /**
     * Предлагает ничью от указанного игрока
     */
    public GameState offerDraw(GameState currentState, String playerId) {
        Side playerSide = getPlayerSide(currentState, playerId);
        if (playerSide == null) {
            throw new IllegalArgumentException("Player is not part of this game");
        }

        if (currentState.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        GameState updatedState = new GameState();
        updatedState.setId(currentState.getId());
        updatedState.setFen(currentState.getFen());
        updatedState.setTurn(currentState.getTurn());
        updatedState.setStatus(currentState.getStatus());
        updatedState.setCreatedAt(currentState.getCreatedAt());
        updatedState.setLastMoveAt(currentState.getLastMoveAt());
        updatedState.setWhitePlayerId(currentState.getWhitePlayerId());
        updatedState.setBlackPlayerId(currentState.getBlackPlayerId());
        updatedState.setDrawOfferFrom(playerSide);

        return updatedState;
    }

    /**
     * Принимает предложение ничьей
     */
    public GameState acceptDraw(GameState currentState, String playerId) {
        Side playerSide = getPlayerSide(currentState, playerId);
        if (playerSide == null) {
            throw new IllegalArgumentException("Player is not part of this game");
        }

        if (currentState.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }

        if (currentState.getDrawOfferFrom() == null) {
            throw new IllegalStateException("No draw offer exists");
        }

        if (currentState.getDrawOfferFrom() == playerSide) {
            throw new IllegalStateException("Cannot accept your own draw offer");
        }

        GameState updatedState = new GameState();
        updatedState.setId(currentState.getId());
        updatedState.setFen(currentState.getFen());
        updatedState.setTurn(currentState.getTurn());
        updatedState.setStatus(GameStatus.DRAW);
        updatedState.setCreatedAt(currentState.getCreatedAt());
        updatedState.setLastMoveAt(java.time.Instant.now());
        updatedState.setWhitePlayerId(currentState.getWhitePlayerId());
        updatedState.setBlackPlayerId(currentState.getBlackPlayerId());
        updatedState.setDrawOfferFrom(null);

        return updatedState;
    }

    /**
     * Определяет сторону игрока (WHITE или BLACK)
     */
    private Side getPlayerSide(GameState gameState, String playerId) {
        if (playerId.equals(gameState.getWhitePlayerId())) {
            return Side.WHITE;
        } else if (playerId.equals(gameState.getBlackPlayerId())) {
            return Side.BLACK;
        }
        return null;
    }
}

