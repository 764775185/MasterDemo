CREATE table user (
    id int(11) not null primary key auto_increment,
    number char(10) not null unique,
    username varchar(50) not null,
    email varchar(50) not null unique,
    password varchar(255) not null,
    role varchar(20) not null,
    pkey text not null,
    attr varchar(255)
) engine=InnoDB default charset=utf8;

create table file_data (
    id int(11) not null primary key auto_increment,
    title varchar(50) not null,
    content text not null,
    cipher varchar(255) not null,
    keyword_vector text not null,
    timestamp varchar(255) not null,
    authorize_role varchar(50) not null,
    user_id int(11) not null,
    foreign key (user_id) references user(id)
) engine=InnoDB default charset=utf8;

create table keyword(
    id int(11) not null primary key auto_increment,
    word varchar(512) not null unique,
    sum int(11) not null
)engine=InnoDB default charset=utf8;

create table cpabe_data (
    id int(11) not null primary key auto_increment,
    title varchar(50) not null,
    content text not null,
    cipher varchar(255) not null,
    timestamp varchar(255) not null,
    policy varchar(255) not null,
    note varchar(255),
    user_id int(11) not null,
    foreign key (user_id) references user(id)
) engine=InnoDB default charset=utf8;
