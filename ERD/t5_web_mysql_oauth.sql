alter table t5_user
add column provider VARCHAR(40);

alter table t5_user
add column providerId varchar(200);

desc t5_user;

select * from t5_user order by id desc;