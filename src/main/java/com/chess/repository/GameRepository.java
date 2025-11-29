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
    // {
    //     'H5T6': new Board
    // }

    private final Map<String, Board> games = new ConcurrentHashMap<>();

    /**
     * Сохраняет или обновляет игру
     */
    public GameState save(String gameId, Board board) {
        if (gameId == null) {
            throw new IllegalArgumentException("board must have a valid gameId");
        }
        games.put(gameId, board);
        return board;
    }

    /**
     * Находит игру по ID
     */
    public Optional<Board> findById(String gameId) {
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
}

