drop table if exists t01_wx_access_token;

CREATE TABLE t01_wx_access_token
(
  access_token        varchar(60)     COMMENT 'token',
  ver_no              varchar(20)     COMMENT '版本号',
  update_time         datetime(3) default  CURRENT_TIMESTAMP(3) on update CURRENT_TIMESTAMP(3) COMMENT '时间',
  create_time         datetime(3) default  CURRENT_TIMESTAMP(3)  COMMENT '时间' primary key
);

alter table t01_wx_access_token comment 'token';

insert into t01_wx_access_token (access_token, ver_no) values ('myTestToken', '1.0');
insert into t01_wx_access_token (access_token, ver_no) values ('myTestToken_local', '1.0');
insert into t01_wx_access_token (access_token, ver_no) values ('myTestToken_dev', '1.0');


--ALTER TABLE t01_wx_access_token MODIFY COLUMN CREATE_TM datetime(3) default  CURRENT_TIMESTAMP(3) on update CURRENT_TIMESTAMP(3) COMMENT '时间';
