merge into APP_USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, CREATED_DATE, CREATED_BY)
values('admin', <need-update>, 'adminF', 'adminL', 'admin@abc.com', now(), 'SYSTEM');
merge into APP_ROLE(USERNAME, ROLE, CREATED_DATE, CREATED_BY) values ('admin', 'ADMIN', now(), 'SYSTEM');

