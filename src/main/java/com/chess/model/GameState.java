package com.chess.model;

import com.github.bhlangonijr.chesslib.Side;

import java.time.Instant;

/**
 * Модель состояния шахматной игры
 */
public class GameState {
    private String id;
    private String fen;
    private Side turn;
    private GameStatus status;
    private Instant createdAt;
    private Instant lastMoveAt;
    private String whitePlayerId;
    private String blackPlayerId;
    private Side drawOfferFrom; // Кто предложил ничью (null если никто)

    public GameState() {
        this.createdAt = Instant.now();
        this.lastMoveAt = Instant.now();
    }

    public GameState(String id, String fen, Side turn, GameStatus status) {
        this();
        this.id = id;
        this.fen = fen;
        this.turn = turn;
        this.status = status;
    }

    // Getters and Setters
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

    public Side getTurn() {
        return turn;
    }

    public void setTurn(Side turn) {
        this.turn = turn;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastMoveAt() {
        return lastMoveAt;
    }

    public void setLastMoveAt(Instant lastMoveAt) {
        this.lastMoveAt = lastMoveAt;
    }

    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(String whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(String blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    public Side getDrawOfferFrom() {
        return drawOfferFrom;
    }

    public void setDrawOfferFrom(Side drawOfferFrom) {
        this.drawOfferFrom = drawOfferFrom;
    }
}

