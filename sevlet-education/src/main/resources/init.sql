create table `oacy_zephyr`.`t_user` (
	`id` int(10) primary key auto_increment comment '用户编号',
    `uname` varchar(20) comment '用户名',
    `pwd` varchar(20) comment '用户密码',
    `phone` varchar(20) comment '手机号码',
    UNIQUE INDEX `uname_idx`(uname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;