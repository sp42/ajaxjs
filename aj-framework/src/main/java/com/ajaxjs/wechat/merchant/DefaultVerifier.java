package com.ajaxjs.wechat.merchant;

import java.security.cert.X509Certificate;
import java.util.NoSuchElementException;

/**
 * 内部验签器
 */
public class DefaultVerifier {
	private String merchantId;

	private DefaultVerifier(String merchantId) {
		this.setMerchantId(merchantId);
	}

//	public boolean verify(String serialNumber, byte[] message, String signature) {
//		if (serialNumber.isEmpty() || message.length == 0 || signature.isEmpty())
//			throw new IllegalArgumentException("serialNumber或message或signature为空");
//
//		BigInteger serialNumber16Radix = new BigInteger(serialNumber, 16);
//		ConcurrentHashMap<BigInteger, X509Certificate> merchantCertificates = certificates.get(merchantId);
//		X509Certificate certificate = merchantCertificates.get(serialNumber16Radix);
//
//		if (certificate == null) {
////			log.error("商户证书为空，serialNumber:{}", serialNumber);
//			return false;
//		}
//
//		try {
//			Signature sign = Signature.getInstance("SHA256withRSA");
//			sign.initVerify(certificate);
//			sign.update(message);
//
//			return sign.verify(Base64.getDecoder().decode(signature));
//		} catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
//		} catch (SignatureException e) {
//			throw new RuntimeException("签名验证过程发生了错误", e);
//		} catch (InvalidKeyException e) {
//			throw new RuntimeException("无效的证书", e);
//		}
//	}

	/**
	 * 获取合法的平台证书
	 *
	 * @return 合法证书
	 */
	public X509Certificate getValidCertificate() {
		X509Certificate certificate = null;

		try {
//			certificate = CertificatesManager.this.getLatestCertificate(merchantId);
		} catch (Exception e) {
			throw new NoSuchElementException("没有有效的微信支付平台证书");
		}

		return certificate;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
}