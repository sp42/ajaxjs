package com.ajaxjs.wechat.applet;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.wechat.applet.model.TemplateMsgMp;
import com.ajaxjs.wechat.applet.model.TemplateMsgWebApp;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Message {
    public final WeChatAppletConfig appletCfg;

    /**
     * @param appletCfg 包含 AppId 和密钥
     */
    public Message(WeChatAppletConfig appletCfg) {
        this.appletCfg = appletCfg;
    }

    private static final String UNIFORM_SEND_API = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token=";

    /**
     * 下发统一消息
     * <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/uniform-message/sendUniformMessage.html">...</a>
     *
     * @param toUser    用户 openid，可以是小程序的 openid，也可以是 mp_template_msg.appid 对应的公众号的 openid
     * @param mpMsg     公众号模板消息相关的信息，与 webAppMsg 二选一
     * @param webAppMsg 小程序模板消息相关的信息，与 mpMsg 二选一
     */
    public void send(String toUser, TemplateMsgMp mpMsg, TemplateMsgWebApp webAppMsg) {
        Map<String, Object> params = new HashMap<>();
        params.put("touser", toUser);
        if (mpMsg != null)
            params.put("mp_template_msg", mpMsg);
        if (webAppMsg != null)
            params.put("weapp_template_msg", webAppMsg);

        Map<String, Object> result = postJson(UNIFORM_SEND_API + appletCfg.getAccessToken(), ConvertToJson.toJson(params));

//        check(result);

//        Map<String, Object> result = Post.apiJsonBody(UNIFORM_SEND_API + appletCfg.getAccessToken(), "", Head.GET_JSON);
    }

    public static Map<String, Object> postJson(String url, String requestBody) {
        System.out.println("准备 POST：" + url + " \nbody：" + requestBody);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.postForEntity(url, new HttpEntity<>(requestBody, headers), String.class);
        String resultJson = entity.getBody();

        if (resultJson == null)
            throw new RuntimeException("远程请求 " + url + "返回空");
        else {
            System.out.println("远程请求 " + url + "返回：\n" + resultJson);
            return EntityConvert.json2map(resultJson);
        }
    }

    private static final String CATEGORY_API = "https://api.weixin.qq.com/wxaapi/newtmpl/getcategory?access_token=";

    /**
     * 获取小程序账号的类目
     *
     * @return 类目列表，如 [{"id":129,"name":"租车"}] 结构
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCategory() {
        Map<String, Object> result = Get.api(CATEGORY_API + appletCfg.getAccessToken());

        if (check(result))
            return (List<Map<String, Object>>) result.get("data");
        else
            return null;
    }

    private static final String PUB_TEMPLATE_TITLE_LIST_API = "https://api.weixin.qq.com/wxaapi/newtmpl/getpubtemplatetitles?start=%s&limit=%s&ids=%s&access_token=";

    /**
     * 获取所属类目下的公共模板
     *
     * @param ids 类目 id，多个用逗号隔开
     * @return 查询结果
     */
    public Map<String, Object> getPubTemplateTitleList(String ids) {
        String api = String.format(PUB_TEMPLATE_TITLE_LIST_API, 0, 20, ids);
        Map<String, Object> result = Get.api(api + appletCfg.getAccessToken());

        return check(result) ? result : null;
    }

    private static final String PUB_TEMPLATE_KEYWORDS_API = "https://api.weixin.qq.com/wxaapi/newtmpl/getpubtemplatekeywords?tid=%s&access_token=";

    /**
     * 获取关键词列表
     *
     * @param tid 模板标题 id，可通过接口获取
     */
    public void getPubTemplateKeyWordsById(String tid) {
        String api = String.format(PUB_TEMPLATE_KEYWORDS_API, tid);
        Map<String, Object> result = Get.api(api + appletCfg.getAccessToken());

        if (check(result)) {

        }
    }

    private static final String MESSAGE_TEMPLATE_LIST_API = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?access_token=";

    /**
     * 获取个人模板列表
     */
    public void getMessageTemplateList() {
        Map<String, Object> result = Get.api(MESSAGE_TEMPLATE_LIST_API + appletCfg.getAccessToken());

    }

    public static boolean check(Map<String, Object> result) {
        if (!(result.containsKey("errcode") && result.get("errcode").equals(0)))
            throw new RuntimeException("访问微信接口错误:" + result.get("errmsg"));
        else
            return true;
    }
}
