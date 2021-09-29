BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS seq_user_id;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;


CREATE TABLE users (
	user_id int DEFAULT nextval('seq_user_id'::regclass) NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	email varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);


INSERT INTO users (username,password_hash,role,email) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN','k8tied1@gmail.com');

DROP TABLE IF EXISTS expense;
CREATE TABLE expense (
	expense_id SERIAL NOT NULL,
	description varchar(50) NOT NULL,
	CONSTRAINT PK_expense_id PRIMARY KEY (expense_id)
);
INSERT INTO expense (description) VALUES('Cheap <1k'), ('Medium 2k-5k'), ('Expensive 5k-10k'),('Holy Shit! >10k');

DROP TABLE IF EXISTS time_of_year;
CREATE TABLE time_of_year(
	toy_id SERIAL NOT NULL,
	description varchar(20) NOT NULL,
	CONSTRAINT PK_toy_id PRIMARY KEY (toy_id)
);
INSERT INTO time_of_year(description) VALUES('Winter'),('Spring'),('Summer'),('Fall'),('Anytime');

DROP TABLE IF EXISTS destinations;
CREATE TABLE destinations (
	destination_id SERIAL NOT NULL,
	location_name varchar(100) NOT NULL,
	passport_required boolean NOT NULL,
	expense_id int,
	time_of_year_id int,
	completed boolean NOT NULL DEFAULT FALSE,
	user_id int NOT NULL,
	CONSTRAINT PK_destination PRIMARY KEY (destination_id),
	CONSTRAINT FK_user_dest FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT FK_expense_dest FOREIGN KEY(expense_id) REFERENCES expense(expense_id),
	CONSTRAINT FK_time_of_year_dest FOREIGN KEY(time_of_year_id) REFERENCES time_of_year(toy_id)
);
DROP TABLE IF EXISTS favorited;
CREATE TABLE favorited (
	user_id int NOT NULL,
	destination_id int NOT NULL,
	CONSTRAINT PK_user_dest PRIMARY KEY (user_id,destination_id),
	CONSTRAINT FK_user_fav FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT FK_destination_fav FOREIGN KEY(destination_id) REFERENCES destinations(destination_id)
);
DROP TABLE IF EXISTS vacation_invites;
CREATE TABLE vacation_invites(
	vacation_plan_id int NOT NULL,
	user_id int NOT NULL,
	CONSTRAINT PK_user_vaca PRIMARY KEY (user_id,vacation_plan_id),
	CONSTRAINT FK_user_vaca_inv FOREIGN KEY(user_id) REFERENCES users(user_id)
);
DROP TABLE IF EXISTS vacation_plan;
CREATE TABLE vacation_plan (
	vacation_plan_id SERIAL,
	creator_id int NOT NULL,
	destination_id int NOT NULL,
	completed boolean NOT NULL DEFAULT FALSE,
	CONSTRAINT PK_vacation_plan_id PRIMARY KEY (vacation_plan_id),
	CONSTRAINT FK_creator_id FOREIGN KEY(creator_id) REFERENCES users(user_id),
	CONSTRAINT FK_destination_vaca_plan FOREIGN KEY(destination_id) REFERENCES destinations(destination_id)
);

COMMIT TRANSACTION;
