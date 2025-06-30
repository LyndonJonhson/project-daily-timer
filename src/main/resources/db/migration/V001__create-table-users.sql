create table users (
    id              varchar primary key,
    name            varchar not null,
    email           varchar unique not null,
    password        varchar not null,
    role            varchar not null,
    created_at      timestamp not null default now(),
    updated_at      timestamp
);