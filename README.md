
# 💳 Bank API

Backend-приложение на **Java + Spring Boot** для управления банковскими картами.  
Поддерживает аутентификацию, авторизацию, переводы, управление картами и пользователями.

---

## 🚀 Возможности

### 👤 Пользователь
- Просмотр своих карт (поиск + пагинация)
- Запрос блокировки карты
- Переводы между своими картами
- Просмотр баланса

### 🛠 Администратор
- Создание, блокировка, активация и удаление карт
- Управление пользователями
- Просмотр всех карт

---

## 🛠 Технологии
- **Java 17+**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MariaDB**
- **Liquibase** — миграции
- **Docker Compose**
- **JUnit** — тесты

---

## 📦 Запуск проекта

### 1. Клонирование репозитория
```bash
git clone https://github.com/arthur2050/test_bank_api.git
cd test_bank_api
```

### 2. Сборка проекта с Maven
Требуется JDK 17+ и Maven.

```bash
mvn clean package -DskipTests
```

После сборки появится JAR-файл:

```bash
target/test_bank_api-0.0.1-SNAPSHOT.jar
```

### 3. Запуск через Docker Compose

```bash
docker compose up --build
```
или (для старых версий Docker):

```bash
docker-compose up --build
```

🌐 Доступ к приложению  
После запуска:

```bash
http://localhost:8084
```

### 📜 API-документация (Swagger)
Swagger ещё не настроен, но планируется по адресу:

```bash
http://localhost:8084/swagger-ui.html
```
или

```bash
http://localhost:8084/swagger-ui/index.html
```

### 🛑 Остановка контейнеров

```bash
docker compose down
```
или

```bash
docker-compose down
```

### ▶ Запуск без Docker

```bash
java -jar target/test_bank_api-0.0.1-SNAPSHOT.jar
```

При этом базу нужно поднять и настроить отдельно.

---

## 📂 Структура проекта

```bash
src/main/java/com/bank/api/      # Исходный код приложения
src/main/resources/              # Конфиги, миграции Liquibase
docker/                          # Dockerfile
docker-compose.yml               # Конфигурация окружения
```

---

## ⚙️ Примеры API-запросов

### Аутентификация
Получение JWT токена:

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "password123"
}
```

Ответ:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```

### Просмотр карт пользователя (требуется JWT в Authorization header)

```bash
GET /api/cards?status=ACTIVE&page=0&size=10
Authorization: Bearer {token}
```

### Создание новой карты (админ)

```bash
POST /api/cards
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "expirationDate": "2026-12-31",
  "balance": 1000.00
}
```

### Перевод между своими картами

```bash
POST /api/transfers
Authorization: Bearer {token}
Content-Type: application/json

{
  "fromCardId": 1,
  "toCardId": 2,
  "amount": 150.50
}
```

### Запрос блокировки карты

```bash
POST /api/cards/{cardId}/block
Authorization: Bearer {token}
```

---

## 🧪 Тестирование

В проекте есть юнит-тесты ключевой бизнес-логики.

Запуск тестов:

```bash
mvn test
```

---

## 👨‍💻 Автор

Arthur2050