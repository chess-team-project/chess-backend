package com.chess.controller;

import com.chess.dto.CreateGameRequest;
import com.chess.dto.DrawAcceptRequest;
import com.chess.dto.DrawOfferRequest;
import com.chess.model.GameState;
import com.chess.repository.GameRepository;
import com.chess.service.ChessGameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для работы с играми
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameRepository gameRepository;
    private final ChessGameService chessGameService;

    public GameController(GameRepository gameRepository, ChessGameService chessGameService) {
        this.gameRepository = gameRepository;
        this.chessGameService = chessGameService;
    }

    /**
     * POST /api/game
     * Создать новую игру
     */
    @PostMapping
    public ResponseEntity<?> createGame(@Valid @RequestBody CreateGameRequest request) {
        try {
            GameState newGame = chessGameService.createNewGame(
                    request.getWhitePlayerId(),
                    request.getBlackPlayerId()
            );
            gameRepository.save(newGame);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Game created",
                    "gameState", newGame
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/game/{gameId}/draw/offer
     * Предложить ничью
     */
    @PostMapping("/{gameId}/draw/offer")
    public ResponseEntity<?> offerDraw(
            @PathVariable String gameId,
            @Valid @RequestBody DrawOfferRequest request) {
        
        GameState gameState = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        try {
            GameState updatedState = chessGameService.offerDraw(gameState, request.getPlayerId());
            gameRepository.update(gameId, updatedState);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Draw offer made",
                    "gameState", updatedState
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/game/{gameId}/draw/accept
     * Принять предложение ничьей
     */
    @PostMapping("/{gameId}/draw/accept")
    public ResponseEntity<?> acceptDraw(
            @PathVariable String gameId,
            @Valid @RequestBody DrawAcceptRequest request) {
        
        GameState gameState = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        try {
            GameState updatedState = chessGameService.acceptDraw(gameState, request.getPlayerId());
            gameRepository.update(gameId, updatedState);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Draw accepted",
                    "gameState", updatedState
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

