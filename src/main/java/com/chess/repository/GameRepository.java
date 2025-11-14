package com.chess.repository;

import com.chess.model.GameState;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory хранилище активных игр
 * Использует ConcurrentHashMap для потокобезопасности
 */
@Repository
public class GameRepository {

    private final Map<String, GameState> games = new ConcurrentHashMap<>();

    /**
     * Сохраняет или обновляет игру
     */
    public GameState save(GameState gameState) {
        if (gameState.getId() == null || gameState.getId().isEmpty()) {
            throw new IllegalArgumentException("GameState must have a valid ID");
        }
        games.put(gameState.getId(), gameState);
        return gameState;
    }

    /**
     * Находит игру по ID
     */
    public Optional<GameState> findById(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    /**
     * Проверяет существование игры
     */
    public boolean existsById(String gameId) {
        return games.containsKey(gameId);
    }

    /**
     * Удаляет игру
     */
    public void deleteById(String gameId) {
        games.remove(gameId);
    }

    /**
     * Возвращает все активные игры
     */
    public Map<String, GameState> findAll() {
        return new ConcurrentHashMap<>(games);
    }

    /**
     * Возвращает количество активных игр
     */
    public long count() {
        return games.size();
    }

    /**
     * Обновляет состояние игры атомарно
     */
    public GameState update(String gameId, GameState updatedState) {
        if (!gameId.equals(updatedState.getId())) {
            throw new IllegalArgumentException("Game ID mismatch");
        }
        return games.compute(gameId, (key, existing) -> {
            if (existing == null) {
                throw new IllegalArgumentException("Game not found: " + gameId);
            }
            return updatedState;
        });
    }
}

