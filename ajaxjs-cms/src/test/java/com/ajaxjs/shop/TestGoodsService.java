package com.ajaxjs.shop;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.cms.utils.CmsUtils;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mock.TestHelper;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.shop.model.Goods;
import com.ajaxjs.shop.service.GoodsService;
import com.ajaxjs.shop.service.SellerService;
import com.ajaxjs.user.model.UserAddress;
import com.ajaxjs.user.service.UserAddressService;

public class TestGoodsService {
//	static GoodsService service;
//	static ShopBookmarkService goodsBookmarkService;

	@BeforeClass
	public static void initDb() {
		CmsUtils.init2("c:\\project\\bgdiving", "com.ajaxjs.cms", "com.ajaxjs.user", "com.ajaxjs.shop");
		
//		System.out.println(new GoodsService().findList());
		
		System.out.println("------------------------->" + BeanContext.getBean(GoodsService.class).findList());
//		service = BeanContext.getBean(GoodsService.class);
//		System.out.println(service.findList());
//		goodsBookmarkService = (ShopBookmarkService) BeanContext.getBean("GoodsBookmarkService");
	}

	static String[] names = new String[] { "Apple iPhone X", "华为 HUAWEI P20", "小米8SE", "联想Z5" };
	static String[] brands = new String[] { "Apple", "华为", "小米", "联想" };
	static String[] content = new String[] { "64GB 深空灰色 移动联通电信4G手机", " 4GB+32GB 曜黑 全网通移动联通电信4G手机 双卡双待" };
	static String[] coverPrices = new String[] { "1500", "1680", "5000" };

	String content2 = "<table class=\"tm-tableAttr\" style=\"margin: 0px 0px 10px; padding: 0px; border-collapse: collapse; border-spacing: 0px; border: 1px solid rgb(229, 229, 229);color: rgb(64, 64, 64); font-family: tahoma, arial, 微软雅黑, sans-serif; font-size: 12px; background-color: rgb(255, 255, 255);\"><tbody style=\"margin: 0px; padding: 0px;\"><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" data-spm-anchor-id=\"a220o.1000855.0.i1.6cd86d99a3tzAN\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">拍照功能</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">后置摄像头</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;4800万像素</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">摄像头类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;前置2500万，后置4800万</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">视频显示格式</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;3gp，mp4</td></tr><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">显示</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">分辨率</th><td data-spm-anchor-id=\"a220o.1000855.0.i2.6cd86d99a3tzAN\" style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;1080 x 2310 像素</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">触摸屏类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;电容屏，多点触控</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">屏幕尺寸</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;6.4英寸</td></tr><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">网络</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">网络类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;4G全网通</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">网络模式</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;双卡双待</td></tr><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">机身详情</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">键盘类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;虚拟QWERTY键盘</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">款式</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;直板</td></tr><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">存储</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">运行内存RAM</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;6GB,8GB</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">存储容量</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;6+128GB&nbsp;8+128GB&nbsp;8+256GB</td></tr><tr class=\"tm-tableAttrSub\" style=\"margin: 0px; padding: 0px;\"><th colspan=\"2\" style=\"margin: 0px; padding: 5px 5px 5px 20px; text-align: left; width: 763px; border-top: 1px solid rgb(229, 229, 229); border-right: 1px solid rgb(229, 229, 229); background-color: rgb(247, 247, 247);\">基本参数</th></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">品牌</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;honor/荣耀</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">型号</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;荣耀V20</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">电池类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;不可拆卸式电池</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">核心数</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;八核 2×Cortex-A76 Based 2.6GHz+</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">机身颜色</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;魅海蓝&nbsp;魅丽红&nbsp;幻夜黑&nbsp;MOSCHINO版幻影蓝</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">手机类型</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;拍照手机&nbsp;音乐手机&nbsp;时尚手机&nbsp;智能手机&nbsp;4G手机&nbsp;商务手机</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">操作系统</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;Android 9</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">CPU品牌</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;HUAWEI Kirin 980</td></tr><tr style=\"margin: 0px; padding: 0px;\"><th style=\"margin: 0px; padding: 5px 5px 5px 20px; color: rgb(153, 153, 153); font-weight: 400; text-align: right; width: 147px; border-top: 1px solid rgb(247, 247, 247); border-right: 1px solid rgb(247, 247, 247);\">产品名称</th><td style=\"margin: 0px; padding: 5px; border-top: 1px solid rgb(247, 247, 247);\">&nbsp;荣耀V20</td></tr></tbody></table>";

//	@Test
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
			Goods entity = new Goods();
			entity.setName(TestHelper.getItem(names));
			entity.setSubTitle(TestHelper.getItem(content));
			entity.setCoverPrice(TestHelper.getItem(coverPrices));
			;
			entity.setContent(TestHelper.getItem(content));
			entity.setBrand(TestHelper.getItem(brands));

//			assertNotNull(service.create(entity));
		}
	}


 

//	@Test
	public void testGoodsAddress() {
		UserAddressService userAddressService = (UserAddressService) BeanContext.getBean("UserAddressService");

		for (int i = 0; i < 10; i++) {
			UserAddress bean = new UserAddress();
			bean.setName(TestHelper.getChineseName());
			bean.setMobile(TestHelper.getTel());
			bean.setPhone(TestHelper.getTel());
			bean.setProvince(TestHelper.getItem(TestHelper.provinces));
			bean.setCity(TestHelper.getItem(TestHelper.cites));
			bean.setDistrict(TestHelper.getItem(TestHelper.districts));
			bean.setAddress(TestHelper.getRoad());
			bean.setUserId(10000L);
			userAddressService.create(bean);
		}

	}

	public static String getOrderNumber() {
		return "2018" + (SnowflakeIdWorker.idWorker.nextId() + "").substring(8, 18);
	}

//	@Test
	public void testPageList() {
//		List<Goods> goodsList = goodsBookmarkService.getGoodsListByUserId(10000L);
//		assertNotNull(goodsList);
//		PageResult<Goods> page = service.findPagedList(0, 10);
//		assertNotNull(page.getTotalCount());

//		PageResult<Map<String, Object>> goods = service.findGoods_Format(0, 10);
//		System.out.println(goods.getTotalCount());
//		assertNotNull(goods.getTotalCount());
	}

	@Test
	public void testGetGoodsDetal() {

	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
