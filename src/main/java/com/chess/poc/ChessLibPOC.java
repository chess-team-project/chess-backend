package com.chess.poc;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

/**
 * Proof of Concept класс для изучения API chesslib
 * Проверяем основные возможности библиотеки
 */
public class ChessLibPOC {

    public static void main(String[] args) {
        System.out.println("=== Chesslib POC ===\n");

        // Создаем новую доску (стартовая позиция)
        Board board = new Board();
        System.out.println("1. Создана новая доска");
        System.out.println("   FEN: " + board.getFen());
        System.out.println("   Чей ход: " + board.getSideToMove());
        System.out.println();

        // Пробуем сделать ход
        try {
            // Ход e2-e4 (белые) - используем константы Square
            Move move = new Move(Square.E2, Square.E4);
            System.out.println("2. Пробуем сделать ход: " + move);
            
            if (board.isMoveLegal(move, true)) {
                board.doMove(move);
                System.out.println("   Ход выполнен успешно!");
                System.out.println("   Новый FEN: " + board.getFen());
                System.out.println("   Чей ход теперь: " + board.getSideToMove());
            } else {
                System.out.println("   Ход невалидный!");
            }
            System.out.println();

            // Еще один ход (черные)
            Move move2 = new Move(Square.E7, Square.E5);
            System.out.println("3. Пробуем сделать ход: " + move2);
            
            if (board.isMoveLegal(move2, true)) {
                board.doMove(move2);
                System.out.println("   Ход выполнен успешно!");
                System.out.println("   Новый FEN: " + board.getFen());
                System.out.println("   Чей ход теперь: " + board.getSideToMove());
            } else {
                System.out.println("   Ход невалидный!");
            }
            System.out.println();

            // Проверяем статус игры
            System.out.println("4. Проверка статуса игры:");
            System.out.println("   Игра окончена: " + board.isDraw() + " (draw) / " + board.isMated() + " (mated)");
            System.out.println("   Шах: " + board.isKingAttacked());
            System.out.println("   Пат: " + board.isStaleMate());
            System.out.println();

            // Пробуем невалидный ход
            Move invalidMove = new Move(Square.A1, Square.A8);
            System.out.println("5. Пробуем невалидный ход: " + invalidMove);
            System.out.println("   Ход валидный: " + board.isMoveLegal(invalidMove, true));
            System.out.println();

        } catch (MoveGeneratorException e) {
            System.err.println("Ошибка при генерации хода: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== POC завершен ===");
    }
}

