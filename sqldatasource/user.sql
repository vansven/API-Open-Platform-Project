create database if not exists api_project;

use api_project;
-- 用户信息表
create table user_info
(
    id            bigint auto_increment
        primary key,
    user_name     varchar(256)  default ''                not null comment '用户名称',
    user_account  varchar(256)                            not null comment '用户账号',
    user_password varchar(512)                            not null comment '用户密码',
    user_avatar   varchar(1024) default ''                not null comment '用户头像',
    public_key    varchar(512)                           not null comment '公钥',
    private_key   varchar(512)                           not null comment '私钥',
    is_admin      tinyint       default 0                 not null comment '是否为管理员，0-不是（默认），1-是',
    create_time   datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint       default 0                 not null comment '逻辑删除，0-没删除 1-删除'
)
    comment '用户信息表';

create index index_name
    on user_info (user_name);

-- 接口信息表
create table interface_info
(
    id              bigint auto_increment
        primary key,
    userId          bigint                                 not null comment '创建人',
    name            varchar(256)                           not null comment '接口名称',
    description     varchar(256) default ''                not null comment '接口描述',
    url             varchar(512)                           not null comment '接口地址',
    method          varchar(256)                           not null comment '请求方法',
    request_params  text                                   null comment '请求参数',
    response_params text                                   null comment '响应参数',
    status          tinyint      default 2                 not null comment '接口状态 0-关闭 1-开启 2-开发中',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       tinyint      default 0                 not null comment '逻辑删除 0- 没有 1-删除',
)
    comment '接口信息表';

create index index_name
    on interface_info (name);

