create table users
      (
       id                    bigserial,
       username              varchar(30) not null unique,
       password              varchar(80) not null,
       email                 varchar(50) unique,
       primary key (id)
      );

create table roles
      (
       id                    bigserial,
       name                  varchar(50) not null,
       primary key (id)
      );

create table user_role
      (
       user_id               bigint not null,
       role_id               bigint not null,
       primary key (user_id, role_id),
       foreign key (user_id) references users (id),
       foreign key (role_id) references roles (id)
      );

