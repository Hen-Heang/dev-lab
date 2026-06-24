-- H2 runs in PostgreSQL-compat mode (see application.yml), so this SQL stays
-- close to the real saas-olv PostgreSQL DDL: sequences, TEXT, nextval(), NOW().

DROP TABLE IF EXISTS board;
DROP SEQUENCE IF EXISTS seq_board;

CREATE SEQUENCE seq_board START WITH 1 INCREMENT BY 1;

CREATE TABLE board (
    board_sn    BIGINT       DEFAULT nextval('seq_board') PRIMARY KEY,
    board_title VARCHAR(200) NOT NULL,
    board_cn    TEXT,
    use_yn      CHAR(1)      DEFAULT 'Y',
    data_reg_id VARCHAR(20),
    data_reg_dt TIMESTAMP    DEFAULT NOW(),
    data_chg_id VARCHAR(20),
    data_chg_dt TIMESTAMP
);
