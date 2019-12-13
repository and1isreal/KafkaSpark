create table if not exists limits_per_hour (
                                          id int not null,
                                          limit_name varchar(25) not null,
                                          limit_value int not null,
                                          effective_date timestamp

);