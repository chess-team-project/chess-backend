package com.chess.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса на принятие ничьей
 */
public class DrawAcceptRequest {
    
    @NotBlank(message = "Player ID is required")
    private String playerId;

    public DrawAcceptRequest() {
    }

    public DrawAcceptRequest(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}

