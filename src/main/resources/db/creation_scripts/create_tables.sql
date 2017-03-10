-- Create the database first, then connect to it in pgAdmin before running this script

-- Sequence: public.hibernate_sequence

-- DROP SEQUENCE public.hibernate_sequence;

CREATE SEQUENCE public.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1000
  CACHE 1;
ALTER TABLE public.hibernate_sequence
  OWNER TO postgres;

-- Table: public.account

-- DROP TABLE public.account;

CREATE TABLE public.account
(
  id bigint NOT NULL,
  approved boolean NOT NULL,
  email character varying(255),
  password character varying(255),
  role character varying(255),
  loginattempts integer,
  CONSTRAINT account_pkey PRIMARY KEY (id),
  CONSTRAINT uk_email UNIQUE (email)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.account
  OWNER TO postgres;

  -- Table: public.accountpasswordhistory

-- DROP TABLE public.accountpasswordhistory;

CREATE TABLE public.accountpasswordhistory
(
  id bigint NOT NULL,
  password character varying(255) NOT NULL,
  lastused timestamp without time zone,
  CONSTRAINT accountpasswordhistory_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountpasswordhistory
  OWNER TO postgres;

  -- Table: public.account_accountpasswordhistory

-- DROP TABLE public.account_accountpasswordhistory;

CREATE TABLE public.account_accountpasswordhistory
(
  account_id bigint NOT NULL,
  accountpasswordhistories_id bigint NOT NULL,
  CONSTRAINT fk_account_id FOREIGN KEY (account_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_accountpasswordhistories_id FOREIGN KEY (accountpasswordhistories_id)
      REFERENCES public.accountpasswordhistory (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.account_accountpasswordhistory
  OWNER TO postgres;
  
-- Insert admin user with password P@ssword1
INSERT INTO public.account(id, approved, email, password, role, loginattempts)
VALUES (1, 't', 'admin@xxshellxx.com', '7343dd0af9f15eab15abd48bd5d330f5393db5766c8f86a4c531c478ecccd0953694d76066e62609', 'ROLE_ADMIN', 0);