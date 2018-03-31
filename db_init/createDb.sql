
create table yelp_user
(
    yelping_since varchar2(10),
    name varchar2(255),
    userid varchar2(30),
    review_count number,
    average_stars float,
    primary key (userid)
);

create table friends
(
    user_id varchar2(30),
    friend_id varchar2(30),
    primary key (user_id,friend_id),
    foreign key (user_id) references yelp_user (userid) on delete cascade
);

create table business
(
    bid varchar2(30),
    address varchar2(255),
    isOpen number(1),
    city varchar2(255),
    review_count varchar2(10),
    name varchar2(255),
    neighborhoods varchar2(255),
    state varchar2(255),
    stars varchar2(10),
    primary key (bid)
);

create index b_state ON Business(State);
create index b_city ON Business(City);

create table business_main_category
(
    bid varchar2(30),
    cname varchar2(255),
    primary key (bid, cname),
    foreign key (bid) references business (bid) on delete cascade
);

create table business_sub_category
(
    bid varchar2(30),
    cname varchar2(255),
    primary key(bid, cname),
    foreign key(bid) references business (bid) on delete cascade
);

create table business_attribute
(
    bid varchar2(30),
    attribute varchar2(255),
    primary key(bid, attribute),
    foreign key (bid) references business (bid) on delete cascade
);

create table business_hour
(
    bid varchar2(30),
    day number(2),
    open number(10),
    close number(10),
    primary key(bid, day),
    foreign key(bid) references business(bid) on delete cascade
);

create index bh_open on business_hour(open);
create index bh_close on business_hour(close);

create table business_checkin
(
    day number(2),
    hour number,
    count number,
    bid varchar2(30),
    primary key (bid, day, hour),
    foreign key (bid) references business (bid) on delete cascade
);

create index bc_count on business_checkin(count);


create table yelp_review
(
    reviewid varchar2(30),
    userid varchar2(255),
    bid varchar2(30),
    funny_count number(2),
    useful_count number(2),
    cool_count number(2),
    stars varchar2(2),
    reviewdate varchar2(10),
    reviewtext long,
    primary key (reviewid),
    foreign key (bid) references business (bid) on delete cascade,
    foreign key (userid) references yelp_user (userid) on delete cascade
);