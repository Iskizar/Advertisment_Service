-- Создание таблицы пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Создание таблицы ролей
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Создание таблицы объявлений
CREATE TABLE announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    boost_end DATE DEFAULT '0001-01-01',
    sold BOOLEAN DEFAULT FALSE,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создание таблицы комментариев
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    text TEXT,
    author_id BIGINT,
    announcement_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (announcement_id) REFERENCES announcements(id) ON DELETE CASCADE
);

-- Создание таблицы чатов
CREATE TABLE chats (
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT,
    user2_id BIGINT,
    FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Создание таблицы сообщений
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT,
    sender_id BIGINT,
    content TEXT,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Создание таблицы оценок
CREATE TABLE estimations (
    id BIGSERIAL PRIMARY KEY,
    estimation INTEGER NOT NULL
);

-- Создание связи многие ко многим между пользователями и оценками
CREATE TABLE users_estimations (
    user_id BIGINT,
    estimation_id BIGINT,
    PRIMARY KEY (user_id, estimation_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (estimation_id) REFERENCES estimations(id) ON DELETE CASCADE
);

-- Создание связи многие ко многим между пользователями и ролями
CREATE TABLE users_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
