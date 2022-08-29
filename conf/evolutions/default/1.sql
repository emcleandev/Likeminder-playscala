# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blog (
  id                            integer auto_increment not null,
  owner_id                      integer,
  name                          varchar(255),
  aboutme                       varchar(255),
  constraint uq_blog_owner_id unique (owner_id),
  constraint pk_blog primary key (id)
);

create table blog_followers_bloggers (
  blog_id                       integer not null,
  blogger_id                    integer not null,
  constraint pk_blog_followers_bloggers primary key (blog_id,blogger_id)
);

create table blogger (
  id                            integer auto_increment not null,
  title                         varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  privillages                   integer default 1,
  constraint uq_blogger_username unique (username),
  constraint pk_blogger primary key (id)
);

create table blogger_post (
  blogger_id                    integer not null,
  post_id                       integer not null,
  constraint pk_blogger_post primary key (blogger_id,post_id)
);

create table blogger_reported_post (
  blogger_id                    integer not null,
  post_id                       integer not null,
  constraint pk_blogger_reported_post primary key (blogger_id,post_id)
);

create table comments (
  id                            integer auto_increment not null,
  post_id                       integer,
  blogger_id                    integer,
  comment                       varchar(255),
  created_at                    datetime,
  constraint pk_comments primary key (id)
);

create table post (
  id                            integer auto_increment not null,
  blog_id                       integer,
  title                         varchar(255),
  image                         varchar(255),
  caption                       varchar(255),
  created_at                    datetime,
  sports                        tinyint(1) default 0 not null,
  constraint pk_post primary key (id)
);

create table post_tags (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_post_tags primary key (id)
);

create table post_tags_post (
  post_tags_id                  integer not null,
  post_id                       integer not null,
  constraint pk_post_tags_post primary key (post_tags_id,post_id)
);

create table blog_category (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_blog_category primary key (id)
);

create table blog_category_blog (
  blog_category_id              integer not null,
  blog_id                       integer not null,
  constraint pk_blog_category_blog primary key (blog_category_id,blog_id)
);

alter table blog add constraint fk_blog_owner_id foreign key (owner_id) references blogger (id) on delete restrict on update restrict;

alter table blog_followers_bloggers add constraint fk_blog_followers_bloggers_blog foreign key (blog_id) references blog (id) on delete restrict on update restrict;
create index ix_blog_followers_bloggers_blog on blog_followers_bloggers (blog_id);

alter table blog_followers_bloggers add constraint fk_blog_followers_bloggers_blogger foreign key (blogger_id) references blogger (id) on delete restrict on update restrict;
create index ix_blog_followers_bloggers_blogger on blog_followers_bloggers (blogger_id);

alter table blogger_post add constraint fk_blogger_post_blogger foreign key (blogger_id) references blogger (id) on delete restrict on update restrict;
create index ix_blogger_post_blogger on blogger_post (blogger_id);

alter table blogger_post add constraint fk_blogger_post_post foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_blogger_post_post on blogger_post (post_id);

alter table blogger_reported_post add constraint fk_blogger_reported_post_blogger foreign key (blogger_id) references blogger (id) on delete restrict on update restrict;
create index ix_blogger_reported_post_blogger on blogger_reported_post (blogger_id);

alter table blogger_reported_post add constraint fk_blogger_reported_post_post foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_blogger_reported_post_post on blogger_reported_post (post_id);

alter table comments add constraint fk_comments_post_id foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_comments_post_id on comments (post_id);

alter table comments add constraint fk_comments_blogger_id foreign key (blogger_id) references blogger (id) on delete restrict on update restrict;
create index ix_comments_blogger_id on comments (blogger_id);

alter table post add constraint fk_post_blog_id foreign key (blog_id) references blog (id) on delete restrict on update restrict;
create index ix_post_blog_id on post (blog_id);

alter table post_tags_post add constraint fk_post_tags_post_post_tags foreign key (post_tags_id) references post_tags (id) on delete restrict on update restrict;
create index ix_post_tags_post_post_tags on post_tags_post (post_tags_id);

alter table post_tags_post add constraint fk_post_tags_post_post foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_post_tags_post_post on post_tags_post (post_id);

alter table blog_category_blog add constraint fk_blog_category_blog_blog_category foreign key (blog_category_id) references blog_category (id) on delete restrict on update restrict;
create index ix_blog_category_blog_blog_category on blog_category_blog (blog_category_id);

alter table blog_category_blog add constraint fk_blog_category_blog_blog foreign key (blog_id) references blog (id) on delete restrict on update restrict;
create index ix_blog_category_blog_blog on blog_category_blog (blog_id);


# --- !Downs

alter table blog drop foreign key fk_blog_owner_id;

alter table blog_followers_bloggers drop foreign key fk_blog_followers_bloggers_blog;
drop index ix_blog_followers_bloggers_blog on blog_followers_bloggers;

alter table blog_followers_bloggers drop foreign key fk_blog_followers_bloggers_blogger;
drop index ix_blog_followers_bloggers_blogger on blog_followers_bloggers;

alter table blogger_post drop foreign key fk_blogger_post_blogger;
drop index ix_blogger_post_blogger on blogger_post;

alter table blogger_post drop foreign key fk_blogger_post_post;
drop index ix_blogger_post_post on blogger_post;

alter table blogger_reported_post drop foreign key fk_blogger_reported_post_blogger;
drop index ix_blogger_reported_post_blogger on blogger_reported_post;

alter table blogger_reported_post drop foreign key fk_blogger_reported_post_post;
drop index ix_blogger_reported_post_post on blogger_reported_post;

alter table comments drop foreign key fk_comments_post_id;
drop index ix_comments_post_id on comments;

alter table comments drop foreign key fk_comments_blogger_id;
drop index ix_comments_blogger_id on comments;

alter table post drop foreign key fk_post_blog_id;
drop index ix_post_blog_id on post;

alter table post_tags_post drop foreign key fk_post_tags_post_post_tags;
drop index ix_post_tags_post_post_tags on post_tags_post;

alter table post_tags_post drop foreign key fk_post_tags_post_post;
drop index ix_post_tags_post_post on post_tags_post;

alter table blog_category_blog drop foreign key fk_blog_category_blog_blog_category;
drop index ix_blog_category_blog_blog_category on blog_category_blog;

alter table blog_category_blog drop foreign key fk_blog_category_blog_blog;
drop index ix_blog_category_blog_blog on blog_category_blog;

drop table if exists blog;

drop table if exists blog_followers_bloggers;

drop table if exists blogger;

drop table if exists blogger_post;

drop table if exists blogger_reported_post;

drop table if exists comments;

drop table if exists post;

drop table if exists post_tags;

drop table if exists post_tags_post;

drop table if exists blog_category;

drop table if exists blog_category_blog;

