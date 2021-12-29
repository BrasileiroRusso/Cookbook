ALTER TABLE ingredient ADD COLUMN parent_id bigint REFERENCES ingredient (id),
                       ADD COLUMN is_group boolean;

update ingredient
   set is_group = false;