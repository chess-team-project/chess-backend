package com.chess.model;

/**
 * Статус шахматной игры
 */
public enum GameStatus {
    IN_PROGRESS,    // Игра идет
    CHECKMATE,      // Мат
    STALEMATE,      // Пат
    DRAW,           // Ничья
    RESIGNED        // Сдался
}

