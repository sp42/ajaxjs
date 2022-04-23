package com.ajaxjs.framework;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.ajaxjs.framework.shop.model.Goods;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.sql.util.Gen;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.map.MapTool;

public class MakeTestData {
	static String token = "f953c611-fc87-4863-a314-03da0a66587b";

	static Consumer<HttpURLConnection> setHeader = conn -> {
		conn.setRequestProperty("Authorization", "Bearer " + token);
	};

	static String[] project_id = { "702d92fc-3e7a-43b7-a3e2-af814fdacffd", "ab84fde9-580e-4a89-8d8d-79769c39e129", "8052a861-2b5d-4fe8-a9f6-a85863b96b69" };
	static String[] project_name = { "与湖南德山资本洽谈在龙泉园区建设100万吨磷酸铁锂正极材料项目", "智能装备（军民融合）产业园项目", "京东（玉溪）新经济产业园总部经济项目" };
	static String[] project_industry = { "信息传输、制造业/专用设备制造", "制造业/化学原料及化学制品/合成材料", "制造业/专用设备制造" };
	static String[] project_location = { "北京", "上海", "广州" };

	public static void main(String[] args) {
		for (int i = 0; i < 200; i++)
			createGoodsFormat();
	}

	static String[] phones = { "老人手机", "智能手机", "三防手机", "智能电视", "投影仪", "收音机" };
	static String[] brands = { "华为", "荣耀", "苹果", "小米", "中兴", "Oppo" };
	static String[] covers = { "https://file08.c.hihonor.com/pimages/product/6973316859874/428_428_36CBF00AC6B17C125B5FA98988C1F97446EB33AC0328B7DCmp.png",
			"https://file08.c.hihonor.com/pimages/product/6973316858433/428_428_7C3CE8569FDABE56DC5F9FD14FD18BC5BF1CFFF67D0BC73Dmp.png",
			"https://file08.c.hihonor.com/pimages/product/6936520803637/428_428_0C026E0F21285DFC33277D2FA54D92914459A0145DBAEA1Amp.png" };
	static String[] coverPrice = { "1200", "最低 1000", "2000 七折", "2100", "首付200", "2001" };
	static String[] intros = { "【中国电信国企品质保障】限时享直降", "5000mAh长续航，22.5w快充", "6.74英寸高刷护眼屏，评论有赏返京豆！", "荣耀honor是全球领先的智能终端提供商，致力于成为构建全场景、面向全渠道、服务全人群的全球标志性科技品牌" };

//	static void createGoods() {
//		Goods goods = new Goods();
//		goods.setName(Gen.getItem(phones));
//		goods.setBrand(Gen.getItem(brands));
//		goods.setCover(Gen.getItem(covers));
//		goods.setContent(Gen.getParagraph());
//		goods.setCoverPrice(Gen.getItem(coverPrice));
//		goods.setIntro(Gen.getItem(intros));
//
//		Map<String, Object> map = MapTool.bean2Map(goods);
//		map.remove("catalogName");
//		map.remove("areaId");
//		map.remove("sellerId");
//		map.remove("stat");
//		map.remove("createDate");
//		map.remove("catelogId");
//		map.remove("updateDate");
//		map.remove("extractData");
//
//		cleanNull(map);
//
//		Post.api("http://127.0.0.1:8080/cp/api/cms/shop_goods", map);
//	}

	static double[] prices = { 2331.3d, 33f, 2333d };
	static String[] goodsFormatNames = { "8G/16G RAM", "16G/32G RAM", "16G/64G RAM", "32G/256G RAM" };

	static void createGoodsFormat() {
		Goods f = new Goods();
		f.setName(Gen.getItem(goodsFormatNames));
//		f.setGoodsId((long) Gen.getNum(1, 200));
		f.setPrice(BigDecimal.valueOf(Gen.getItem(prices)));

		Map<String, Object> map = MapTool.bean2Map(f);
		cleanNull(map);

		Post.api("http://127.0.0.1:8080/cp/api/cms/shop_goods_format", map);
	}

	static void cleanNull(Map<String, Object> map) {
		List<String> list = new ArrayList<>();
		for (String k : map.keySet()) {
			if (map.get(k) == null || "null".equals(map.get(k)))
				list.add(k);
		}

		for (String k : list)
			map.remove(k);
	}

	static void createMeeting() {
//        String[] meeting_id = {"2ab26e67-add2-48f9-b72f-c8557e7a1bba", "2af07cb7-ee71-448a-abab-678330cd6279", "2f0be9cc-ea49-4b27-9fd7-b513a6318843"};
//        String[] project_agreement = {"投资协议", "框架协议"};
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("meeting_id", Gen.getItem(meeting_id));
//        params.put("project_id", Gen.getItem(project_id));
//        params.put("project_name", Gen.getItem(project_name));
//        params.put("project_agreement",  Gen.getItem(project_agreement));
//        Map<String, Object> result = Post.api("http://10.5.12.255:8081/yuxi/api/yuxi-business/enterprise_tract_meeting", params, setHeader);
//
//        System.out.println(result);
	}

