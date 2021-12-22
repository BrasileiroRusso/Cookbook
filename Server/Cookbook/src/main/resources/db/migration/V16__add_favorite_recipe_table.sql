create table favorite_recipe
      (
       user_id      bigint not null,
       recipe_id    bigint not null,
       primary key (user_id, recipe_id),
       foreign key (recipe_id) references recipe (id),
       foreign key (user_id)   references users  (id)
      );
