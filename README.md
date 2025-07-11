# MAJOR CHANGES WILL UPDATE UPLOAD SRC

- ### Creating the SQL Database
```CREATE DATABASE quizprodb;```

- ### Create the SQL Table
```
CREATE TABLE myusers (
	userId VARCHAR(50),
	username VARCHAR(50),
	email VARCHAR(50),
	password VARCHAR(50),
	otp VARCHAR(10), 
	city VARCHAR(20)
);
```

- ### Inserting a Record
```
INSERT INTO myusers (user_id, username, email, password, city) VALUES ('101', 'pritish-tripathy', 'myexample@mailinator.com', 'Ppt@123', 'bengaluru');
```
