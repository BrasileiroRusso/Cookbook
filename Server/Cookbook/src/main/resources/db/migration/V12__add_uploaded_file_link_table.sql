create table uploaded_file_link
      (
       id                    bigserial               primary key,
       object_id             bigint       not null,
       object_type           varchar(10)  not null,
       object_part           varchar(30)  not null,
       uploaded_file_id      bigint       not null,
       description           varchar(255) not null,
       foreign key (uploaded_file_id)     references uploaded_file (id)
      );