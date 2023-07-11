package com.ajaxjs.geo;

import org.springframework.util.StringUtils;

/**
 * 经纬度工具类
 */
public class GeoUtils {
    /**
     * 表示角度的度、分、秒分别使用°、′、″符号进行表示。
     * 1°=60′，1′=60″ ，1°=3600″。
     * 由上述可知度分秒转换度的计算公式为：(dd°mm′ss″) dd+mm/60+ss/3600
     * 经纬度转换 ，度分秒转度
     *
     * @param jwd
     * @return
     */
    public static String DmsTurnD(String jwd) {
        if (StringUtils.hasText(jwd) && (jwd.contains("°"))) {//如果不为空并且存在度单位
            //计算前进行数据处理
            jwd = jwd.replace("E", "").replace("N", "").replace(":", "").replace("：", "");
            double d = Double.parseDouble(jwd.split("°")[0]), m = 0, s = 0;

            //不同单位的分，可扩展
            if (jwd.contains("′")) //正常的′
                m = Double.parseDouble(jwd.split("°")[1].split("′")[0]);
            else if (jwd.contains("'")) //特殊的'
                m = Double.parseDouble(jwd.split("°")[1].split("'")[0]);

            //不同单位的秒，可扩展
            if (jwd.contains("″")) //正常的″
                //有时候没有分 如：112°10.25″
                s = jwd.contains("′") ? Double.parseDouble(jwd.split("′")[1].split("″")[0]) : Double.parseDouble(jwd.split("°")[1].split("″")[0]);
            else if (jwd.contains("''")) //特殊的''
                //有时候没有分 如：112°10.25''
                s = jwd.contains("'") ? Double.parseDouble(jwd.split("'")[1].split("''")[0]) : Double.parseDouble(jwd.split("°")[1].split("''")[0]);

            jwd = String.valueOf(d + m / 60 + s / 60 / 60);//计算并转换为 string

            //使用BigDecimal进行加减乘除
            /*BigDecimal bd = new BigDecimal("60");
            BigDecimal d = new BigDecimal(jwd.contains("°")?jwd.split("°")[0]:"0");
            BigDecimal m = new BigDecimal(jwd.contains("′")?jwd.split("°")[1].split("′")[0]:"0");
            BigDecimal s = new BigDecimal(jwd.contains("″")?jwd.split("′")[1].split("″")[0]:"0");

            //divide相除可能会报错（无限循环小数），要设置保留小数点
            jwd = String.valueOf(d.add(m.divide(bd,6,BigDecimal.ROUND_HALF_UP)
                    .add(s.divide(bd.multiply(bd),6,BigDecimal.ROUND_HALF_UP))));*/
        }

        return jwd;
    }


    /**
     * 度分 转度
     * 十进制经纬度转换 ddd°mm.mmmm' 转 ddd.ddddd°
     * 如：112°30.4128' = 112.50688
     *
     * @param jwd
     * @return
     */
    public static String DmTurnD(String jwd) {
        if (StringUtils.hasText(jwd) && (jwd.contains("°") && jwd.contains("'"))) {//如果不为空并且存在度、分单位
            double d = Double.parseDouble(jwd.split("°")[0]),
                    m = Double.parseDouble(jwd.split("°")[1].split("'")[0]) / 60;

            jwd = String.valueOf(d + m);
        } else if (StringUtils.hasText(jwd) && (jwd.contains("°"))) {//如果不为空并且存在度单位
            double d = Double.parseDouble(jwd.split("°")[0]),
                    m = Double.parseDouble(jwd.split("°")[1]) / 60;

            jwd = String.valueOf(d + m);
        }

        return jwd;
    }

}
