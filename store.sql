drop database if exists mallDB;
create database if not exists mallDB;

use malldb;
drop table if exists store;
create table if not exists store(
	storeName char(10) not null,
    storeKind char(2) not null,
	storePhone char(12) not null,
	floor tinyint not null,
    constraint pk_storeName primary key (storeName)
);

drop table if exists store;
drop table if exists product;
create table if not exists product(
	storeName char(10) not null,
    name char(9) not null,
    kind char(9) not null,
    price mediumint unsigned not null,
    stock smallint not null,
    date date not null,
	constraint fk_product_store_storeName FOREIGN KEY (storeName) REFERENCES store (storeName)
);

insert into store values('한국의류', '의류' , '02-2000-200', 3);

insert into product values('한국의류', '검정티', '상의', 30000, 20, now());

delete from store where storename = '한국의류';

select * from store;
select * from product;

-- 인덱스 설정
create index idx_store on store(storeName); 
create index idx_product on product(storeName);

SELECT * FROM deletestore;
SELECT * FROM deleteproduct;

delete from store where storeName = '한국의류';

drop table if exists deletestore;
create table deletestore(
	storeName char(10) not null,
    storeKind char(2) not null,
	storePhone char(12) not null,
	floor tinyint not null,
    deletedate datetime
);

drop table if exists deleteproduct;
create table deleteproduct(
	storeName char(10) not null,
    name char(9) not null,
    kind char(9) not null,
    price mediumint unsigned not null,
    stock smallint not null,
    deletedate datetime
);

DELIMITER $$
create trigger trg_delete_Store
	after delete
	on store 
	for each row
begin
    insert into deletestore values(OLD.storeName , OLD.storeKind , OLD.storePhone, OLD.floor, now());
end $$
DELIMITER ; 

DELIMITER $$
create trigger trg_delete_product
	after delete
	on product
	for each row
begin
    insert into deleteproduct values(OLD.storeName , OLD.name , OLD.kind, OLD.price, OLD.stock, now());
end $$
DELIMITER ;

select * from deletestore;
select * from deleteproduct;


DELIMITER $$
create procedure procedure_delete_store_product (
	In in_storeName char(10)
)
begin
	delete from product where storeName = in_storeName;
	delete from store where storeName = in_storeName;
end $$
delimiter ;

call procedure_delete_store_product('한국의류');
