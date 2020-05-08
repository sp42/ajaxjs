package com.ajaxjs.shop.payment.wechat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.util.logger.LogHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * PC 版支付
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class PcPay {
	private static final LogHelper LOGGER = LogHelper.getLog(PcPay.class);

	/**
	 * 微信下单接口
	 *
	 * @param out_trade_no 订单号
	 * @param body         商家商品名
	 * @param money        总金额
	 * @param applyNo      商品编号
	 * @return
	 */
	public static String unifiedOrder(OrderInfo order) {
		LOGGER.info("PC 微信下单请求交易开始");
		Map<String, String> data = new HashMap<>();
		WxUtil.commonSetUnifiedOrder(order, data);

		data.put("body", "TESTTEST");
		data.put("product_id", order.getId() + ""); // 商品 id，但一个订单可能包含多个商品，所以填入订单数据库 id
		data.put("notify_url", ConfigService.getValueAsString("shop.payment.wx.notifyUrl"));
		data.put("appid", ConfigService.getValueAsString("shop.payment.wx.appId"));
		data.put("mch_id", ConfigService.getValueAsString("shop.payment.wx.mchId"));

		data.put("trade_type", "NATIVE");
		data.put("device_info", "web");
		data.put("spbill_create_ip", Tools.getIp());

		data.put("sign", WxUtil.generateSignature(data, ConfigService.getValueAsString("shop.payment.wx.apiSecret")));

		WxUtil.sendUnifiedOrder(data);

		return null;
	}

	/**
	 * 创建二维码输入流
	 * 
	 * @param content    二维码内容
	 * @param qrCodeSize 二维码尺寸
	 * @param response   响应给页面
	 */
	public static void createQrCodeStream(String content, int qrCodeSize, HttpServletResponse response) {
		// 设置二维码纠错级别 map
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // 矫错级别

		// 创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = null;
		try {
			byteMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		} catch (WriterException e) {
			LOGGER.warning(e);
		}

		// 使BufferedImage勾画QRCode (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		graphics.setColor(Color.BLACK);// 使用比特矩阵画并保存图像

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j))
					graphics.fillRect(i - 100, j - 100, 1, 1);
			}
		}
		// 返回给页面
		try {
			OutputStream outputStream = response.getOutputStream();
			// 关流
			try {
				ImageIO.write(image, "JPEG", outputStream);
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

}
