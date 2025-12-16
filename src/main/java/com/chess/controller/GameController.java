package com.chess.controller;

import com.chess.dto.MoveRequest;
import com.chess.model.GameState;
import com.chess.repository.GameRepository;
import com.chess.service.ChessGameService;
import com.github.bhlangonijr.chesslib.Board;
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

    public GameController(GameRepository gameRepository, ChessGameService chessGameService) {
        this.gameRepository = gameRepository;
        this.chessGameService = chessGameService;
    }

    /**
     * POST /api/game/create/{id}
     * Создать новую игру с указанным ID
     * Возвращает: {gameId, fen, legal moves for current player}
     */
    @PostMapping("/create/{id}")
    public ResponseEntity<?> createGame(@PathVariable("id") String gameId) {
        try {
            Board newGame = chessGameService.createGame(gameId);
            gameRepository.save(gameId, newGame);
            
            List<String> legalMoves = chessGameService.getLegalMoves(newGame.getFen());
            
            return ResponseEntity.ok(Map.of(
                    "gameId", gameId,
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
            Board gameBoard = gameRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
            
            // Получаем легальные ходы для текущего игрока
            List<String> legalMoves = chessGameService.getLegalMoves(gameBoard.getFen());
            
            return ResponseEntity.ok(Map.of(
                    "fen", gameBoard.getFen(),
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
            Board gameBoard = gameRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
            
            // Используем весь строковый ход (включая возможную промоцію)
            String moveStr = moveRequest.getMove();
            
            GameState updatedState = chessGameService.makeMove(gameBoard, moveStr);
            
            // Обновляем доску с новым FEN
            Board updatedBoard = new Board();
            updatedBoard.loadFromFen(updatedState.getFen());
            gameRepository.update(id, updatedBoard);
            
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

    /**
     * DELETE /api/game/{id}
     * Удалить игру по ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable String id) {
        try {
            if (!gameRepository.existsById(id)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Game not found: " + id));
            }
            
            gameRepository.deleteById(id);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Game deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error deleting game: " + e.getMessage()));
        }
    }
}
