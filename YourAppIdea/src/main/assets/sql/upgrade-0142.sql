drop table T_TUTORIAL;

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

