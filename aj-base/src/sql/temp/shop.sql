-- Dumping structure for table shop.delivery_address
CREATE TABLE IF NOT EXISTS `delivery_address` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '收货地址表id',
  `userId` int(20) DEFAULT NULL COMMENT '用户表id',
  `realname` varchar(50) DEFAULT NULL COMMENT '收件人姓名',
  `telphone` int(20) DEFAULT NULL COMMENT '联系电话',
  `telphone2` int(20) DEFAULT NULL COMMENT '备用联系电话',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `area` varchar(50) DEFAULT NULL COMMENT '地区',
  `street` varchar(50) DEFAULT NULL COMMENT '街道/详细收货地址',
  `zip` int(20) DEFAULT NULL COMMENT '邮政编码',
  `isDefaultAddress` tinyint(4) DEFAULT NULL COMMENT '是否默认收货地址',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收货地址';

-- Dumping structure for table shop.order
CREATE TABLE IF NOT EXISTS `order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单表主键id',
  `orderNo` int(20) DEFAULT NULL COMMENT '唯一值,供客户查询',
  `shopId` int(20) DEFAULT NULL COMMENT '商店表id',
  `orderStatus` tinyint(4) DEFAULT NULL COMMENT '订单状态：未付款,已付款,已发货,已签收,退货申请,退货中,已退货,取消交易',
  `productCount` int(20) DEFAULT NULL COMMENT '商品项目数量,不是商品',
  `productAmountTotal` int(20) DEFAULT NULL COMMENT '商品总价',
  `orderAmountTotal` int(20) DEFAULT NULL COMMENT '订单金额,实际付款金额',
  `logisticsFee` int(20) DEFAULT NULL COMMENT '运费金额',
  `isUnpackingInspection` tinyint(4) DEFAULT NULL COMMENT '是否开箱验货',
  `invoiceOrNot` tinyint(4) DEFAULT NULL COMMENT '是否开具发票',
  `invoiceNo` int(20) DEFAULT NULL COMMENT '订单发票表自id',
  `addressId` int(20) DEFAULT NULL COMMENT '收货地址id',
  `orderlogisticsId` int(20) DEFAULT NULL COMMENT '订单物流表id',
  `payChannel` varchar(50) DEFAULT NULL COMMENT '订单支付渠道',
  `outTradeNo/escrowTrade_no` tinyint(4) DEFAULT NULL COMMENT '订单支付单号,第三方支付流水号',
  `orderTime` datetime DEFAULT NULL COMMENT '下单时间',
  `paymentTime` datetime DEFAULT NULL COMMENT '付款时间',
  `deliveryTime` datetime DEFAULT NULL COMMENT '发货时间',
  `userId` int(20) DEFAULT NULL COMMENT '客户用户表id',
  `customerComments` varchar(50) DEFAULT NULL COMMENT '客户备注',
  `orderSettlementStatus` tinyint(4) DEFAULT NULL COMMENT '订单结算状态,货到付款、分期付款等',
  `orderSettlementTime` datetime DEFAULT NULL COMMENT '订单结算时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单';

-- Dumping data for table shop.order: ~0 rows (approximately)
-- Dumping structure for table shop.order_auditbiz
CREATE TABLE IF NOT EXISTS `order_auditbiz` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单业务审核流程表id',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `orderStatus` tinyint(4) DEFAULT NULL COMMENT '订单状态,0:未审核或发起交易;1:交易完成;20:核单通过;24:核单失败;30:已发货;未签收;34:仓库退回;40:座席取消;41:买家取消;42:逾期取消;43:订单无效取消;50:客户签收;54:客户拒签;55:客户退货',
  `isSellerRiskConfirm` tinyint(4) DEFAULT NULL COMMENT '销售员直接确认订单,不需要订单审核员确认，直接强制审核通过，如客户退货则销售员必须承担退货运费',
  `isSellerPunish` tinyint(4) DEFAULT NULL COMMENT '订单退货,销售员是否承担运费',
  `isSellerCommission` varchar(50) DEFAULT NULL COMMENT '销售员是否提成',
  `sellerCommissionRate` varchar(50) DEFAULT NULL COMMENT '销售员提成比例,无提成则填0',
  `sellerCommissionAmount` int(20) DEFAULT NULL COMMENT '销售员提成金额',
  `sellerRemark` varchar(50) DEFAULT NULL COMMENT '销售员订单备注,给订单审核员看的备注',
  `confirmerRemark` varchar(50) DEFAULT NULL COMMENT '订单审核员订单备注,给仓管看的备注',
  `storekeeperReturnbackRemark` varchar(50) DEFAULT NULL COMMENT '仓管备注,仓管退给订单审核员看的备注',
  `cashierRemark` varchar(50) DEFAULT NULL COMMENT '财务备注,财务给销售员看的备注',
  `sellerUid` int(20) DEFAULT NULL COMMENT '销售员用户id',
  `auditorUid` int(20) DEFAULT NULL COMMENT '订单审核员用户id',
  `cashierUid` int(20) DEFAULT NULL COMMENT '收款人用户编号id,收款人不一定是财务',
  `accountantUid` int(20) DEFAULT NULL COMMENT '财务用户id',
  `orderSource` varchar(50) DEFAULT NULL COMMENT '订单来源,销售下单,内部购买',
  `auditorAuditedTime` datetime DEFAULT NULL COMMENT '订单审核员审核时间',
  `storekeeperAuditedTime` datetime DEFAULT NULL COMMENT '仓管员审核时间',
  `accountantAuditedTime` datetime DEFAULT NULL COMMENT '财务审核时间',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单业务审核流程';

-- Dumping data for table shop.order_auditbiz: ~0 rows (approximately)
-- Dumping structure for table shop.order_commission
CREATE TABLE IF NOT EXISTS `order_commission` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单提成表id',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `sellerUid` int(20) DEFAULT NULL COMMENT '销售员用户id',
  `amount` int(20) DEFAULT NULL COMMENT '提成金额',
  `stat` tinyint(4) DEFAULT NULL COMMENT '结算状态',
  `settlementTime` datetime DEFAULT NULL COMMENT '结算时间',
  `cashierUid` int(20) DEFAULT NULL COMMENT '财务人员用户id',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单提成';

-- Dumping data for table shop.order_commission: ~0 rows (approximately)
-- Dumping structure for table shop.order_detail
CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单商品详情表id',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `productId` int(20) DEFAULT NULL COMMENT '商品表id',
  `productName` varchar(50) DEFAULT NULL COMMENT '商品名称,商品可能删除,所以这里要记录，不能直接读商品表',
  `productPrice` int(20) DEFAULT NULL COMMENT '商品价格,商品可能删除,所以这里要记录',
  `productMarque` varchar(50) DEFAULT NULL COMMENT '商品型号,前台展示给客户',
  `productStoreBarcode` varchar(50) DEFAULT NULL COMMENT '商品条码,商品仓库条码',
  `productModeDesc` varchar(50) DEFAULT NULL COMMENT '商品型号信息,记录详细商品型号，如颜色、规格、包装等',
  `productModeParams` varchar(100) DEFAULT NULL COMMENT '商品型号参数,JSON格式，记录单位编号、颜色编号、规格编号等',
  `discountRate` varchar(50) DEFAULT NULL COMMENT '折扣比例',
  `discountAmount` int(20) DEFAULT NULL COMMENT '折扣金额',
  `number` int(11) DEFAULT NULL COMMENT '购买数量',
  `subtotal` int(20) DEFAULT NULL COMMENT '小计金额',
  `isProductExists` tinyint(1) DEFAULT NULL COMMENT '商品是否有效',
  `remark` varchar(50) DEFAULT NULL COMMENT '客户商品备注',
  KEY `orderDetailId` (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单商品详情';

-- Dumping data for table shop.order_detail: ~0 rows (approximately)
-- Dumping structure for table shop.order_dispatch
CREATE TABLE IF NOT EXISTS `order_dispatch` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单调度表id',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `fromSellerUid` int(20) DEFAULT NULL COMMENT '被调度的营销人员用户id',
  `toSellerUid` int(20) DEFAULT NULL COMMENT '营销人员用户id',
  `reason` varchar(50) DEFAULT NULL COMMENT '调度原因',
  `adminUid` varchar(50) DEFAULT NULL COMMENT '调度管理员',
  `createdTime` datetime DEFAULT NULL COMMENT '调度日期',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单调度';

-- Dumping data for table shop.order_dispatch: ~0 rows (approximately)
-- Dumping structure for table shop.order_invoice
CREATE TABLE IF NOT EXISTS `order_invoice` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单发票表ID',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `isVat` varchar(50) DEFAULT NULL COMMENT '是否增值税发票,普通发票,增值发票',
  `name` varchar(50) DEFAULT NULL COMMENT '发票抬头名称',
  `content` varchar(50) DEFAULT NULL COMMENT '发票抬头内容',
  `amount` int(20) DEFAULT NULL COMMENT '发票金额',
  `taxNo` int(20) DEFAULT NULL COMMENT '发票税号',
  `invoiceTax` int(20) DEFAULT NULL COMMENT '开票税金',
  `vatCompanyName` varchar(50) DEFAULT NULL COMMENT '公司名称[增值税]',
  `vatCompanyAddress` varchar(50) DEFAULT NULL COMMENT '公司地址[增值税]',
  `vatTelphone` int(20) DEFAULT NULL COMMENT '联系电话[增值税]',
  `vatBankName` varchar(20) DEFAULT NULL COMMENT '开户银行[增值税]',
  `vatBankAccount` int(20) DEFAULT NULL COMMENT '银行帐号[增值税]',
  `createdTime` datetime DEFAULT NULL COMMENT '开票时间',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单发票';

-- Dumping data for table shop.order_invoice: ~0 rows (approximately)
-- Dumping structure for table shop.order_logistics
CREATE TABLE IF NOT EXISTS `order_logistics` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单物流表id',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `expressNo` int(20) DEFAULT NULL COMMENT '物流单号,发货快递单号',
  `consigneeRealname` varchar(10) DEFAULT NULL COMMENT '收货人姓名,收货地址表可能更新或删除，因此要在这里记录',
  `consigneeTelphone` int(20) DEFAULT NULL COMMENT '联系电话,收货地址表可能更新或删除，因此要在这里记录',
  `consigneeTelphone2` int(20) DEFAULT NULL COMMENT '备用联系电话,收货地址表可能更新或删除，因此要在这里记录',
  `consigneeAddress` varchar(50) DEFAULT NULL COMMENT '收货地址,收货地址表可能更新或删除，因此要在这里记录',
  `consigneeZip` int(20) DEFAULT NULL COMMENT '邮政编码,收货地址表可能更新或删除，因此要在这里记录',
  `logisticsType` varchar(50) DEFAULT NULL COMMENT '物流方式, ems, express等',
  `logisticsId` int(20) DEFAULT NULL COMMENT '物流商家表id',
  `logisticsFee` int(20) DEFAULT NULL COMMENT '物流发货运费,显示给客户的订单运费',
  `agencyFee` int(20) DEFAULT NULL COMMENT '快递代收货款费率,快递公司代收货款费率，如货值的2%-5%，一般月结',
  `deliveryAmount` int(20) DEFAULT NULL COMMENT '物流成本金额,实际支付给物流公司的金额',
  `orderlogisticsStatus` varchar(50) DEFAULT NULL COMMENT '物流状态',
  `logisticsSettlementStatus` varchar(50) DEFAULT NULL COMMENT '物流结算状态,未结算,已结算,部分结算',
  `logisticsResultLast` varchar(50) DEFAULT NULL COMMENT '物流最后状态描述',
  `logisticsResult` varchar(50) DEFAULT NULL COMMENT '物流描述',
  `logisticsCreateTime` datetime DEFAULT NULL COMMENT '发货时间',
  `logisticsUpdateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '物流更新时间',
  `logistics_settlement_time` datetime DEFAULT NULL COMMENT '物流结算时间',
  `logisticsPaymentChannel` varchar(50) DEFAULT NULL COMMENT '物流支付渠道',
  `logisticsPaymentNo` int(20) DEFAULT NULL COMMENT '物流支付单号',
  `reconciliationStatus` tinyint(4) DEFAULT NULL COMMENT '物流公司已对账状态,已对账,未对账',
  `reconciliationTime` datetime DEFAULT NULL COMMENT '物流公司对账日期',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单物流';

