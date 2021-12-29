ALTER TABLE user_rating ADD COLUMN comment text;

update user_rating
   set comment = '';