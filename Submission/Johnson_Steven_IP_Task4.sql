-- While working on the database design, it's useful to start from scratch every time
-- Hence, we drop tables in reverse order they are created (so the foreign key constraints are not violated)
DROP TABLE IF EXISTS Transaction_Obj
DROP TABLE IF EXISTS Cut_Job
DROP TABLE IF EXISTS Paint_Job
DROP TABLE IF EXISTS Fit_Job
DROP TABLE IF EXISTS Job
DROP TABLE IF EXISTS Cut_Process
DROP TABLE IF EXISTS Paint_Process
DROP TABLE IF EXISTS Fit_Process
DROP TABLE IF EXISTS Process
DROP TABLE IF EXISTS Department
DROP TABLE IF EXISTS Assembly_Obj;
DROP TABLE IF EXISTS Process_Acc
DROP TABLE IF EXISTS Department_Acc
DROP TABLE IF EXISTS Assembly_Acc
DROP TABLE IF EXISTS Account
DROP TABLE IF EXISTS Customer;


-- Create tables
CREATE TABLE Customer (
 name VARCHAR(64) PRIMARY KEY,
 address VARCHAR(64),
 category INT CHECK (category >= 1 AND category <= 10),
);

CREATE TABLE Account (
 account_no INT PRIMARY KEY,
); 

CREATE TABLE Assembly_Acc (
 account_no INT PRIMARY KEY,
 details_1 INT,
 CONSTRAINT FK_assembly_account_no FOREIGN KEY (account_no) REFERENCES Account,
); 

CREATE TABLE Department_Acc (
 account_no INT PRIMARY KEY,
 details_2 INT,
 CONSTRAINT FK_dept_account_no FOREIGN KEY (account_no) REFERENCES Account,
); 

CREATE TABLE Process_Acc (
 account_no INT PRIMARY KEY,
 details_3 INT,
 CONSTRAINT FK_proc_account_no FOREIGN KEY (account_no) REFERENCES Account,
); 

CREATE TABLE Assembly_Obj (
 assembly_id INT PRIMARY KEY,
 date_ordered VARCHAR(10),
 assembly_details VARCHAR(64),
 name VARCHAR(64),
 account_no INT,
 CONSTRAINT FK_customer_name_on_order FOREIGN KEY (name) REFERENCES Customer,
 CONSTRAINT FK_assembly_account_no_to_use FOREIGN KEY (account_no) REFERENCES Account,
);

CREATE TABLE Department (
 department_no INT PRIMARY KEY,
 department_data VARCHAR(64),
 account_no INT,
 CONSTRAINT FK_dept_account_no_to_use FOREIGN KEY (account_no) REFERENCES Account,
); 

CREATE TABLE Process (
 process_id INT PRIMARY KEY,
 process_data VARCHAR(64),
 assembly_id INT,
 department_no INT,
 account_no INT,
 CONSTRAINT FK_manufactured_assembly_id FOREIGN KEY (assembly_id) REFERENCES Assembly_Obj,
 CONSTRAINT FK_proc_supervisor_dept_no FOREIGN KEY (department_no) REFERENCES Department,
 CONSTRAINT FK_proc_account_no_to_use FOREIGN KEY (account_no) REFERENCES Account,
); 

CREATE TABLE Fit_Process (
 process_id INT PRIMARY KEY,
 fit_type VARCHAR(64),
 CONSTRAINT FK_fit_proc_id FOREIGN KEY (process_id) REFERENCES Process,
); 

CREATE TABLE Paint_Process (
 process_id INT PRIMARY KEY,
 paint_type VARCHAR(64),
 paint_method VARCHAR(64),
 CONSTRAINT FK_paint_proc_id FOREIGN KEY (process_id) REFERENCES Process,
); 

CREATE TABLE Cut_Process (
 process_id INT PRIMARY KEY,
 cutting_type VARCHAR(64),
 machine_type VARCHAR(64),
 CONSTRAINT FK_cut_proc_id FOREIGN KEY (process_id) REFERENCES Process,
); 

