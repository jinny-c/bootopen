drop table if exists t01_wx_access_token;

CREATE TABLE t01_wx_access_token
(
  ACCESS_TOKEN      varchar(60)     COMMENT 'token',
  VER               varchar(20)     COMMENT 'ver',
  CREATE_TM         datetime(3) default  CURRENT_TIMESTAMP(3) on update CURRENT_TIMESTAMP(3) COMMENT '修改时间';
);

alter table t01_wx_access_token comment 'token';
