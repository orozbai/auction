create table lot
(
    id          integer not null
        primary key,
    name        varchar(70),
    description varchar(300),
    quantity    integer
);

alter table lot
    owner to "RegDbUser";

