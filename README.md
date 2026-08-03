# Task Management System

REST API для постановки, ведения и приёмки задач. Учебный проект, выросший из пятимодульного интенсива по Spring Boot: от in-memory хранилища до приложения с реальной БД, миграциями, JWT-аутентификацией и веб-интерфейсом.

## Стек

| Слой | Технология |
|---|---|
| Язык | Java 17+ |
| Каркас | Spring Boot 4.1 |
| Web | Spring Web MVC |
| Доступ к данным | Spring Data JPA, Hibernate 7 |
| БД | PostgreSQL 18 |
| Миграции | Flyway |
| Безопасность | Spring Security, JWT (jjwt) |
| Валидация | Jakarta Bean Validation |
| Сборка | Maven |
| Инфраструктура | Docker, Docker Compose |

## Запуск

Нужен только Docker — Java и PostgreSQL локально не требуются.

```bash
docker-compose up -d
```

Команда собирает приложение, поднимает базу и запускает API. Первая сборка занимает несколько минут, последующие — секунды за счёт кэша слоёв.

Приложение доступно на **http://localhost:8080** — откроется страница входа.

Остановить, сохранив данные:

```bash
docker-compose down
```

Остановить и стереть базу:

```bash
docker-compose down -v
```

### Локальный запуск без контейнера приложения

Если удобнее запускать приложение из IDE, поднимите только базу:

```bash
docker-compose up -d postgres
```
и стартуйте `TaskSystemApplication`. Приложение подключится к `localhost:5433`.

## API

### Аутентификация

| Метод | Путь | Описание |
|---|---|---|
| `POST` | `/auth/register` | Регистрация, возвращает токен |
| `POST` | `/auth/login` | Вход, возвращает токен |

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"fedoseev","password":"secret"}'
```

### Задачи

Все методы требуют заголовок `Authorization: Bearer <token>`.

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/tasks` | Все задачи |
| `GET` | `/tasks/{id}` | Задача по идентификатору |
| `GET` | `/tasks/search` | Поиск с фильтрами и пагинацией |
| `POST` | `/tasks` | Создать задачу |
| `PUT` | `/tasks/{id}` | Изменить исполнителя, срок, приоритет |
| `POST` | `/tasks/{id}/start` | Перевести в работу |
| `POST` | `/tasks/{id}/complete` | Закрыть |
| `DELETE` | `/tasks/{id}` | Удалить |

Параметры поиска: `creatorId`, `assignedUserId`, `status`, `priority`, `pageNum`, `pageSize`.

```bash
curl "http://localhost:8080/tasks/search?status=IN_PROGRESS&priority=HIGH&pageSize=20" \
  -H "Authorization: Bearer $TOKEN"
```

Создание задачи:

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "creatorId": 1,
    "assignedUserId": 2,
    "createDateTime": "2026-08-03T10:00:00",
    "deadlineDate": "2026-08-20T18:00:00",
    "priority": "HIGH"
  }'
```

## Модель данных

**task**

| Колонка | Тип | Описание |
|---|---|---|
| `id` | bigserial | Первичный ключ |
| `creator_id` | bigint | Постановщик |
| `assigned_user_id` | bigint | Исполнитель |
| `status` | varchar | `CREATED`, `IN_PROGRESS`, `DONE` |
| `create_date_time` | timestamp | Дата постановки |
| `deadline_date` | timestamp | Срок сдачи |
| `done_date_time` | timestamp | Фактическое закрытие |
| `priority` | varchar | `LOW`, `MEDIUM`, `HIGH` |

**users**

| Колонка | Тип | Описание |
|---|---|---|
| `id` | bigserial | Первичный ключ |
| `username` | varchar | Логин, уникальный |
| `password_hash` | varchar | BCrypt-хэш |
| `role` | varchar | `USER`, `ADMIN` |

Схема создаётся Flyway-миграциями из `src/main/resources/db/migration`. Hibernate работает в режиме `validate` и только сверяет сущности с реальными таблицами, ничего не изменяя.

