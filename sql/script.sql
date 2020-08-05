CREATE DATABASE desafioStefanini;

CREATE TABLE public.usuario (
  id bigint NOT NULL,
  NOME character varying(100) NOT NULL,
  SEXO character varying(10) NOT NULL,
  EMAIL character varying(30) NOT NULL,
  NASCIMENTO character varying(15) NOT NULL,
  NATURALIDADE character varying(15) NOT NULL,
  NASCIONALIDADE character varying(15) NOT NULL,
  CPF character varying(15),
  data_criacao date NOT NULL,
  CONSTRAINT usuario_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
