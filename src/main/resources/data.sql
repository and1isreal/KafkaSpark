delete from limits_per_hour;

insert into limits_per_hour (id, limit_name, limit_value, effective_date) values (1, 'min', 1024, CURRENT_TIMESTAMP );
insert into limits_per_hour (id, limit_name, limit_value, effective_date) values (2, 'max', 1073741824, CURRENT_TIMESTAMP);