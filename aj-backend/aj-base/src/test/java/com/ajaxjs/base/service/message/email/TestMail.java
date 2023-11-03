package com.ajaxjs.base.service.message.email;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestMail {
    @Test
    public void test163() {
        Mail mail = new Mail();
        mail.setMailServer("smtp.163.com");
        mail.setAccount("pacoweb");
        mail.setPassword("xxxxx"); //
        mail.setFrom("pacoweb@163.com");
        mail.setTo("frank@ajaxjs.com");
        mail.setSubject("你好容祯");
        mail.setHtmlBody(false);
        mail.setContent("xccx我希望可以跟你做朋友34354344");

        Map<String, byte[]> attachment = new HashMap<>();

        try {
            attachment.put("test14.txt", Files.readAllBytes(Paths.get("C:\\Users\\admin\\Desktop\\test.txt")));
            attachment.put("test2.txt", Files.readAllBytes(Paths.get("C:\\Users\\admin\\Desktop\\test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mail.setAttachment(attachment);

        assertTrue(Sender.send(mail));
    }

    @Test
    public void testYM163() {
        Mail mail = new Mail();
        mail.setMailServer("smtp.ym.163.com");
        mail.setAccount("admin@bgdiving.com");
        mail.setFrom("admin@bgdiving.com");
        mail.setTo("frank@ajaxjs.com");
        mail.setSubject("你好容祯");
        mail.setHtmlBody(false);
        mail.setContent("xccx我希望可以跟你做朋友34354344");

        assertTrue(Sender.send(mail));
    }

    @Test
    public void testBrevo() {
        Mail mail = new Mail();
        mail.setMailServer("smtp-relay.brevo.com");
        mail.setPort(587);
        mail.setAccount("frank@ajaxjs.com");
        mail.setPassword("WtbKrfECqZHpJhV0");
        mail.setFrom("frank@ajaxjs.com");
        mail.setTo("sp42@qq.com");
        mail.setSubject("你好容祯");
        mail.setHtmlBody(false);
        mail.setContent("xccx我希望可以跟你做朋友34354344");

        assertTrue(Sender.send(mail));
    }

}