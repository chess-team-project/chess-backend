package com.chess.model;

/**
 * Упрощенная модель состояния шахматной игры
 * Хранит только ID игры и FEN позицию
 */
public class GameState {
    private String id;
    private String fen;

    public GameState() {
    }

    public GameState(String id, String fen) {
        this.id = id;
        this.fen = fen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
}

