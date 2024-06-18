create table customer
(
    id      integer not null
        primary key,
    lot_id  integer
        references lot,
    user_id integer
        references users
);

alter table customer
    owner to "RegDbUser";

