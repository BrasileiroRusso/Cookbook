create table recipe_ingredient
      (
       recipe_id          bigint not null,
       ingredient_id      bigint not null,
       unit_id            bigint not null,
       amount             numeric(15, 4) not null,
       primary key (recipe_id, ingredient_id),
       foreign key (recipe_id)     references recipe (id),
       foreign key (ingredient_id) references ingredient (id),
       foreign key (unit_id)       references unit (id)
      );
