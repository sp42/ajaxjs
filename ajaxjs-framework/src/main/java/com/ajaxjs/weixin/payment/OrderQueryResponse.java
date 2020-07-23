package com.ajaxjs.weixin.payment;

import java.util.Date;

import com.ajaxjs.shop.payment.wxpay.BaseResponse;

/**
 */
public class OrderQueryResponse extends BaseResponse {

	private String deviceInfo;

	private String openId;

	private boolean subscribed;

	private String tradeType;

	private String tradeState;

	private String bankType;

	private int totalFee;

	private int settlementTotalFee;

	private String feeType;

	private int cashFee;

	private String caseFeeType;

	private int couponFee;

	private int couponFeeCount;

	private String transactionId;

	private String tradeNumber;

	private String attach;

	private Date timeEnd;

	private String tradeStateDesc;

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public boolean isSubscribed() {
		return this.subscribed;
	}

	public void setSubscribed(String subscribed) {
		this.subscribed = "Y".equalsIgnoreCase(subscribed);
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public int getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(int settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public int getCashFee() {
		return cashFee;
	}

	public void setCashFee(int cashFee) {
		this.cashFee = cashFee;
	}

	public String getCaseFeeType() {
		return caseFeeType;
	}

	public void setCaseFeeType(String caseFeeType) {
		this.caseFeeType = caseFeeType;
	}

	public int getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(int couponFee) {
		this.couponFee = couponFee;
	}

	public int getCouponFeeCount() {
		return couponFeeCount;
	}

	public void setCouponFeeCount(int couponFeeCount) {
		this.couponFeeCount = couponFeeCount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getTradeStateDesc() {
		return tradeStateDesc;
	}

	public void setTradeStateDesc(String tradeStateDesc) {
		this.tradeStateDesc = tradeStateDesc;
	}
}
