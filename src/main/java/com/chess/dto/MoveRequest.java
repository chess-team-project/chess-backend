package com.chess.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO для запроса хода
 */
public class MoveRequest {

    @NotBlank(message = "Move is required")
    @Pattern(regexp = "^[a-h][1-8][a-h][1-8]([nbrqNBRQ])?$",
             message = "Move must be like 'e2e4' or 'e7e8q' (optional promotion)")
    private String move;

    public MoveRequest() {
    }

    public MoveRequest(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    /**
     * Извлекает начальную позицию из хода (первые 2 символа)
     */
    public String getFromSquare() {
        if (move == null || move.length() < 2) {
            throw new IllegalArgumentException("Invalid move format");
        }
        return move.substring(0, 2);
    }

    /**
     * Извлекает конечную позицию из хода (последние 2 символа)
     */
    public String getToSquare() {
        if (move == null || move.length() < 4) {
            throw new IllegalArgumentException("Invalid move format");
        }
        return move.substring(2, 4);
    }

    /**
     * Якщо є символ промоції (5-й символ), повертає його у нижньому регістрі, інакше null
     */
    public Character getPromotionChar() {
        if (move != null && move.length() == 5) {
            return Character.toLowerCase(move.charAt(4));
        }
        return null;
    }
}

