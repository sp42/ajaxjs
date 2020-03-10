package com.ajaxjs.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.framework.BaseModel;

/**
 * 
 * 订单实体
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class OrderInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一订单号
	 */
	private String orderNo;
	
	
	private String transactionId;
	


	/**
	 * 设置唯一订单号
	 * 
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 获取唯一订单号
	 * 
	 * @return 唯一订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 买家 id
	 */
	private Long buyerId;

	/**
	 * 设置买家 id
	 * 
	 * @param buyerId
	 */
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	/**
	 * 获取买家 id
	 * 
	 * @return 买家 id
	 */
	public Long getBuyerId() {
		return buyerId;
	}

	/**
	 * 交易状态：0=进行中
	 */
	private Integer tradeStatus;

	/**
	 * 设置交易状态：0=进行中
	 * 
	 * @param tradeStatus
	 */
	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	/**
	 * 获取交易状态：0=进行中
	 * 
	 * @return 交易状态：0=进行中
	 */
	public Integer getTradeStatus() {
		return tradeStatus;
	}

	/**
	 * 支付状态：1=未付款
	 */
	private Integer payStatus;

	/**
	 * 设置支付状态：1=未付款
	 * 
	 * @param payStatus
	 */
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	/**
	 * 获取支付状态：1=未付款
	 * 
	 * @return 支付状态：1=未付款
	 */
	public Integer getPayStatus() {
		return payStatus;
	}

	/**
	 * 最佳送货时间
	 */
	private java.util.Date bestDeliveryTime;

	/**
	 * 设置最佳送货时间
	 * 
	 * @param bestDeliveryTime
	 */
	public void setBestDeliveryTime(java.util.Date bestDeliveryTime) {
		this.bestDeliveryTime = bestDeliveryTime;
	}

	/**
	 * 获取最佳送货时间
	 * 
	 * @return 最佳送货时间
	 */
	public java.util.Date getBestDeliveryTime() {
		return bestDeliveryTime;
	}

	/**
	 * 商品id外键
	 */
	private String goodsId;

	/**
	 * 设置商品id外键
	 * 
	 * @param goodsId
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * 获取商品id外键
	 * 
	 * @return 商品id外键
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * 商品标签
	 */
	private String label;

	/**
	 * 设置商品标签
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 获取商品标签
	 * 
	 * @return 商品标签
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 订单金额
	 */
	private BigDecimal orderPrice;

	/**
	 * 设置订单金额
	 * 
	 * @param orderPrice
	 */
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	/**
	 * 最终金额
	 */
	private BigDecimal totalPrice;

	/**
	 * 设置最终金额
	 * 
	 * @param totalPrice
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * 获取最终金额
	 * 
	 * @return 最终金额
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * 创建者 id
	 */
	private Integer createByUser;

	/**
	 * 设置创建者 id
	 * 
	 * @param createByUser
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}

	/**
	 * 获取创建者 id
	 * 
	 * @return 创建者 id
	 */
	public Integer getCreateByUser() {
		return createByUser;
	}

	/**
	 * 是否已删除 1=已删除；0/null；未删除
	 */
	private Integer deleted;

	/**
	 * 设置是否已删除 1=已删除；0/null；未删除
	 * 
	 * @param deleted
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * 获取是否已删除 1=已删除；0/null；未删除
	 * 
	 * @return 是否已删除 1=已删除；0/null；未删除
	 */
	public Integer getDeleted() {
		return deleted;
	}

	/**
	 * 分类 id
	 */
	private Integer catelogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId
	 */
	public void setCatelogId(Integer catelogId) {
		this.catelogId = catelogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatelogId() {
		return catelogId;
	}

	/**
	 * 交易订单号
	 */
	private String outerTradeNo;

	/**
	 * 设置交易订单号
	 * 
	 * @param outerTradeNo
	 */
	public void setOuterTradeNo(String outerTradeNo) {
		this.outerTradeNo = outerTradeNo;
	}

	/**
	 * 获取交易订单号
	 * 
	 * @return 交易订单号
	 */
	public String getOuterTradeNo() {
		return outerTradeNo;
	}

	/**
	 * 支付类型：1=在线支付
	 */
	private Integer payType;

	/**
	 * 设置支付类型：1=在线支付
	 * 
	 * @param payType
	 */
	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	/**
	 * 获取支付类型：1=在线支付
	 * 
	 * @return 支付类型：1=在线支付
	 */
	public Integer getPayType() {
		return payType;
	}

	/**
	 * 付款时间
	 */
	private java.util.Date payDate;

	/**
	 * 设置付款时间
	 * 
	 * @param payDate
	 */
	public void setPayDate(java.util.Date payDate) {
		this.payDate = payDate;
	}

	/**
	 * 获取付款时间
	 * 
	 * @return 付款时间
	 */
	public java.util.Date getPayDate() {
		return payDate;
	}

	/**
	 * 物流名称
	 */
	private String shipping;

	/**
	 * 设置物流名称
	 * 
	 * @param shipping
	 */
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	/**
	 * 获取物流名称
	 * 
	 * @return 物流名称
	 */
	public String getShipping() {
		return shipping;
	}

	/**
	 * 物流单号
	 */
	private String shippingCode;

	/**
	 * 设置物流单号
	 * 
	 * @param shippingCode
	 */
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	/**
	 * 获取物流单号
	 * 
	 * @return 物流单号
	 */
	public String getShippingCode() {
		return shippingCode;
	}

	/**
	 * 物流费用
	 */
	private BigDecimal shippingFee;

	/**
	 * 设置物流费用
	 * 
	 * @param shippingFee
	 */
	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	/**
	 * 获取物流费用
	 * 
	 * @return 物流费用
	 */
	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	/**
	 * 
	 */
	private String shippingTarget;

	/**
	 * 设置
	 * 
	 * @param shippingTarget
	 */
	public void setShippingTarget(String shippingTarget) {
		this.shippingTarget = shippingTarget;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getShippingTarget() {
		return shippingTarget;
	}

	/**
	 * 收货地址（由地址簿生成）
	 */
	private String shippingAddress;

	/**
	 * 设置收货地址（由地址簿生成）
	 * 
	 * @param shippingAddress
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * 获取收货地址（由地址簿生成）
	 * 
	 * @return 收货地址（由地址簿生成）
	 */
	public String getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * 收货人联系方式（由地址簿的电话生成）
	 */
	private String shippingPhone;

	/**
	 * 设置收货人联系方式（由地址簿的电话生成）
	 * 
	 * @param shippingPhone
	 */
	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	/**
	 * 获取收货人联系方式（由地址簿的电话生成）
	 * 
	 * @return 收货人联系方式（由地址簿的电话生成）
	 */
	public String getShippingPhone() {
		return shippingPhone;
	}

	public OrderItem[] getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(OrderItem[] orderItems) {
		this.orderItems = orderItems;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
 
	private OrderItem[] orderItems;

}