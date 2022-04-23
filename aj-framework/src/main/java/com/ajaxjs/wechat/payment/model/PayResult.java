package com.ajaxjs.wechat.payment.model;

/**
 * 由微信支付通知过来的支付结果
 * 
 * @author Frank Cheung
 *
 */
public class PayResult {
	/**
	 * 商户订单号
	 */
	private String out_trade_no;

	/**
	 * 微信支付订单号
	 */
	private String transaction_id;

	/**
	 * 交易类型
	 * 
	 * JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
	 */
	private String trade_type;

	/**
	 * 交易状态
	 * 
	 * SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（付款码支付）
	 * USERPAYING：用户支付中（付款码支付） PAYERROR：支付失败(其他原因，如银行返回失败)
	 */
	private String trade_state;

	/**
	 * 交易状态描述
	 */
	private String trade_state_desc;

	/**
	 * 付款银行
	 */
	private String bank_type;

	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 支付完成时间
	 */
	private String success_time;

	/**
	 * 支付完成时间
	 */
	private String payerOpenId;

	/**
	 * 总金额
	 */
	private Integer total;

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}

	public String getTrade_state_desc() {
		return trade_state_desc;
	}

	public void setTrade_state_desc(String trade_state_desc) {
		this.trade_state_desc = trade_state_desc;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSuccess_time() {
		return success_time;
	}

	public void setSuccess_time(String success_time) {
		this.success_time = success_time;
	}

	public String getPayerOpenId() {
		return payerOpenId;
	}

	public void setPayerOpenId(String payerOpenId) {
		this.payerOpenId = payerOpenId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPayer_total() {
		return payer_total;
	}

	public void setPayer_total(Integer payer_total) {
		this.payer_total = payer_total;
	}

	/**
	 * 用户支付金额
	 */
	private Integer payer_total;

}
