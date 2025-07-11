# User Service

## Сервис для работы с пользователями

Предоставляет регистрацию, логин и отправку в телеграм сообщений

## Стек

- Java 17
- PostgreSQL 16
- Spring Boot 3.3.3
- Keycloak 26.0.0

## Как запустить docker-compose?

1) Добавить `.env` файл

```
TELEGRAM_TOKEN=Ваш токен телеграм бота
```

2) Запустить `docker-compose-local.yml`

## Как запустить для разработки?

1) Добавить `.env` файл

```
TELEGRAM_TOKEN=Ваш токен телеграм бота
```

2) Установить библиотеку common-models
3) Запустить docker-compose.yml
4) Запустить `TELEGRAM_TOKEN=ваш токен телеграм бота mvn clean install -Dspring.profiles.active=test` для тестов
5) Запустить Spring приложение с профиле `local`

[Postman](https://www.postman.com/team88-6762/workspace/berkut-service/collection/23909140-2e5310c3-d575-46f4-9875-46b59172f2f6?action=share&creator=23909140)

