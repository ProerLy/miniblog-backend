-- 商城数据库初始化脚本
CREATE DATABASE IF NOT EXISTS mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mall;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `gender` TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(255) COMMENT '图标URL',
  `sort` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS `goods` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `subtitle` VARCHAR(500) COMMENT '副标题',
  `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2) COMMENT '原价',
  `main_image` VARCHAR(500) COMMENT '主图',
  `images` TEXT COMMENT '图片列表(JSON)',
  `category_id` BIGINT COMMENT '分类ID',
  `stock` INT DEFAULT 0 COMMENT '库存',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `description` VARCHAR(500) COMMENT '简短描述',
  `detail` TEXT COMMENT '详细描述',
  `is_hot` TINYINT DEFAULT 0 COMMENT '是否热销',
  `is_new` TINYINT DEFAULT 0 COMMENT '是否新品',
  `is_rec` TINYINT DEFAULT 0 COMMENT '是否推荐',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID',
  `goods_name` VARCHAR(200) COMMENT '商品名称(冗余)',
  `goods_image` VARCHAR(500) COMMENT '商品图片(冗余)',
  `price` DECIMAL(10,2) COMMENT '单价(冗余)',
  `quantity` INT DEFAULT 1 COMMENT '数量',
  `sku` VARCHAR(100) COMMENT '规格',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
  `user_id` BIGINT NOT NULL,
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '总金额',
  `status` TINYINT DEFAULT 0 COMMENT '0-待支付 1-已支付 2-待发货 3-已发货 4-已完成 5-已取消',
  `receiver_name` VARCHAR(50) COMMENT '收货人',
  `receiver_phone` VARCHAR(20) COMMENT '联系电话',
  `receiver_address` VARCHAR(255) COMMENT '收货地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 消息表
CREATE TABLE IF NOT EXISTS `message` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `type` TINYINT DEFAULT 1 COMMENT '1-系统通知 2-订单通知 3-优惠活动 4-物流通知',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Banner表
CREATE TABLE IF NOT EXISTS `banner` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) COMMENT '标题',
  `image` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `link` VARCHAR(500) COMMENT '跳转链接',
  `sort` INT DEFAULT 0,
  `type` TINYINT DEFAULT 1 COMMENT '1-首页轮播 2-活动Banner',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化测试数据
INSERT INTO `category` (`name`, `icon`, `sort`) VALUES
('数码电子', '/static/icons/category1.png', 1),
('服饰鞋包', '/static/icons/category2.png', 2),
('美妆护肤', '/static/icons/category3.png', 3),
('食品生鲜', '/static/icons/category4.png', 4),
('家居生活', '/static/icons/category5.png', 5),
('图书音像', '/static/icons/category6.png', 6),
('运动户外', '/static/icons/category7.png', 7),
('母婴玩具', '/static/icons/category8.png', 8);

INSERT INTO `goods` (`name`, `subtitle`, `price`, `original_price`, `main_image`, `category_id`, `stock`, `sales`, `description`, `is_hot`, `is_new`, `is_rec`) VALUES
('iPhone 15 Pro Max 256GB', '全新A17 Pro芯片，钛金属设计', 8999.00, 9999.00, 'https://img.alicdn.com/imgextra/i1/6000000002747/O1CN01ABC123xyz_!!6000000002747.jpg', 1, 500, 2580, '苹果旗舰手机', 1, 1, 1),
('华为 Mate60 Pro', '麒麟9000S芯片，卫星通话', 6999.00, 7999.00, 'https://img.alicdn.com/imgextra/i2/6000000001234/O1CN01DEF456_!!6000000001234.jpg', 1, 300, 1890, '华为旗舰手机', 1, 1, 1),
('小米14 Ultra', '徕卡影像，骁龙8 Gen3', 5999.00, 6499.00, 'https://img.alicdn.com/imgextra/i3/6000000005678/O1CN01GHI789_!!6000000005678.jpg', 1, 200, 960, '小米影像旗舰', 1, 1, 1),
('AirPods Pro 2代', '主动降噪，空间音频', 1799.00, 1999.00, 'https://img.alicdn.com/imgextra/i4/6000000009012/O1CN01JKL012_!!6000000009012.jpg', 1, 800, 3200, '苹果无线耳机', 1, 0, 1),
('MacBook Air M3', '轻薄便携，强劲续航', 9499.00, 10999.00, 'https://img.alicdn.com/imgextra/i5/6000000003456/O1CN01MNO345_!!6000000003456.jpg', 1, 150, 780, '苹果笔记本电脑', 1, 1, 1),
('Nike Air Jordan 1', '经典复古，百搭时尚', 1299.00, 1499.00, 'https://img.alicdn.com/imgextra/i6/6000000007890/O1CN01PQR678_!!6000000007890.jpg', 2, 600, 4500, 'AJ1经典配色', 1, 0, 1),
('Adidas Ultraboost 22', 'Boost中底，跑鞋旗舰', 1399.00, 1599.00, 'https://img.alicdn.com/imgextra/i7/6000000001234/O1CN01STU901_!!6000000001234.jpg', 2, 400, 2100, '阿迪达斯跑鞋', 1, 0, 1),
('SK-II神仙水230ml', '护肤精华，焕亮肌肤', 1599.00, 1999.00, 'https://img.alicdn.com/imgextra/i8/6000000005678/O1CN01VWX234_!!6000000005678.jpg', 3, 200, 1580, 'SK-II经典套装', 1, 1, 1),
('雅诗兰黛小棕瓶50ml', '修护肌肤，夜间精华', 899.00, 1099.00, 'https://img.alicdn.com/imgextra/i9/6000000009012/O1CN01YZA567_!!6000000009012.jpg', 3, 300, 2200, '雅诗兰黛精华', 1, 0, 1),
('阳澄湖大闸蟹礼券', '公4两母3两，8只装', 399.00, 499.00, 'https://img.alicdn.com/imgextra/i1/6000000003456/O1CN01BCD890_!!6000000003456.jpg', 4, 1000, 8900, '正宗阳澄湖大闸蟹', 1, 1, 1),
('戴森吹风机HD15', '智能温控，快速干发', 2799.00, 3199.00, 'https://img.alicdn.com/imgextra/i2/6000000007890/O1CN01EFG123_!!6000000007890.jpg', 5, 150, 980, '戴森旗舰吹风机', 1, 0, 1),
('小米扫地机器人4 Pro', '智能规划，自动集尘', 1999.00, 2499.00, 'https://img.alicdn.com/imgextra/i3/6000000001234/O1CN01HIJ456_!!6000000001234.jpg', 5, 300, 1650, '米家扫地旗舰', 1, 1, 1);

INSERT INTO `banner` (`title`, `image`, `link`, `sort`, `type`) VALUES
('iPhone 15 Pro 新品上市', 'https://img.alicdn.com/imgextra/i1/600000000001/O1CN01123456_!!600000000001.jpg', '/pages/goods/detail?id=1', 1, 1),
('华为 Mate60 限时优惠', 'https://img.alicdn.com/imgextra/i2/600000000002/O1CN01234567_!!600000000002.jpg', '/pages/goods/detail?id=2', 2, 1),
('数码科技节 全场8折', 'https://img.alicdn.com/imgextra/i3/600000000003/O1CN01345678_!!600000000003.jpg', '/pages/goods/search?keyword=数码', 3, 1),
('美妆护肤专场 满300减50', 'https://img.alicdn.com/imgextra/i4/600000000004/O1CN01456789_!!600000000004.jpg', '/pages/goods/search?categoryId=3', 4, 1);

INSERT INTO `message` (`user_id`, `title`, `content`, `type`, `is_read`) VALUES
(1, '欢迎来到商城', '恭喜您注册成功，新用户首单立减50元，快去选购吧！', 1, 0),
(1, '您的订单已发货', '订单号ORD123456789已发货，预计3天内送达，请注意查收。', 2, 0),
(1, '双十一预售开启', '预付定金可抵双倍金额，错过等一年！', 3, 1),
(1, '商品降价提醒', '您关注的iPhone 15 Pro Max已降价200元，快来看看吧！', 1, 0);

INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `phone`, `gender`) VALUES
('admin', 'admin123', '管理员', 'https://img.alicdn.com/imgextra/avatar/2024/default_avatar.png', '13800138000', 1),
('test', 'test123', '测试用户', 'https://img.alicdn.com/imgextra/avatar/2024/default_avatar.png', '13900139000', 2);
