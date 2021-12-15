create table recipe_tag
      (
       recipe_id    bigint not null,
       tag_id       bigint not null,
       primary key (recipe_id, tag_id),
       foreign key (recipe_id) references recipe   (id),
       foreign key (tag_id)    references hash_tag (id)
      );