CREATE TABLE person (
    id text NOT NULL PRIMARY KEY unique,
    username text NOT NULL,
    email text NOT NULL
);

CREATE TABLE routine (
    id bigserial NOT NULL primary key unique,
    user_id text references person(id),
    name text NOT NULL,
    appraisal integer DEFAULT 0 NOT NULL,
    comment text
);

CREATE TABLE routine_exercise (
    routine_id bigint NOT NULL references routine(id),
    exercise_name text NOT NULL references exercise(name),
    sets integer,
    reps integer
);

create table routine_type(
    routine_id bigint NOT NULL references routine(id),
    type_name text NOT NULL references type(name)
);

CREATE TABLE workout (
    user_id text NOT NULL references person(id),
    routine_id bigint NOT NULL references routine(id),
    location text,
    date timestamp with time zone
);

create table likes(
    user_id text references person(id), 
    routine_id bigint references routine(id))
);