CREATE TABLE Job (
 job_no INT PRIMARY KEY,
 date_commenced VARCHAR(10),
 date_completed VARCHAR(10),
 process_id INT,
 CONSTRAINT FK_proc_id_for_parent_of_job FOREIGN KEY (process_id) REFERENCES Process,
); 

CREATE TABLE Fit_Job (
 job_no INT PRIMARY KEY,
 labor_time INT,
 CONSTRAINT FK_fit_job_no FOREIGN KEY (job_no) REFERENCES Job,
); 

CREATE TABLE Paint_Job (
 job_no INT PRIMARY KEY,
 color VARCHAR(64),
 volume INT,
 labor_time INT,
 CONSTRAINT FK_paint_job_no FOREIGN KEY (job_no) REFERENCES Job,
); 

CREATE TABLE Cut_Job (
 job_no INT PRIMARY KEY,
 machine_type VARCHAR(64),
 time_used INT,
 material_used VARCHAR(64),
 labor_time INT,
 CONSTRAINT FK_cut_job_no FOREIGN KEY (job_no) REFERENCES Job,
); 

CREATE TABLE Transaction_Obj (
 transaction_no INT,
 sup_cost INT,
 assembly_id INT,
 process_id INT,
 department_no INT,
 job_no INT,
 CONSTRAINT FK_expenditures_from_assembly_id FOREIGN KEY (assembly_id) REFERENCES Assembly_Obj,
 CONSTRAINT FK_expenditures_from_proc_id FOREIGN KEY (process_id) REFERENCES Process,
 CONSTRAINT FK_expenditures_from_dept_no FOREIGN KEY (department_no) REFERENCES Department,
 CONSTRAINT FK_expenditures_from_job_no FOREIGN KEY (job_no) REFERENCES Job,
); 





-- Create sample data for each table
INSERT INTO Customer
VALUES
 ('Jackson', '489 Stinson St', 5),
 ('Taylor', '331 NW 35th St', 3);

INSERT INTO Account
VALUES
 (1),
 (2),
 (3);

INSERT INTO Assembly_Acc
VALUES
 (1, 50);

INSERT INTO Department_Acc
VALUES
 (2, 100000);

INSERT INTO Process_Acc
VALUES
 (3, 150);

INSERT INTO Assembly_Obj
VALUES
 (100, '11/19/2021', 'do this and that or something like that', 'Jackson', 1),
 (65, '11/20/2021', 'the best assembly around', 'Taylor', 1);

INSERT INTO Department
VALUES
 (1, 'The first department', 2),
 (2, 'The second department', 2);

INSERT INTO Process
VALUES
 (99, 'processes', 100, 1, 3),
 (10, 'are', 65, 2, 3),
 (26, 'cool', 65, 2, 3);

INSERT INTO Fit_Process
VALUES
 (99, 'fit-type');

INSERT INTO Paint_Process
VALUES
 (10, 'paint-type', 'paint-method');

INSERT INTO Cut_Process
VALUES
 (26, 'cutting-type', 'machine-type');

INSERT INTO Job
VALUES
 (1, '11/16/2021', '11/21/2021', 99),
 (2, '11/17/2021', '11/21/2021', 10),
 (3, '11/18/2021', '11/21/2021', 26),
 (4, '11/19/2021', NULL, 26),
 (5, '11/20/2021', NULL, 10),
 (6, '11/21/2021', NULL, 99);

INSERT INTO Fit_Job
VALUES
 (1, 55),
 (6, 45);

INSERT INTO Paint_Job
VALUES
 (2, 'color: red', 50, 135),
 (5, 'color: blue', 50, 160);

INSERT INTO Cut_Job
VALUES
 (3, 'machine-type', 50, 'material used', 20),
 (4, 'machine-type', 40, 'material used', 30);

INSERT INTO Transaction_Obj
VALUES
 (201, 50, NULL, NULL, NULL, 1), 
 (202, 35, NULL, NULL, NULL, 2),
 (203, 25, NULL, NULL, NULL, 3),
 (204, 1300, NULL, NULL, 1, NULL);