	static void createMeetingProject() {
		String[] meeting_id = { "2ab26e67-add2-48f9-b72f-c8557e7a1bba", "2af07cb7-ee71-448a-abab-678330cd6279", "2f0be9cc-ea49-4b27-9fd7-b513a6318843" };
		String[] project_agreement = { "投资协议", "框架协议" };

		Map<String, Object> params = new HashMap<>();
		params.put("meeting_id", Gen.getItem(meeting_id));
		params.put("project_id", Gen.getItem(project_id));
		params.put("project_name", Gen.getItem(project_name));
		params.put("project_industry", Gen.getItem(project_industry));
		params.put("project_agreement", Gen.getItem(project_agreement));
		Map<String, Object> result = Post.api("http://localhost:8081/yuxi/api/yuxi-business/trace_meeting_projects", params, setHeader);

		System.out.println(result);
	}

	static void createFund() {
		Map<String, Object> params = new HashMap<>();
		params.put("project_id", Gen.getItem(project_id));
		params.put("project_name", Gen.getItem(project_name));
		params.put("project_industry", Gen.getItem(project_industry));
		params.put("project_year", random_int(2009, 2022) + "");
		params.put("fund_code", StrUtil.getRandomString(10) + "");
		params.put("money", random_int(1, 20));
		params.put("payment", random_int(1, 2));
		Map<String, Object> result = Post.api("http://localhost:8081/yuxi/api/yuxi-business/trace_fund", params, setHeader);

		System.out.println(result);
	}

	static String[] company = { "珠海中珠集团股份有限公司", "京东（云南）数字经济有限公司", "云南德新纸业有限公司" };

	static void createFundHistory() {

		String[] fund_id = { "702d92fc-3e7a-43b7-a3e2-af814fdacffd", "ab84fde9-580e-4a89-8d8d-79769c39e129", "8052a861-2b5d-4fe8-a9f6-a85863b96b69" };
		String[] time = { "2022-01-18 21:07:27", "2022-01-18 21:07:19", "2022-01-18 21:07:37" };

		Map<String, Object> params = new HashMap<>();
		params.put("fund_id", Gen.getItem(fund_id));
		params.put("company", Gen.getItem(company));
		params.put("content", Gen.getItem(time));
		params.put("pay_time", Gen.getItem(time));
		params.put("pay_time_plan", Gen.getItem(time));
		params.put("money", random_int(1, 20));
		params.put("meeting_type", random_int(1, 3));
		params.put("meeting_time", Gen.getItem(time));
		params.put("meeting_money", random_int(1, 20));
		Map<String, Object> result = Post.api("http://localhost:8081/yuxi/api/yuxi-business/trace_fund_history", params, setHeader);

		System.out.println(result);
	}

	static void createFormalities() {
		Map<String, Object> params = new HashMap<>();
		params.put("project_id", Gen.getItem(project_id));
		params.put("project_name", Gen.getItem(project_name));
		params.put("project_industry", Gen.getItem(project_industry));
		params.put("project_year", random_int(2009, 2022) + "");
		params.put("project_location", Gen.getItem(project_location));

		Map<String, Object> result = Post.api("http://localhost:8082/yuxi/api/yuxi-business/trace_formalities", params, setHeader);
		System.out.println(result);
	}

	static void createFormalitiesInfo() {
		String[] formalities_id = { "0077f4f4-d64c-4855-a620-477ba7ee7c6e", "02642956-51d2-4fad-9626-3b15d85eb70c", "02e41a06-00ff-4b0a-b686-bdd3a417dfb0" };

		Map<String, Object> params = new HashMap<>();
		params.put("company", Gen.getItem(company));
		params.put("company_contact", Gen.getRoad());
		params.put("company_phone", Gen.getTel());
		params.put("formalities_id", Gen.getItem(formalities_id));

		Map<String, Object> result = Post.api("http://localhost:8082/yuxi/api/yuxi-business/trace_formalities_info", params, setHeader);
		System.out.println(result);
	}

	public static int random_int(int Min, int Max) {
		return (int) (Math.random() * (Max - Min)) + Min;
	}

	static String[] owner_id = { "01162a6b-c47e-41a1-825c-8e6f5d688e96", "03d3d495-67e7-4259-a00e-650351ab5974", "05da5b17-4a08-4043-8b0d-fb2e7e45f53a" };

	static void createAttachemnt() {
		Map<String, Object> params = new HashMap<>();
		params.put("owner_id", Gen.getItem(owner_id));
		params.put("name", Gen.getRoad());
		params.put("format", Gen.getItem(company));
		params.put("size", random_int(100, 234));

		Map<String, Object> result = Post.api("http://localhost:8082/yuxi/api/yuxi-business/trace_attachment", params, setHeader);
		System.out.println(result);
	}

}
