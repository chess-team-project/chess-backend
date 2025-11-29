# Chess Backend API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Все эндпоинты (кроме `/api/health`) требуют заголовок авторизации:
```
Authorization: sfhasdjf
```

---

## Endpoints

### 1. Health Check

#### `GET /api/health`
Проверка работоспособности сервиса.

**Headers:**
- Не требуется авторизация

**Response:**
```json
{
  "status": "UP"
}
```

**Status Codes:**
- `200 OK` - Сервис работает

---

### 2. Create Game (Legacy)

#### `POST /api/game`
Создать новую игру с двумя игроками.

**Headers:**
- `Authorization: sfhasdjf`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "whitePlayerId": "player1",
  "blackPlayerId": "player2"
}
```

**Response:**
```json
{
  "message": "Game created",
  "gameState": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "turn": "WHITE",
    "status": "IN_PROGRESS",
    "whitePlayerId": "player1",
    "blackPlayerId": "player2"
  }
}
```

**Status Codes:**
- `200 OK` - Игра создана успешно
- `400 Bad Request` - Ошибка валидации или создания игры

---

### 3. Create Game for Player

#### `POST /api/game/create/{id}`
Создать новую игру для игрока с указанным ID. Игрок играет белыми.

**Headers:**
- `Authorization: sfhasdjf`

**Path Parameters:**
- `id` (String) - ID игрока

**Request Example:**
```
POST /api/game/create/player123
```

**Response:**
```json
{
  "gameId": "550e8400-e29b-41d4-a716-446655440000",
  "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
  "legalMoves": [
    "a2a3", "a2a4", "b2b3", "b2b4", "c2c3", "c2c4",
    "d2d3", "d2d4", "e2e3", "e2e4", "f2f3", "f2f4",
    "g2g3", "g2g4", "h2h3", "h2h4", "b1a3", "b1c3",
    "g1f3", "g1h3"
  ]
}
```

**Response Fields:**
- `gameId` (String) - Уникальный ID созданной игры
- `fen` (String) - Текущая позиция в формате FEN
- `legalMoves` (Array<String>) - Массив легальных ходов для текущего игрока в формате "e2e4" (from-to)

**Status Codes:**
- `200 OK` - Игра создана успешно
- `400 Bad Request` - Ошибка создания игры

---

### 4. Get Game State

#### `GET /api/game/state/{id}`
Получить текущее состояние игры по ID.

**Headers:**
- `Authorization: sfhasdjf`

**Path Parameters:**
- `id` (String) - ID игры

**Request Example:**
```
GET /api/game/state/550e8400-e29b-41d4-a716-446655440000
Headers:
  Authorization: sfhasdjf
```

**Response:**
```json
{
  "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
  "legalMoves": [
    "a2a3", "a2a4", "b2b3", "b2b4", "c2c3", "c2c4",
    "d2d3", "d2d4", "e2e3", "e2e4", "f2f3", "f2f4",
    "g2g3", "g2g4", "h2h3", "h2h4", "b1a3", "b1c3",
    "g1f3", "g1h3"
  ]
}
```

**Response Fields:**
- `fen` (String) - Текущая позиция в формате FEN
- `legalMoves` (Array<String>) - Массив легальных ходов для текущего игрока в формате "e2e4" (from-to)

**Status Codes:**
- `200 OK` - Состояние игры получено успешно
- `400 Bad Request` - Игра не найдена или произошла ошибка

---

### 5. Make Move

#### `POST /api/game/move/{id}`
Выполнить ход в игре.

**Headers:**
- `Authorization: sfhasdjf`
- `Content-Type: application/json`

**Path Parameters:**
- `id` (String) - ID игры

**Request Body:**
```json
{
  "move": "e2e4"
}
```

**Request Example:**
```
POST /api/game/move/550e8400-e29b-41d4-a716-446655440000
Headers:
  Authorization: sfhasdjf
  Content-Type: application/json

