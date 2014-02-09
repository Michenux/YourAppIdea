create table T_FRIEND (
	_id integer primary key autoincrement, 
	JOB TEXT not null,
	FACE TEXT not null,
	NAME text not null);

insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Bertrand', 'Seller', 'face08');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Romain', 'Teacher', 'face03');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Alexis', 'Actor', 'face01');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Vincent', 'Doctor', 'face09');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Julie', 'Project Manager', 'face06');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Florence', 'Baker', 'face03');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Lydie', 'Web Designer', 'face07');
insert into T_FRIEND(_id, NAME, JOB, FACE) values (null, 'Vanessa', 'House wife', 'face04');

create table T_TUTORIAL (
	_id integer primary key autoincrement,
	POSTID INTEGER not null,
	TITLE TEXT not null,
	DESCRIPTION TEXT not null,
	THUMBNAIL TEXT not null,
	URL TEXT not null,
	CONTENT TEXT not null,
	AUTHOR TEXT not null,
	DATECREATION INTEGER  not null,
	DATEMODIFICATION INTEGER  not null);

create table T_PLACE (
	_id integer primary key autoincrement,
	NAME TEXT not null,
	COUNTRY TEXT not null,
	URL TEXT not null,
	LONGITUDE REAL not null,
	LATITUDE REAL not null );

insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Statue of Liberty','United States','http://lmichenaud.free.fr/yai/Statue%20of%20Liberty.jpg',-74.044444, 40.689167);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Effeil Tower','France','http://lmichenaud.free.fr/yai/Effeil%20Tower.jpg',2.294694, 48.858093);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Great Sphinx','Egypt','http://lmichenaud.free.fr/yai/Great%20Sphinx.jpg',31.137751, 29.975309);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Taj Mahal','India','http://lmichenaud.free.fr/yai/Taj%20Mahal.jpg',78.0420685, 27.1731476);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Colosseum','Italy','http://lmichenaud.free.fr/yai/Colosseum.jpg',12.492314999999962, 41.890268);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Angkor','Cambodge','http://lmichenaud.free.fr/yai/Angkor.jpg',103.86000000000001, 13.4256);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Stonehenge','United Kingdom','http://lmichenaud.free.fr/yai/Stonehenge.jpg',-1.8261171000000331, 51.1788997);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Acropolis of Athens','Greece','http://lmichenaud.free.fr/yai/Acropolis%20of%20Athens.JPG',23.724831900000027, 37.9704259);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Potala Palace','Tibet','http://lmichenaud.free.fr/yai/Potala%20Palace.jpg',91.11630390000005, 29.6528005);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'The Sultan Ahmed Mosque','Turkey','http://lmichenaud.free.fr/yai/The%20Sultan%20Ahmed%20Mosque.jpg',28.976946999999996, 41.005804);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'The Kaaba','Saudi Arabia','http://lmichenaud.free.fr/yai/The%20Kaaba.jpg',29.907371199999943, 31.1846033);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'St. Basil''s Cathedral','Russia','http://lmichenaud.free.fr/yai/St.%20Basil''s%20Cathedral.jpg',37.62316099999998, 55.75250399999999);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Machu Picchu','Peru','http://lmichenaud.free.fr/yai/Machu%20Picchu.jpg',-72.54594329999998, -13.163721);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Big Ben','United Kingdom','http://lmichenaud.free.fr/yai/Big%20Ben.jpg',-0.12452989999997044, 51.5007396);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Tower of Pisa','Italy','http://lmichenaud.free.fr/yai/Tower%20of%20Pisa.jpg',10.396597100000008, 43.7229516);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Christ the Redeemer','Brazil','http://lmichenaud.free.fr/yai/Christ%20the%20Redeemer.jpg',-43.210841500000015, -22.9514737);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Easter Island','Chile','http://lmichenaud.free.fr/yai/Easter%20Island.jpg',-43.210841500000015, -27.1211919);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Capitol Hill','United States','http://lmichenaud.free.fr/yai/Capitol%20Hill.jpg',-77.00174649999997, 38.8844371);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Niagara Falls','Canada','http://lmichenaud.free.fr/yai/Niagara%20Falls.jpg',-79.08610759999999, 43.0903891);
insert into T_PLACE(_id, NAME, COUNTRY, URL, LONGITUDE, LATITUDE) values (null,'Chichen Itza','Mexique','http://lmichenaud.free.fr/yai/Chichen%20Itza.jpg',-88.56861099999998, 20.6795187);


create table T_CITY (
	_id integer primary key autoincrement,
	NAME TEXT not null,
	COUNTRY TEXT not null,
	LONGITUDE REAL not null,
	LATITUDE REAL not null );

insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Tokyo','Japan', 139.7513889, 35.685);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Shanghai','China', 121.4086111, 31.005);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Jakarta','Indonesia', 106.8294444, -6.1744444);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Seoul','Korea', 126.9788888889, 37.5658333333);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Delhi','India', 77.216667, 28.666667);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Mexico','Mexico', -99.138611, 19.434167);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Manila','Philippines', 120.9822222, 14.6041667);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'New York','United State', -74.0063889, 40.7141667);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Sao Paulo','Brazil', -46.6166667, -23.5333333);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Moscow','Russia', 37.6155556, 55.7522222);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Cairo','Egypt', 31.25, 30.05 );
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'London','Great Britain', -0.116667, 51.5 );
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Buenos Aires','Argentina', -58.3816666667, -34.6033333333);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Bangkok','Thailand', 100.516667, 13.75);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Istanbul','Turkey', 28.9647222, 41.0186111);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Paris','France', 2.333333, 48.866667);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Lagos','Nigeria', 3.3958333, 6.4530556);
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Bogota','Colombia', -74.0833333, 4.6 );
insert into T_CITY(_id, NAME, COUNTRY, LONGITUDE, LATITUDE) values (null,'Berlin','Germany', 13.4, 52.5166667);