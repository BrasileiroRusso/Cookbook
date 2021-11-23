create table unit
      (
       id                    bigserial                   primary key,
       brief_name            varchar(80)  not null,
       name                  varchar(255) not null
      );