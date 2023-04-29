package com.ajaxjs.zinc;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.zinc.model.ZincResponse;

public class TestZinc {
	String target = "tyest";
	DocumentService docService = new DocumentService();

	{
		docService.setApi("http://localhost:4080");
		docService.setUser("admin");
		docService.setPassword("Complexpass#123");
	}

//	@Test
	public void testCreate() {
		Map<String, Object> doc = ListUtils.hashMap("title", "AIGC带你看来自“天涯海角”的新种子");
		doc.put("content", "央视新闻《开局之年“hui”蓝图》系列微视频，用AI视角，带您看两会。\r\n" + "\r\n"
				+ "目前，中国的水果产量稳居世界第一，国人的“果盘子”琳琅满目，瓜果飘香。而作为全球第一的肉类生产和消费大国，近十多年来，全国居民牛羊肉消费量也持续提升。未来的水果产业和牛羊养殖业什么样？让我们跟随AIGC，感受从田间走到舌尖的“新科技”。");

		ZincResponse resp = docService.create(target, doc);
		assertNotNull(resp);

		resp = docService.create(target, doc, 2l);
		assertNotNull(resp);
	}

//	@Test
	public void testUpdate() {
		Map<String, Object> doc = ListUtils.hashMap("title", "222222222AIGC带你看来自“天涯海角”的新种子");
		doc.put("content", "央视新闻《开局之年“hui”蓝图》系列微视频，用AI视角，带您看两会。\r\n" + "\r\n"
				+ "目前，中国的水果产量稳居世界第一，国人的“果盘子”琳琅满目，瓜果飘香。而作为全球第一的肉类生产和消费大国，近十多年来，全国居民牛羊肉消费量也持续提升。未来的水果产业和牛羊养殖业什么样？让我们跟随AIGC，感受从田间走到舌尖的“新科技”。");

		docService.update(target, doc, 2l);
	}

	@Test
	public void testDelete() {
		ZincResponse resp = docService.delete(target, 2l);
		assertNotNull(resp);
	}
}
