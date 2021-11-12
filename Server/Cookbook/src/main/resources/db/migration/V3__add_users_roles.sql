insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into users (username, password, email)
values ('victor', '$2a$10$MwfhoW1Lpjl31DO0wSw2ruK.GPwgSQSBDXqKpdH5XGVmxkIKFxcEq', 'victor@gmail.com');

insert into users (username, password, email)
values ('evgesha', '$2a$10$MwfhoW1Lpjl31DO0wSw2ruK.GPwgSQSBDXqKpdH5XGVmxkIKFxcEq', 'evgesha@gmail.com');

insert into users (username, password, email)
values ('evgeniya', '$2a$10$MwfhoW1Lpjl31DO0wSw2ruK.GPwgSQSBDXqKpdH5XGVmxkIKFxcEq', 'evgeniya@gmail.com');

insert into users (username, password, email)
values ('igor', '$2a$10$MwfhoW1Lpjl31DO0wSw2ruK.GPwgSQSBDXqKpdH5XGVmxkIKFxcEq', 'igor@gmail.com');



insert into user_role (user_id, role_id)
values (1, 1),
       (1, 2);

insert into user_role (user_id, role_id)
values (2, 1),
       (2, 2);

insert into user_role (user_id, role_id)
values (3, 1),
       (3, 2);

insert into user_role (user_id, role_id)
values (4, 1),
       (4, 2);