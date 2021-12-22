create table user_rating
      (
       recipe_id          bigint  not null,
       user_id            bigint  not null,
       rate               integer not null,
       primary key (recipe_id, user_id),
       foreign key (recipe_id) references recipe (id),
       foreign key (user_id)   references users  (id)
      );