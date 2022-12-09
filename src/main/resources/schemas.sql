DROP TABLE if EXISTS userinfo;

create table userinfo(id serial primary key, givenname varchar(255),
surname varchar(255),
username varchar(255),
email varchar(255),
password varchar(255));