# Task Management System — сервис управления задачами

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)

## Что это

Сервис для постановки и ведения задач: создать задачу, назначить исполнителя, взять в работу, закрыть.
REST API и простой веб-интерфейс (вход и панель задач) работают в одном приложении.

## Зачем

Учебный проект: показывает базовый backend на Spring Boot с авторизацией, правилами предметной области
(жизненный цикл задачи), миграциями БД и тестами.

## Что умеет

- регистрация и вход по JWT, роли `USER` и `ADMIN`;
- создание, изменение и удаление задач;
- жизненный цикл `CREATED → IN_PROGRESS → DONE`, приоритеты `LOW / MEDIUM / HIGH`;
- поиск по создателю, исполнителю, статусу и приоритету, с постраничной выдачей;
- правила: нельзя взять в работу задачу без исполнителя, нельзя менять завершённую задачу,
  у одного исполнителя не больше 5 задач в работе;
- единый формат ошибок через глобальный обработчик исключений;
- веб-интерфейс из статических страниц, которые раздаёт само приложение.

## Эндпоинты

| Метод | Путь | Что делает |
|---|---|---|
| POST | `/auth/register`, `/auth/login` | регистрация и вход, возвращает JWT |
| GET | `/tasks`, `/tasks/{id}` | список задач и одна задача |
| GET | `/tasks/search` | поиск по фильтрам |
| POST | `/tasks` | создать задачу |
| PUT | `/tasks/{id}` | изменить исполнителя, приоритет, срок |
| POST | `/tasks/{id}/start` | взять в работу |
| POST | `/tasks/{id}/complete` | завершить |
| DELETE | `/tasks/{id}` | удалить |

Все `/tasks/**` требуют заголовок `Authorization: Bearer <токен>`.

## Стек

Java 17, Spring Boot 4, Spring Security + JWT (jjwt), Spring Data JPA, PostgreSQL, Flyway, Lombok, Docker.

## Как запустить

Нужен только Docker:

```bash
docker-compose up -d
```

Откройте http://localhost:8080, там страница входа. Остановить: `docker-compose down`.

Без Docker для приложения: поднимите PostgreSQL на порту 5433 (параметры в `application.properties`)
и выполните `./mvnw spring-boot:run`.

## Тесты

```bash
./mvnw test
```

## Структура

```
src/main/java/fedoseev/tasks/system/
  task/       задачи: контроллер, сервис, репозиторий, исключения
  security/   JWT, пользователи, настройка безопасности
src/main/resources/db/migration/   Flyway-миграции
src/main/resources/static/         веб-интерфейс
```

## Авторство

Бэкенд написан мной, веб-интерфейс сделан с помощью ИИ-ассистента.
