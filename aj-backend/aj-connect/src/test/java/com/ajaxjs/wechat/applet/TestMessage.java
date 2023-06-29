package com.ajaxjs.wechat.applet;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.wechat.applet.model.TemplateMsgMp;
import com.ajaxjs.wechat.applet.model.TemplateMsgWebApp;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestMessage {
    Message m;

    {
        WeChatAppletConfig cfg = new WeChatAppletConfig();
        cfg.setAccessKeyId("wx755cba2f58e01fff");
        cfg.setAccessSecret("cb1bdc966083037e1e3174837bda8b91");

        GetToken getToken = new GetToken(cfg);
        getToken.init();

        m = new Message(cfg);
    }

    @Test

    public void testSend() {
        TemplateMsgMp msg = new TemplateMsgMp();
        msg.setAppid("wx450b94ff4d18b540");
        msg.setTemplate_id("f46S5vuzp4aB3OEuCNDJendMs3HuoIALWqVDjJWwS5Y");
        msg.setUrl("http://foo");

        Map<String, Object> params = new HashMap<>();
        params.put("first", ListUtils.hashMap("value", "您有一条订单产生预警消息，请尽快安排处理。"));
        params.put("keyword1", ListUtils.hashMap("value", "无"));
        params.put("keyword2", ListUtils.hashMap("value", "date"));
        params.put("keyword3", ListUtils.hashMap("value", "warningType"));
        params.put("remark", ListUtils.hashMap("value", "详细请至“车辆租赁”运营后台序查看并处理。"));
        msg.setData(params);


        TemplateMsgMp.Miniprogram miniprogram = new TemplateMsgMp.Miniprogram();
        msg.setMiniprogram(miniprogram);
        miniprogram.setAppid("wx450b94ff4d18b540");
        miniprogram.setPagepath("");

        TemplateMsgWebApp webAppMsg = new TemplateMsgWebApp();
        webAppMsg.setTemplate_id("f46S5vuzp4aB3OEuCNDJendMs3HuoIALWqVDjJWwS5Y");
        webAppMsg.setForm_id("");

        m.send("oacQ60yWx4Hh2eQ0Ye1a7vZLgugw", msg, null);
    }

    @Test
    public void testGetCategory() {
        m.getCategory();
    }

    @Test
    public void testGetPubTemplateKeyWordsById() {
        m.getPubTemplateKeyWordsById("129");
    }

    @Test
    public void testGetPubTemplateTitleList() {
        m.getPubTemplateTitleList("129");
    }

    @Test
    public void testGetMessageTemplateList() {
        m.getMessageTemplateList();
    }
}
