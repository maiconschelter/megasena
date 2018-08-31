CREATE DATABASE megasena
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;

CREATE TABLE public.results(
  rescodigo INTEGER NOT NULL,
  resdate DATE NOT NULL,
  resnumber INTEGER[] NOT NULL
);

ALTER TABLE public.results ADD CONSTRAINT pk_results PRIMARY KEY(rescodigo);