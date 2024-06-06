package com.ajaxjs.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMessageDigestHelper {
    @Test
    public void testHash() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", MessageDigestHelper.getMd5(""));
        assertEquals("4297f44b13955235245b2497399d7a93", MessageDigestHelper.getMd5("123123"));

        assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", MessageDigestHelper.getSHA1("123"));
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", MessageDigestHelper.getSHA1("abc"));

        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", MessageDigestHelper.getSHA256("abc"));
    }

    @Test
    public void testHashBase64() {
        assertEquals("1B2M2Y8AsgTpgAmY7PhCfg==", MessageDigestHelper.getMd5AsBase64(""));
    }

    @Test
    public void testKey() {
        assertEquals("a44d981236018e53119e980ac001ea65", MessageDigestHelper.getMd5("akjkjkjkjiuj", ""));
        assertEquals("pE2YEjYBjlMRnpgKwAHqZQ==", MessageDigestHelper.getMd5AsBase64("akjkjkjkjiuj", ""));
    }
}
