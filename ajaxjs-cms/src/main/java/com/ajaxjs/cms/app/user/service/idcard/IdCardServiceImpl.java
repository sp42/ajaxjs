package com.ajaxjs.cms.app.user.service.idcard;

public class IdCardServiceImpl implements IdCardService {
	private String idCardNo;

	@Override
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	@Override
	public IdcardInfoExtractor getInfo() {
		return new IdcardInfoExtractor(getIdCardNo());
	}

	@Override
	public boolean fastTest() {
		return FastDetect.cleckIdNumber(getIdCardNo());
	}

	@Override
	public boolean strictTest() {
		return StrictDetect.isValidIdNo(getIdCardNo());
	}
}
