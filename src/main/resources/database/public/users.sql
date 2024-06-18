create table users
(
    id       serial
        primary key,
    name     varchar(100) not null,
    age      integer,
    password varchar(255) not null,
    email    varchar(255) not null
        unique,
    role     varchar(255)
);

alter table users
    owner to "RegDbUser";

