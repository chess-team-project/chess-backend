package com.chess.service;

import com.chess.model.GameState;
import com.github.bhlangonijr.chesslib.Board;
import java.util.Locale;
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
    /**
     * Выполняет ход и обновляет состояние игры. Принимает ход в формате "e2e4" или "e7e8q" (промоция).
     * @param board текущая доска
     * @param moveStr ход в виде строки
     * @return GameState с обновленным FEN
     */
    public GameState makeMove(Board board, String moveStr) {
        try {
            List<Move> legalMoves = MoveGenerator.generateLegalMoves(board);

            String normalized = moveStr.toLowerCase(Locale.ROOT);

            Move selected = legalMoves.stream()
                    .filter(m -> {
                        String s = m.getFrom().toString().toLowerCase() + m.getTo().toString().toLowerCase();
                        if (m.getPromotion() != null) {
                            String promo = m.getPromotion().toString().toLowerCase();
                            if (promo.contains("queen")) s += "q";
                            else if (promo.contains("rook")) s += "r";
                            else if (promo.contains("bishop")) s += "b";
                            else if (promo.contains("knight")) s += "n";
                        }
                        return s.equals(normalized);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Недопустимый ход: " + moveStr));

            board.doMove(selected);

            GameState updatedState = new GameState();
            updatedState.setFen(board.getFen());
            return updatedState;
        } catch (MoveGeneratorException e) {
            throw new RuntimeException("Error generating legal moves: " + e.getMessage(), e);
        }
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
                    .map(move -> {
                        String s = move.getFrom().toString().toLowerCase() +
                                move.getTo().toString().toLowerCase();
                        if (move.getPromotion() != null) {
                            String promo = move.getPromotion().toString().toLowerCase();
                            if (promo.contains("queen")) s += "q";
                            else if (promo.contains("rook")) s += "r";
                            else if (promo.contains("bishop")) s += "b";
                            else if (promo.contains("knight")) s += "n";
                        }
                        return s;
                    })
                    .collect(Collectors.toList());
        } catch (MoveGeneratorException e) {
            throw new RuntimeException("Error generating legal moves: " + e.getMessage(), e);
        }
    }
}
