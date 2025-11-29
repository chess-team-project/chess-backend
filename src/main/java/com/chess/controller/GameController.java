package com.chess.controller;

import com.chess.dto.CreateGameRequest;
import com.chess.dto.DrawAcceptRequest;
import com.chess.dto.DrawOfferRequest;
import com.chess.dto.MoveRequest;
import com.chess.model.GameState;
import com.chess.repository.GameRepository;
import com.chess.service.ChessGameService;
import com.chess.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с играми
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameRepository gameRepository;
    private final ChessGameService chessGameService;
    private final AuthUtil authUtil;

    public GameController(GameRepository gameRepository, ChessGameService chessGameService, AuthUtil authUtil) {
        this.gameRepository = gameRepository;
        this.chessGameService = chessGameService;
        this.authUtil = authUtil;
    }

    /**
     * POST /api/game/create/:id
     * Создать новую игру с указанным ID
     * Возвращает: {gameId, fen, legal moves for current player}
     */
    @PostMapping("/create/{id}")
    public ResponseEntity<?> createGame(@PathVariable String gameId) {
        try {
            Board newGame = chessGameService.createGame(gameId);
            gameRepository.save(gameId, newGame);
            
            List<String> legalMoves = chessGameService.getLegalMoves(newGame.getFen());
            
            return ResponseEntity.ok(Map.of(
                    "gameId", newGame.getId(),
                    "fen", newGame.getFen(),
                    "legalMoves", legalMoves
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/game/state/{id}
     * Получить состояние игры по ID
     * Возвращает: {fen, legal actions for current player}
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<?> getGameState(@PathVariable String id) {
        try {
            Board gameState = gameRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
            
            // Получаем легальные ходы для текущего игрока
            List<String> legalMoves = chessGameService.getLegalMoves(gameState.getFen());
            
            return ResponseEntity.ok(Map.of(
                    "fen", gameState.getFen(),
                    "legalMoves", legalMoves
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error getting game state: " + e.getMessage()));
        }
    }

    /**
     * POST /api/game/move/{id}
     * Выполнить ход в игре
     * Принимает: {id, move}
     * Возвращает: {new fen, legal actions for next player(after move applied)} | {error}
     */
    @PostMapping("/move/{id}")
    public ResponseEntity<?> makeMove(
            @PathVariable String id,
            @Valid @RequestBody MoveRequest moveRequest) {
        
        try {
            Board gameState = gameRepository.findById(id) // <-- Board type
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
            
            // Парсим ход из формата "e2e4"
            String fromSquare = moveRequest.getFromSquare();
            String toSquare = moveRequest.getToSquare();
            
            // Выполняем ход
            GameState updatedState = chessGameService.makeMove(gameState, fromSquare, toSquare);
            gameRepository.update(id, updatedState);
            
            // Получаем легальные ходы для следующего игрока (после хода)
            List<String> legalMoves = chessGameService.getLegalMoves(updatedState.getFen());
            
            return ResponseEntity.ok(Map.of(
                    "fen", updatedState.getFen(),
                    "legalMoves", legalMoves
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error making move: " + e.getMessage()));
        }
    }

   
}

