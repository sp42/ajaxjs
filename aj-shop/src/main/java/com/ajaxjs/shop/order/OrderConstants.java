package com.ajaxjs.shop.order;

public interface OrderConstants {

	/**
	 * 订单状态
	 */
	public static enum OrderStatus {
		/**
		 * 等待支付
		 */
		Awaiting_Pay,

		/**
		 * 等待确认支付
		 * 
		 * 对于通过支付网关在线支付，一般由网关返回信息自动处理本状态
		 */
		PAYMENT_VERIFY,

		/**
		 * 等待确认信息
		 * 
		 * 支付成功后 或 选择货到付款 后 ，需要订单管理员进行订单信息的确认，如地址是否有效，是否有库存等
		 */
		INFO_VERIFY,

		/**
		 * 订单取消
		 * 
		 * 前面三个状态均可以跳到订单取消。这是订单进入履行前的终止状态。如果已经支付了，则需要做退款处理。
		 */
		CANCELLED,

		/**
		 * 等待备货
		 * 
		 * 再次检查库存并通过后，进入订单履行环节的第一个状态
		 */
		AWAITING_PICK_UP,

		/**
		 * 发货中
		 * 
		 * 生成第一张发货单，并设置该发货单状态为“发货中”时，订单的状态也更新发货中状态
		 */
		SHIPPING,

		/**
		 * 发货完毕
		 * 
		 * 设置发货单为“顾客已签收”时，判断该订单是否存在未发货的产品，如果没有，则订单的状态也更新为发货完毕状态
		 */
		SHIPPED,

		/**
		 * 发货中止
		 * 
		 * 表示剩余未发货的产品因为库存原因无法履行（比如临时损坏了，又无法调货）
		 */
		SHIP_SUSPEND,

		/**
		 * 订单完成
		 */
		COMPLETED,

		/**
		 * 不能履行
		 * 
		 * 表示进入履行阶段后，发现订单全部产品均缺货（临时损坏、无法调货等）
		 */
		IMPOSSIBLILITY,
	}
}