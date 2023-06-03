/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : yanglaofuwu_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-07-11 23:56:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_health`
-- ----------------------------
DROP TABLE IF EXISTS `t_health`;
CREATE TABLE `t_health` (
  `healthId` int(11) NOT NULL auto_increment COMMENT '健康id',
  `userObj` varchar(30) NOT NULL COMMENT '老人',
  `xinlv` varchar(20) NOT NULL COMMENT '心率',
  `xueya` varchar(20) NOT NULL COMMENT '血压',
  `hxpl` varchar(40) NOT NULL COMMENT '呼吸频率',
  `tiwen` varchar(20) NOT NULL COMMENT '体温',
  `tizhong` varchar(20) NOT NULL COMMENT '体重',
  `healthDesc` varchar(8000) NOT NULL COMMENT '健康详述',
  `testDate` varchar(20) default NULL COMMENT '测试日期',
  PRIMARY KEY  (`healthId`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_health_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_health
-- ----------------------------
INSERT INTO `t_health` VALUES ('1', 'user1', '110', '80/120', '78次/分钟', '37度', '56千克', '<p>老人有心脏病，需要多休息！</p>', '2018-03-29');
INSERT INTO `t_health` VALUES ('2', 'user2', '115', '82/118', '75次/分钟', '37.3度', '52公斤', '<p>老人腿脚不方便！</p>', '2018-04-02');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(30) NOT NULL COMMENT 'user_name',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '老人姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '老人照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '联系邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `xqah` varchar(800) NOT NULL COMMENT '兴趣爱好',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('user1', '123', '王爷爷', '男', '2018-03-06', 'upload/a466431d-fa2f-4067-ac66-1bc807af13f9.jpg', '13580830834', 'zhongqiang@163.com', '四川成都红星路13号', '老人年龄高了还有心脏病', '2018-03-29 10:58:08');
INSERT INTO `t_userinfo` VALUES ('user2', '123', '王奶奶', '女', '1944-06-08', 'upload/0046e3fb-f96c-4793-99d3-1bcc797ab4ce.jpg', '13088308993', 'wangjia@163.com', '四川省德阳市阳光路10号', '喜欢唱歌和跳舞', '2018-04-02 15:58:07');

-- ----------------------------
-- Table structure for `t_worker`
-- ----------------------------
DROP TABLE IF EXISTS `t_worker`;
CREATE TABLE `t_worker` (
  `workUserName` varchar(30) NOT NULL COMMENT 'workUserName',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `zzxx` varchar(20) NOT NULL COMMENT '资质信息',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '联系邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `workerDesc` varchar(8000) NOT NULL COMMENT '医疗者简介',
  PRIMARY KEY  (`workUserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_worker
-- ----------------------------
INSERT INTO `t_worker` VALUES ('worker1', '123', '张医护', '男', '2018-03-05', '护理医师', '13980830834', 'zhangyihu@163.com', '四川达州', '<p>拥有15年医护经验！</p>');
INSERT INTO `t_worker` VALUES ('worker2', '123', '罗医护', '女', '2018-04-04', '护理医生', '13083080834', 'luoyih@163.com', '四川成都建设路', '<p>工作认真负责，对病人热情</p>');

-- ----------------------------
-- Table structure for `t_zhenduan`
-- ----------------------------
DROP TABLE IF EXISTS `t_zhenduan`;
CREATE TABLE `t_zhenduan` (
  `zhenduanId` int(11) NOT NULL auto_increment COMMENT '诊断id',
  `workerObj` varchar(30) NOT NULL COMMENT '医疗者',
  `zhenduanDate` varchar(20) default NULL COMMENT '诊断日期',
  `zhenduanTime` varchar(20) NOT NULL COMMENT '上门时间',
  `userObj` varchar(30) NOT NULL COMMENT '诊断老人',
  `zhenduanState` varchar(20) NOT NULL COMMENT '诊断状态',
  `zhenduanResult` varchar(8000) NOT NULL COMMENT '诊断结果',
  PRIMARY KEY  (`zhenduanId`),
  KEY `workerObj` (`workerObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_zhenduan_ibfk_1` FOREIGN KEY (`workerObj`) REFERENCES `t_worker` (`workUserName`),
  CONSTRAINT `t_zhenduan_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_zhenduan
-- ----------------------------
INSERT INTO `t_zhenduan` VALUES ('1', 'worker1', '2018-03-29', '13:00', 'user1', '诊断完毕', '<p>没得大问题</p>');
INSERT INTO `t_zhenduan` VALUES ('2', 'worker1', '2018-04-05', '13:00', 'user1', '待诊断', '<p>---</p>');
INSERT INTO `t_zhenduan` VALUES ('3', 'worker2', '2018-04-06', '下午2点到4点', 'user2', '诊断完毕', '<p>请罗医护去给王奶奶诊断下！<br/></p><p>已经诊断完毕！</p><p><br/></p><p>结论：王奶奶有风湿关节炎，腿脚不方便</p>');

-- ----------------------------
-- Table structure for `t_zhiban`
-- ----------------------------
DROP TABLE IF EXISTS `t_zhiban`;
CREATE TABLE `t_zhiban` (
  `zhibanId` int(11) NOT NULL auto_increment COMMENT '值班id',
  `workerObj` varchar(30) NOT NULL COMMENT '值班人',
  `zhibanDate` varchar(20) default NULL COMMENT '值班日期',
  `zhibanTime` varchar(20) NOT NULL COMMENT '值班时间',
  `zhibanMemo` varchar(800) NOT NULL COMMENT '值班备注',
  PRIMARY KEY  (`zhibanId`),
  KEY `workerObj` (`workerObj`),
  CONSTRAINT `t_zhiban_ibfk_1` FOREIGN KEY (`workerObj`) REFERENCES `t_worker` (`workUserName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_zhiban
-- ----------------------------
INSERT INTO `t_zhiban` VALUES ('1', 'worker1', '2018-03-28', '6:00-14:00', '测试');
INSERT INTO `t_zhiban` VALUES ('2', 'worker2', '2018-04-02', '16:00-23:59', '请认真上班');

-- ----------------------------
-- Table structure for `t_zyz`
-- ----------------------------
DROP TABLE IF EXISTS `t_zyz`;
CREATE TABLE `t_zyz` (
  `zyzId` int(11) NOT NULL auto_increment COMMENT '志愿者id',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '志愿者照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `grda` varchar(8000) NOT NULL COMMENT '个人档案',
  `fwxm` varchar(8000) NOT NULL COMMENT '服务项目',
  PRIMARY KEY  (`zyzId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_zyz
-- ----------------------------
INSERT INTO `t_zyz` VALUES ('1', '王明霞', '女', '2018-03-14', 'upload/06a6db73-bec9-452a-8ace-55fecd29797f.jpg', '13530808023', '四川南充市区', '<p>周末志愿服务老人的哦！</p>', '<p>给老人捶背，给老人洗脚！</p>');
INSERT INTO `t_zyz` VALUES ('2', '张小凤', '女', '2018-04-01', 'upload/1a513611-7963-46db-9f24-62f159ac2e67.jpg', '13890093423', '四川省资阳市乐至县', '<p>很亲近的一个志愿者！</p>', '<p>给老人讲故事，给老人唱歌跳舞</p>');