-- Dumping data for table shop.order_logistics: ~0 rows (approximately)
-- Dumping structure for table shop.order_returns
CREATE TABLE IF NOT EXISTS `order_returns` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单退货表id',
  `returnsNo` int(20) DEFAULT NULL COMMENT '退货编号,供客户查询',
  `orderId` int(20) DEFAULT NULL COMMENT '订单表id',
  `expressNo` int(20) DEFAULT NULL COMMENT '退货物流单号',
  `consigneeRealname` varchar(20) DEFAULT NULL COMMENT '收货人姓名',
  `consigneeTelphone` int(20) DEFAULT NULL COMMENT '联系电话',
  `consigneeTelphone2` int(20) DEFAULT NULL COMMENT '备用联系电话',
  `consigneeAddress` varchar(50) DEFAULT NULL COMMENT '收货地址',
  `consigneeZip` int(20) DEFAULT NULL COMMENT '邮政编码',
  `logisticsType` varchar(50) DEFAULT NULL COMMENT '物流方式',
  `logisticsId` int(20) DEFAULT NULL COMMENT '物流商家id',
  `logisticsFee` int(20) DEFAULT NULL COMMENT '物流发货运费,退货运费',
  `orderlogisticsStatus` tinyint(4) DEFAULT NULL COMMENT '物流状态',
  `logisticsResultLast` varchar(20) DEFAULT NULL COMMENT '物流最后状态描述',
  `logisticsResult` varchar(50) DEFAULT NULL COMMENT '物流描述',
  `logisticsUpdateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '物流更新时间',
  `logisticsCreateTime` datetime DEFAULT NULL COMMENT '物流发货时间',
  `returnsType` tinyint(4) DEFAULT NULL COMMENT '退货类型,全部退单,部分退单',
  `handlingWay` varchar(50) DEFAULT NULL COMMENT '退货处理方式PUPAWAY:退货入库;REDELIVERY:重新发货;RECLAIM-REDELIVERY:不要求归还并重新发货; REFUND:退款; COMPENSATION:不退货并赔偿',
  `returnsAmount` int(20) DEFAULT NULL COMMENT '退款金额',
  `sellerPunishFee` int(20) DEFAULT NULL COMMENT '退货销售员承担的费用',
  `returnSubmitTime` datetime DEFAULT NULL COMMENT '退货申请时间',
  `handlingTime` datetime DEFAULT NULL COMMENT '退货处理时间',
  `reasonForReturn` varchar(50) DEFAULT NULL COMMENT '退货原因',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单退货';

-- Dumping data for table shop.order_returns: ~0 rows (approximately)
-- Dumping structure for table shop.shoppingcart
CREATE TABLE IF NOT EXISTS `shoppingcart` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '购物车表id',
  `userId` int(20) DEFAULT NULL COMMENT '用户表id',
  `shopId` int(20) DEFAULT NULL COMMENT '商店表id',
  `productId` int(20) DEFAULT NULL COMMENT '商品表id',
  `isProductExists` tinyint(1) DEFAULT NULL COMMENT '是否有效',
  `number` int(20) DEFAULT NULL COMMENT '购买数量',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  KEY `id` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车';

-- Dumping data for table shop.shoppingcart: ~0 rows (approximately)