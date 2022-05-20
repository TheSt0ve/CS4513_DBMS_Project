-- Query 1
INSERT INTO Customer
VALUES
 ('Parker', '324 NW Pen St', 5);


-- Query 2
INSERT INTO Department
VALUES
 (3, 'The third department', NULL);


-- Query 3
INSERT INTO Process
VALUES
 (16, 'very', NULL, 2, NULL);

INSERT INTO Fit_Process
VALUES
 (16, 'the best fit type');


-- Query 4
INSERT INTO Assembly_Obj
VALUES
 (138, '11/21/2021', 'description of assembly', 'Parker', NULL);

UPDATE Process
SET assembly_id = 138
WHERE process_id = 16;


-- Query 5
INSERT INTO Account
VALUES
 (4);

INSERT INTO Process_Acc
VALUES
 (4, 10000);

UPDATE Process
SET account_no = 3
WHERE process_id = 16;


-- Query 6
INSERT INTO Job
VALUES
  (7, '11/20/2021', NULL, 16);

UPDATE Process
SET assembly_id = 138
WHERE process_id = 16;


-- Query 7
UPDATE Job
SET date_completed = '11/21/2021'
WHERE job_no = 7;

INSERT INTO Fit_Job
VALUES
 (7, 55);


-- Query 8
INSERT INTO Transaction_Obj
VALUES
 (205, 120, NULL, NULL, NULL, 7);

UPDATE Process_Acc
SET details_3 = details_3 + 
 (SELECT sup_cost
 FROM Transaction_Obj
 WHERE transaction_no = 205)
WHERE account_no = 3;


-- Query 9
SELECT SUM(ISNULL(S.sup_cost, 0))
FROM
    (
    SELECT sup_cost
    FROM Transaction_Obj
    WHERE 
        job_no IN(
            SELECT job_no
            FROM Job
            WHERE process_id IN(
                SELECT process_id
                FROM Process
                WHERE
                    assembly_id = 138)
        )
    ) AS S;


-- Query 10
SELECT SUM(S.labor_time)
FROM
    (
    SELECT job_no, labor_time FROM Fit_Job
    UNION
    SELECT job_no, labor_time FROM Paint_Job
    UNION
    SELECT job_no, labor_time FROM Cut_Job
    ) AS S
WHERE S.job_no IN(
    SELECT job_no
    FROM Job
    WHERE 
        date_completed = '11/21/2021' AND process_id IN(
            SELECT process_id
            FROM Process
            WHERE department_no = 2
    )
);


-- Query 11
-- DOESN'T QUITE NOT WORK AS INTENDED
-- Does not sort by date, it sorts by process_id
SELECT process_id, department_no
FROM Process
WHERE assembly_id = 65;


-- Query 12
SELECT job_no, labor_time
FROM Fit_Job AS F
WHERE F.job_no IN(
    SELECT job_no
    FROM Job
    WHERE 
        date_completed = '11/21/2021' AND process_id IN(
            SELECT process_id
            FROM Process
            WHERE department_no = 2
    )
);

SELECT job_no, labor_time, color, volume
FROM Paint_Job AS P
WHERE P.job_no IN(
    SELECT job_no
    FROM Job
    WHERE 
        date_completed = '11/21/2021' AND process_id IN(
            SELECT process_id
            FROM Process
            WHERE department_no = 2
    )
);

SELECT job_no, labor_time, machine_type, time_used, material_used
FROM Cut_Job AS C
WHERE C.job_no IN(
    SELECT job_no
    FROM Job
    WHERE 
        date_completed = '11/21/2021' AND process_id IN(
            SELECT process_id
            FROM Process
            WHERE department_no = 2
    )
);


-- Query 13
SELECT *
FROM Customer
WHERE category BETWEEN 4 AND 6;


-- Query 14
DROP TABLE IF EXISTS temp_cut_jobs;

SELECT *
INTO temp_cut_jobs
FROM Cut_Job;

DELETE FROM Cut_Job
WHERE job_no BETWEEN 4 AND 7;

DELETE FROM Transaction_Obj
WHERE job_no IN(
    SELECT job_no
    FROM temp_cut_jobs
    WHERE job_no BETWEEN 4 AND 7
);

DELETE FROM Job
WHERE job_no IN(
    SELECT job_no
    FROM temp_cut_jobs
    WHERE job_no BETWEEN 4 AND 7
);


-- Query 15
UPDATE Paint_Job
SET color = 'color: purple'
WHERE job_no = 2;