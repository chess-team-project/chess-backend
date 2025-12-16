package com.chess.service;

import com.chess.model.GameState;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с шахматными играми через chesslib
 */
@Service
public class ChessGameService {

    /**
     * Создает новую игру со стартовой позицией
     * @param gameId ID игры
     * @return Board с начальной позицией
     */
    public Board createGame(String gameId) {
        Board board = new Board();
        return board;
    }

    /**
     * Выполняет ход и обновляет состояние игры
     * @param board текущая доска
     * @param fromSquare начальная позиция (например "e2")
     * @param toSquare конечная позиция (например "e4")
     * @return GameState с обновленным FEN
     */
    public GameState makeMove(Board board, String fromSquare, String toSquare) {
        // Конвертируем строки в Square
        Square from = Square.valueOf(fromSquare.toUpperCase());
        Square to = Square.valueOf(toSquare.toUpperCase());
        
        Move move = new Move(from, to);
        
        if (!board.isMoveLegal(move, true)) {
            throw new IllegalArgumentException("Недопустимый ход: " + fromSquare + "-" + toSquare);
        }
        
        board.doMove(move);
        
        // Создаем GameState с новым FEN
        GameState updatedState = new GameState();
        updatedState.setFen(board.getFen());
        
        return updatedState;
    }

    /**
     * Получает список легальных ходов для текущего игрока в формате "e2e4"
     * @param fen текущая позиция в формате FEN
     * @return список легальных ходов
     */
    public List<String> getLegalMoves(String fen) {
        try {
            Board board = new Board();
            board.loadFromFen(fen);
            
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
