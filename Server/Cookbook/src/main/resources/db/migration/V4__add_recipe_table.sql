create table recipe
      (
       id                    bigserial                   primary key,
       user_id               bigint not null,
       category_id           bigint not null,
       title                 varchar(80) not null,
       description           varchar(255) not null,
       recipe                text,
       foreign key (user_id)     references users    (id),
       foreign key (category_id) references category (id)
      );