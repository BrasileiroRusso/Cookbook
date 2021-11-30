create table uploaded_file
      (
       id                    bigserial                   primary key,
       filename              varchar(60)  not null,
       user_id               bigint       not null,
       foreign key (user_id) references users (id)
      );