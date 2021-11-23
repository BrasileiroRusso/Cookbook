create table recipe_step
      (
       recipe_id          bigint  not null,
       step_num           integer not null,
       description        text    not null,
       image_path         text,
       primary key (recipe_id, step_num),
       foreign key (recipe_id) references recipe (id)
      );
