create table activity (
    id              bigserial primary key,
    text            varchar not null,
    task_id         bigint not null references task(id) on delete cascade,
    created_at      timestamp not null default now(),
    updated_at      timestamp
);