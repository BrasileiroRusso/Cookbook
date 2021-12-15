create table hash_tag
      (
       id                    bigserial                   primary key,
       name                  varchar(50) not null,
       description           text        not null
      );