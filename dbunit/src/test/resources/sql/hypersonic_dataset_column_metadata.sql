CREATE TABLE A
  (PK_A INTEGER,
   DEC_COL_A2 DECIMAL(9, 2),
   CHAR_COL_A3 CHAR(30),
   DATE_COL_A4 DATE,
   PRIMARY KEY (PK_A));

INSERT INTO A VALUES(101, 155.33, 'Somebody Else', '2017-06-30');