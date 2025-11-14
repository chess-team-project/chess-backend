package com.chess.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса на предложение ничьей
 */
public class DrawOfferRequest {
    
    @NotBlank(message = "Player ID is required")
    private String playerId;

    public DrawOfferRequest() {
    }

    public DrawOfferRequest(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}

