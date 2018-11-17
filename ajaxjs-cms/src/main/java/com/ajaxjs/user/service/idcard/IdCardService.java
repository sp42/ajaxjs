package com.ajaxjs.user.service.idcard;

public interface IdCardService {
	/**
	 * 设置身份证号码
	 * 
	 * @param idCardNo
	 */
	public void setIdCardNo(String idCardNo);

	/**
	 * 快速测试
	 * 
	 * @return
	 */
	public boolean fastTest();

	/**
	 * 严格测试
	 * 
	 * @return
	 */
	public boolean strictTest();

	/**
	 * 
	 * @return
	 */
	public IdcardInfoExtractor getInfo();
}
