package com.chess.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса на создание новой игры
 */
public class CreateGameRequest {
    
    @NotBlank(message = "White player ID is required")
    private String whitePlayerId;
    
    @NotBlank(message = "Black player ID is required")
    private String blackPlayerId;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String whitePlayerId, String blackPlayerId) {
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
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
}

