package com.chess.repository;

import com.github.bhlangonijr.chesslib.Board;
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
    public Board save(String gameId, Board board) {
        if (gameId == null) {
            throw new IllegalArgumentException("gameId must not be null");
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
     * Обновляет игру
     */
    public Board update(String gameId, Board board) {
        if (!games.containsKey(gameId)) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        games.put(gameId, board);
        return board;
    }

    /**
     * Удаляет игру
     */
    public void deleteById(String gameId) {
        games.remove(gameId);
    }
}
