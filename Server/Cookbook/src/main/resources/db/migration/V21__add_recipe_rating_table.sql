create table recipe_rating
      (
       recipe_id           bigint not null,
       ratings             bigint not null,
       total_rating        bigint not null,
       primary key (recipe_id),
       foreign key (recipe_id)    references recipe (id)
      );