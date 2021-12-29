create table unit_type
      (
       id            bigserial               primary key,
       brief_name    varchar(30)  not null,
       name          varchar(100) not null,
       description   text         not null
      );