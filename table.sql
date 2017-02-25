drop table if exists `tbl_goods`;
CREATE TABLE `tbl_goods` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` text COMMENT '商品名称',
  `price` mediumint unsigned DEFAULT 0 COMMENT '商品价格',
  `stock` mediumint unsigned DEFAULT 0 COMMENT '库存数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '商品表';

drop table if exists `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(10) COMMENT '用户账号',
  `password` varchar(10) COMMENT '用户密码',
  `phone` varchar(11) COMMENT '手机号',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`),
  UNIQUE KEY (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '用户表';

drop table if exists `tbl_order`;
CREATE TABLE `tbl_order` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `userId` int unsigned NOT NULL,
  `goodsId` int unsigned NOT NULL,
  `number` mediumint unsigned COMMENT '购买数量',
  `time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  PRIMARY KEY (`id`),
  KEY (`userId`),
  KEY (`goodsId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '订单表';