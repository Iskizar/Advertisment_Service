-- Вставка пользователей
INSERT INTO users (first_name, last_name, email, password) VALUES
('John', 'Doe', 'john.doe@example.com', '123'),
('Jane', 'Smith', 'jane.smith@example.com', '123'),
('Alice', 'Johnson', 'alice.johnson@example.com', '123');

-- Вставка ролей
INSERT INTO roles (name) VALUES
('USER'),
('ADMIN');

-- Вставка объявлений
INSERT INTO announcements (title, description, boost_end, sold, author_id) VALUES
('For Sale: Bicycle', 'A brand new bicycle in excellent condition.', '2024-08-01', FALSE, 1),
('Looking for a Roommate', 'Room available in downtown apartment.', '2024-09-01', FALSE, 2),
('Free Concert Tickets', 'Two tickets for a free concert this weekend.', '2024-07-30', FALSE, 3);

-- Вставка комментариев
INSERT INTO comments (text, author_id, announcement_id) VALUES
('Great deal on the bicycle!', 2, 1),
('I’m interested in the room.', 1, 2),
('Can you send me more details?', 2, 3);

-- Вставка чатов
INSERT INTO chats (user1_id, user2_id) VALUES
(1, 2),
(2, 3);

-- Вставка сообщений
INSERT INTO messages (chat_id, sender_id, content) VALUES
(1, 1, 'Hi, I’m interested in the bicycle.'),
(1, 2, 'Sure, I can send more details.'),
(2, 3, 'Do you have any more information about the concert?');

-- Вставка оценок
INSERT INTO estimations (estimation) VALUES
(5),
(4),
(3);

-- Вставка связей многие ко многим между пользователями и оценками
INSERT INTO users_estimations (user_id, estimation_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3);

-- Вставка связей многие ко многим между пользователями и ролями
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(1, 1);
