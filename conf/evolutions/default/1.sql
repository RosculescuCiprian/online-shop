# --- !Ups

create table "clients" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "age" int not null
);

create table "suppliers" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "desc" int not null
);

create table "products" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "supplierId" bigint not null,
  "desc" int not null,
  FOREIGN KEY (SUP_FK) REFERENCES suppliers(id)
);



# --- !Downs

drop table "people" if exists;
drop table "suppliers" if exists;
