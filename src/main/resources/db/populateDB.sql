DELETE
from meals;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq
  RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

insert into meals (id, dateTime, description, calories, user_id)
values (2, '2015-05-30 10:00:00', 'Завтрак', 500, 100000),
       (3, '2015-05-30 13:00:00', 'Обед', 1000, 100000),
       (4, '2015-05-30 20:00:00', 'Ужин', 500, 100000),
       (5, '2015-05-31 10:00:00', 'Завтрак', 1000, 100001),
       (6, '2015-05-31 13:00:00', 'Обед', 500, 100001),
       (7, '2015-05-31 20:00:00', 'Ужин', 510, 100001);
