ALTER TABLE recipe ADD COLUMN prepare_time integer, ADD COLUMN comment text;

UPDATE recipe
   SET prepare_time = 0,
       comment      = '';