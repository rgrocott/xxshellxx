-- Note that this database and role are not associated with the database you're connected to,
-- but pgAdmin won't let you run a script without being connected to a database.

-- Database: xxshellxx

-- DROP DATABASE xxshellxx;

CREATE DATABASE xxshellxx
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'English_United States.1252'
       LC_CTYPE = 'English_United States.1252'
       CONNECTION LIMIT = -1;

-- Role: xxshellxx

-- DROP ROLE xxshellxx;
-- This is just for local development databases so no need for encryption. 
CREATE ROLE xxshellxx LOGIN
  UNENCRYPTED PASSWORD 'password1'
  SUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
