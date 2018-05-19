--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.7
-- Dumped by pg_dump version 9.6.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: exercise; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE exercise (
    name text NOT NULL
);


ALTER TABLE exercise OWNER TO marin;

--
-- Name: exercise_type; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE exercise_type (
    exercise_name text NOT NULL,
    type_name text NOT NULL
);


ALTER TABLE exercise_type OWNER TO marin;

--
-- Name: person; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE person (
    id integer NOT NULL,
    username text NOT NULL
);


ALTER TABLE person OWNER TO marin;

--
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: marin
--

CREATE SEQUENCE person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE person_id_seq OWNER TO marin;

--
-- Name: person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: marin
--

ALTER SEQUENCE person_id_seq OWNED BY person.id;


--
-- Name: routine; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE routine (
    id integer NOT NULL,
    user_id integer,
    name text NOT NULL,
    appraisal integer DEFAULT 0 NOT NULL,
    comment text
);


ALTER TABLE routine OWNER TO marin;

--
-- Name: routine_exercise; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE routine_exercise (
    routine_id integer NOT NULL,
    exercise_name text NOT NULL,
    sets integer,
    reps integer
);


ALTER TABLE routine_exercise OWNER TO marin;

--
-- Name: routine_id_seq; Type: SEQUENCE; Schema: public; Owner: marin
--

CREATE SEQUENCE routine_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routine_id_seq OWNER TO marin;

--
-- Name: routine_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: marin
--

ALTER SEQUENCE routine_id_seq OWNED BY routine.id;


--
-- Name: type; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE type (
    name text NOT NULL
);


ALTER TABLE type OWNER TO marin;

--
-- Name: workout; Type: TABLE; Schema: public; Owner: marin
--

CREATE TABLE workout (
    user_id integer NOT NULL,
    routine_id integer NOT NULL,
    location text,
    date timestamp with time zone
);


ALTER TABLE workout OWNER TO marin;

--
-- Name: person id; Type: DEFAULT; Schema: public; Owner: marin
--

ALTER TABLE ONLY person ALTER COLUMN id SET DEFAULT nextval('person_id_seq'::regclass);


--
-- Name: routine id; Type: DEFAULT; Schema: public; Owner: marin
--

ALTER TABLE ONLY routine ALTER COLUMN id SET DEFAULT nextval('routine_id_seq'::regclass);


--
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (name);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: person person_username_key; Type: CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_username_key UNIQUE (username);


--
-- Name: routine routine_pkey; Type: CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY routine
    ADD CONSTRAINT routine_pkey PRIMARY KEY (id);


--
-- Name: type type_pkey; Type: CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY type
    ADD CONSTRAINT type_pkey PRIMARY KEY (name);


--
-- Name: exercise_type exercise_type_exercise_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY exercise_type
    ADD CONSTRAINT exercise_type_exercise_name_fkey FOREIGN KEY (exercise_name) REFERENCES exercise(name);


--
-- Name: exercise_type exercise_type_type_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY exercise_type
    ADD CONSTRAINT exercise_type_type_name_fkey FOREIGN KEY (type_name) REFERENCES type(name);


--
-- Name: routine_exercise routine_exercise_exercise_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY routine_exercise
    ADD CONSTRAINT routine_exercise_exercise_name_fkey FOREIGN KEY (exercise_name) REFERENCES exercise(name);


--
-- Name: routine_exercise routine_exercise_routine_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY routine_exercise
    ADD CONSTRAINT routine_exercise_routine_id_fkey FOREIGN KEY (routine_id) REFERENCES routine(id);


--
-- Name: routine routine_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY routine
    ADD CONSTRAINT routine_user_id_fkey FOREIGN KEY (user_id) REFERENCES person(id);


--
-- Name: workout workout_routine_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY workout
    ADD CONSTRAINT workout_routine_id_fkey FOREIGN KEY (routine_id) REFERENCES routine(id);


--
-- Name: workout workout_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: marin
--

ALTER TABLE ONLY workout
    ADD CONSTRAINT workout_user_id_fkey FOREIGN KEY (user_id) REFERENCES person(id);


--
-- PostgreSQL database dump complete
--

