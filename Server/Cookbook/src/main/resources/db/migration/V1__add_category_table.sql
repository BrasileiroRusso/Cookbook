CREATE TABLE category
      (
       id         bigserial                         primary key
      ,title      varchar(255)   not null
      );

INSERT INTO category
      (
       title
      )
VALUES(
      'OTHER'
      );