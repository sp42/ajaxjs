package com.ajaxjs.geo;

import org.junit.Test;

import static com.ajaxjs.geo.GeoUtils.DmTurnD;

public class TestGeo {
    @Test
    public void testDmTurnD() {
        System.out.println(DmTurnD("120°39.516'"));
        System.out.println(DmTurnD("30°46.237'"));
    }
}
