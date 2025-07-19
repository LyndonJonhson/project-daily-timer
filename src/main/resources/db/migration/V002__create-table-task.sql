create table task (
    id              bigserial primary key,
    title           varchar not null,
    description     varchar,
    user_id         varchar not null references users(id) on delete cascade,
    created_at      timestamp not null default now(),
    updated_at      timestamp
);