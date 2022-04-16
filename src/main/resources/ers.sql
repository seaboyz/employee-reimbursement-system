DROP TABLE ERS_USERS;
DROP TABLE ERS_USER_ROLES;
DROP TABLE ERS_REIMBURSEMENT_TYPE;
DROP TABLE ERS_REIMBURSEMENT_STATUS;


-- ERS_USER_ROLES 
CREATE TABLE ERS_USER_ROLES (
  ERS_USER_ROLE_ID SERIAL PRIMARY KEY,
  USER_ROLE VARCHAR(10) UNIQUE NOT NULL
);

INSERT INTO ERS_USER_ROLES(USER_ROLE) 
VALUES ('EMPLOYEE');
INSERT INTO ERS_USER_ROLES(USER_ROLE) 
VALUES('MANAGER');
SELECT * FROM ERS_USER_ROLES;

-- ERS_REIMBURSEMENT_STATUS
CREATE TABLE ERS_REIMBURSEMENT_STATUS (
  REIMB_STATUS_ID SERIAL PRIMARY KEY,
  REIMB_STATUS VARCHAR(10)
);

INSERT INTO ERS_REIMBURSEMENT_STATUS(REIMB_STATUS)
VALUES ('PENDING');
INSERT INTO ERS_REIMBURSEMENT_STATUS(REIMB_STATUS)
VALUES ('APPROVED');
INSERT INTO ERS_REIMBURSEMENT_STATUS(REIMB_STATUS)
VALUES ('DENIED');
SELECT * FROM ERS_REIMBURSEMENT_STATUS;

-- ERS_REIMBURSEMENT_TYPE
CREATE TABLE ERS_REIMBURSEMENT_TYPE (
  REIMB_TYPE_ID SERIAL PRIMARY KEY,
  REIMB_TYPE VARCHAR(10)
);
INSERT INTO ERS_REIMBURSEMENT_TYPE(REIMB_TYPE)
VALUES ('TRAVEL');
INSERT INTO ERS_REIMBURSEMENT_TYPE(REIMB_TYPE)
VALUES ('TRAINING');
INSERT INTO ERS_REIMBURSEMENT_TYPE(REIMB_TYPE)
VALUES ('BUSINESS');
SELECT * FROM ERS_REIMBURSEMENT_TYPE;

-- users
CREATE TABLE ERS_USERS (
   ERS_USER_ID SERIAL PRIMARY KEY,
   ERS_USER_NAME VARCHAR(50) UNIQUE NOT NULL,
   ERS_EMAIL VARCHAR(150) NOT NULL,
   ERS_PASSWORD VARCHAR(50) NOT NULL,
   ERS_FIRST_NAME VARCHAR(100) NOT NULL,
   ERS_LAST_NAME VARCHAR(100) NOT NULL,
   USER_ROLE_ID INTEGER REFERENCES ERS_USER_ROLES(ERS_USER_ROLE_ID) DEFAULT 1
);

INSERT INTO 
ERS_USERS(ERS_USER_NAME,ERS_EMAIL,ERS_PASSWORD,ERS_FIRST_NAME,ERS_LAST_NAME)
VALUES 
('test','test@test.com','123456','john','doe');

INSERT INTO ERS_USERS
(ERS_USER_NAME,ERS_PASSWORD,ERS_EMAIL,ERS_FIRST_NAME,ERS_LAST_NAME)
VALUES 
('ccatto0', 'GT7U95423R', 'ccatto0@dion.ne.jp', 'Caitlin', 'Catto');

SELECT * FROM ERS_USERS; 

DELETE FROM ERS_USERS WHERE ERS_USER_ID = 20;

-- update
UPDATE ERS_USERS 
SET 
ERS_USER_NAME = 'test1', 
ERS_EMAIL = 'test1@test.com',
ERS_PASSWORD = '654321',
ERS_FIRST_NAME = 'jane',
ERS_LAST_NAME = 'doe'
WHERE 
ERS_USER_ID = 23;

-- reimbursement
DROP TABLE ERS_REIMBURSEMENT;
CREATE TABLE ERS_REIMBURSEMENT(
  REIMB_ID SERIAL PRIMARY KEY,
  REIMB_AMOUNT NUMERIC NOT NULL CHECK (REIMB_AMOUNT > 0),
  REIMB_SUBMITTED TIMESTAMP DEFAULT NOW(),
  REIMB_RESOLVED TIMESTAMP,
  REIMB_DESCRIPTION VARCHAR(250),
  REIMB_RECEIPT BYTEA,
  REIMB_AUTHOR INTEGER REFERENCES ERS_USERS(ERS_USER_ID) NOT NULL,
  REIMB_RESOLVER INTEGER REFERENCES ERS_USERS(ERS_USER_ID),
  REIMB_STATUS_ID INTEGER REFERENCES ERS_REIMBURSEMENT_STATUS(REIMB_STATUS_ID) DEFAULT 1,
  REIMB_TYPE_ID INTEGER REFERENCES ERS_REIMBURSEMENT_TYPE(REIMB_TYPE_ID) NOT NULL
);
INSERT INTO ERS_REIMBURSEMENT
(REIMB_AMOUNT,REIMB_AUTHOR, REIMB_TYPE_ID)
VALUES
(100, 1, 1);
SELECT * FROM ERS_REIMBURSEMENT;

-- adminministration
-- CREATE USER seaboyz WITH PASSWORD '123456';
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA FROM seaboyz;
-- SELECT * FROM information_schema. table_privileges LIMIT 5


