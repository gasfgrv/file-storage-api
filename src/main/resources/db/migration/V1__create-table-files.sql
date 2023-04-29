create table "public"."files" (
  "id" UUID not null,
  "name" varchar(255) not null,
  "type" varchar(255) not null,
  "data" BYTEA not null,
  constraint "files_pkey" primary key ("id")
);
