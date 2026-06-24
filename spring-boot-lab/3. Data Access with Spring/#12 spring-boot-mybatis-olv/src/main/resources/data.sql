-- seed rows (board_sn auto-filled by the sequence default)
INSERT INTO board (board_title, board_cn, use_yn, data_reg_id) VALUES ('Welcome to the board', 'first post body', 'Y', 'admin');
INSERT INTO board (board_title, board_cn, use_yn, data_reg_id) VALUES ('MyBatis paging notice', 'how LIMIT/OFFSET works', 'Y', 'admin');
INSERT INTO board (board_title, board_cn, use_yn, data_reg_id) VALUES ('Spring Boot tips',       'config and beans',     'Y', 'user');
INSERT INTO board (board_title, board_cn, use_yn, data_reg_id) VALUES ('Hidden draft',           'should not appear',    'N', 'user');
INSERT INTO board (board_title, board_cn, use_yn, data_reg_id) VALUES ('Another notice',         'keyword search target','Y', 'admin');
