package com.chess.service;

import com.chess.model.GameState;
import com.chess.model.GameStatus;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с шахматными играми через chesslib
 */
@Service
public class ChessGameService {

    /**
     * Создает новую игру со стартовой позицией
     */
    public GameState createNewGame(String gameId) {
        Board board = new Board();

        // gameRepository.save(gameId, board)
        
        return gameState;
    }

    /**
     * Выполняет ход и обновляет состояние игры
     */
    public GameState makeMove(String gameId, String fromSquare, String toSquare) {
        Board board = gameRepository.getById(gameid)
        
        Square from = Square.valueOf(fromSquare.toUpperCase());
        Square to = Square.valueOf(toSquare.toUpperCase());
        
        Move move = new Move(from, to);
        
        if (!board.isMoveLegal(move, true)) {
            throw new IllegalArgumentException("Недопустимый ход: " + fromSquare + "-" + toSquare);
        }
        
        board.doMove(move);
        
        
        return updatedState;
    }


    /**
     * Получает список легальных ходов для текущего игрока в формате "e2e4"
     */
    public List<String> getLegalMoves(String gameId) {
        try {
            Board board = gameRepository.getById(gameid)

            
            List<Move> legalMoves = MoveGenerator.generateLegalMoves(board);
            
            return legalMoves.stream()
                    .map(move -> move.getFrom().toString().toLowerCase() + 
                                move.getTo().toString().toLowerCase())
                    .collect(Collectors.toList());
        } catch (MoveGeneratorException e) {
            throw new RuntimeException("Error generating legal moves: " + e.getMessage(), e);
        }
    }
}

