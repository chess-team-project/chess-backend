#!/bin/bash

# Скрипт для установки git hooks

echo "Installing git hooks..."

# Переходим в корень проекта
cd "$(dirname "$0")/.."

# Копируем pre-commit hook
if [ -f "scripts/pre-commit" ]; then
    cp scripts/pre-commit .git/hooks/pre-commit
    chmod +x .git/hooks/pre-commit
    echo "✅ Pre-commit hook installed successfully!"
else
    echo "❌ Error: scripts/pre-commit not found"
    exit 1
fi

echo "All hooks installed!"

