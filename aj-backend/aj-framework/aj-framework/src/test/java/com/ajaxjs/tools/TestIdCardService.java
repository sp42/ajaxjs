package com.ajaxjs.tools;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestIdCardService {
    @Test
    public void test() {
        assertTrue(IdCardService.check("310230199104010311"));
        assertTrue(IdCardService.check("31023019910401075X"));
        assertTrue(IdCardService.check("310230199104012413"));
        assertTrue(IdCardService.check("430525199112134512"));

        System.out.println(new IdCardService("310230199104012413").extractor());
    }
}
