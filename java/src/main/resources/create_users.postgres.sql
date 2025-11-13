insert into APP_USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, CREATED_DATE, CREATED_BY)
values('admin', 'abc12345', 'adminF', 'adminL', 'admin@abc.com', now(), 'SYSTEM')
on conflict (USERNAME) DO NOTHING;
insert into APP_ROLE(USERNAME, ROLE, CREATED_DATE, CREATED_BY) values ('admin', 'ADMIN', now(), 'SYSTEM')
on conflict(USERNAME, ROLE) DO NOTHING;