Body:
{
  "move": "e2e4"
}
```

**Response (Success):**
```json
{
  "fen": "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
  "legalMoves": [
    "a7a6", "a7a5", "b7b6", "b7b5", "c7c6", "c7c5",
    "d7d6", "d7d5", "e7e6", "e7e5", "f7f6", "f7f5",
    "g7g6", "g7g5", "h7h6", "h7h5", "b8a6", "b8c6",
    "g8f6", "g8h6"
  ]
}
```

**Response (Error):**
```json
{
  "error": "Недопустимый ход: e2e5"
}
```

**Response Fields:**
- `fen` (String) - Новая позиция после хода в формате FEN
- `legalMoves` (Array<String>) - Массив легальных ходов для следующего игрока (после применения хода)
- `error` (String) - Сообщение об ошибке, если ход невалидный

**Status Codes:**
- `200 OK` - Ход выполнен успешно
- `400 Bad Request` - Игра не найдена, ход невалидный или произошла ошибка

**Move Format:**
- Ход должен быть в формате "e2e4" (4 символа: from-to)
- Первые 2 символа - начальная позиция (например, "e2")
- Последние 2 символа - конечная позиция (например, "e4")

---

### 6. Offer Draw

#### `POST /api/game/{gameId}/draw/offer`
Предложить ничью в текущей игре.

**Headers:**
- `Authorization: sfhasdjf`
- `Auth: {playerId}` - ID игрока, который предлагает ничью

**Path Parameters:**
- `gameId` (String) - ID игры

**Request Example:**
```
POST /api/game/550e8400-e29b-41d4-a716-446655440000/draw/offer
Headers:
  Authorization: sfhasdjf
  Auth: player1
```

**Response:**
```json
{
  "message": "Draw offer made",
  "gameState": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "turn": "WHITE",
    "status": "IN_PROGRESS",
    "drawOfferFrom": "WHITE"
  }
}
```

**Status Codes:**
- `200 OK` - Предложение ничьей создано
- `400 Bad Request` - Игрок не является участником игры или игра не в процессе

---

### 7. Accept Draw

#### `POST /api/game/{gameId}/draw/accept`
Принять предложение ничьей.

**Headers:**
- `Authorization: sfhasdjf`
- `Auth: {playerId}` - ID игрока, который принимает ничью

**Path Parameters:**
- `gameId` (String) - ID игры

**Request Example:**
```
POST /api/game/550e8400-e29b-41d4-a716-446655440000/draw/accept
Headers:
  Authorization: sfhasdjf
  Auth: player2
```

**Response:**
```json
{
  "message": "Draw accepted",
  "gameState": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "turn": "WHITE",
    "status": "DRAW",
    "drawOfferFrom": null
  }
}
```

**Status Codes:**
- `200 OK` - Ничья принята, игра завершена
- `400 Bad Request` - Нет предложения ничьей, игрок не является участником или игра не в процессе

---

## Data Models

### GameState
```json
{
  "id": "string",
  "fen": "string",
  "turn": "WHITE | BLACK",
  "status": "IN_PROGRESS | CHECKMATE | STALEMATE | DRAW",
  "createdAt": "ISO 8601 timestamp",
  "lastMoveAt": "ISO 8601 timestamp",
  "whitePlayerId": "string",
  "blackPlayerId": "string",
  "drawOfferFrom": "WHITE | BLACK | null"
}
```

### GameStatus
- `IN_PROGRESS` - Игра в процессе
- `CHECKMATE` - Мат
- `STALEMATE` - Пат
- `DRAW` - Ничья

### Side
- `WHITE` - Белые
- `BLACK` - Черные

---

## Error Responses

Все ошибки возвращаются в следующем формате:

```json
{
  "error": "Error message description"
}
```

**Common Status Codes:**
- `400 Bad Request` - Неверный запрос или валидация не прошла
- `401 Unauthorized` - Отсутствует или неверный заголовок Authorization
- `404 Not Found` - Ресурс не найден

---

## Notes

1. **FEN Format**: Позиция доски передается в стандартном формате FEN (Forsyth-Edwards Notation)
2. **Move Format**: Ходы передаются в формате "e2e4" (квадрат откуда + квадрат куда)
3. **Legal Moves**: Список легальных ходов генерируется для текущего игрока (чей ход)
4. **Player ID**: ID игрока передается в заголовке `Auth` для эндпоинтов, требующих идентификации игрока

---

## Examples

### Создание игры и получение легальных ходов
```bash
curl -X POST http://localhost:8080/api/game/create/player123 \
  -H "Authorization: sfhasdjf"
```

### Получение состояния игры
```bash
curl -X GET http://localhost:8080/api/game/state/{gameId} \
  -H "Authorization: sfhasdjf"
```

### Выполнение хода
```bash
curl -X POST http://localhost:8080/api/game/move/{gameId} \
  -H "Authorization: sfhasdjf" \
  -H "Content-Type: application/json" \
  -d '{"move": "e2e4"}'
```

### Предложение ничьей
```bash
curl -X POST http://localhost:8080/api/game/{gameId}/draw/offer \
  -H "Authorization: sfhasdjf" \
  -H "Auth: player1"
```

### Принятие ничьей
```bash
curl -X POST http://localhost:8080/api/game/{gameId}/draw/accept \
  -H "Authorization: sfhasdjf" \
  -H "Auth: player2"
```

