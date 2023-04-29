package com.ajaxjs.watermark.utils;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Helper {
	/* -------------------WORD--------------- */

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String getHeaderXml(String id) {
		// " <v:imagedata r:id=\"" + id + "\" o:title=\"sola\" gain=\"19661f\"
		// blacklevel=\"22938f\"/>\n"
		// 使用以上字段可以显示出冲蚀效果
		// @formatter:off
	    return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
	            "<w:hdr xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" \n" +
	            "    xmlns:cx=\"http://schemas.microsoft.com/office/drawing/2014/chartex\" \n" +
	            "    xmlns:cx1=\"http://schemas.microsoft.com/office/drawing/2015/9/8/chartex\" \n" +
	            "    xmlns:cx2=\"http://schemas.microsoft.com/office/drawing/2015/10/21/chartex\" \n" +
	            "    xmlns:cx3=\"http://schemas.microsoft.com/office/drawing/2016/5/9/chartex\" \n" +
	            "    xmlns:cx4=\"http://schemas.microsoft.com/office/drawing/2016/5/10/chartex\" \n" +
	            "    xmlns:cx5=\"http://schemas.microsoft.com/office/drawing/2016/5/11/chartex\" \n" +
	            "    xmlns:cx6=\"http://schemas.microsoft.com/office/drawing/2016/5/12/chartex\" \n" +
	            "    xmlns:cx7=\"http://schemas.microsoft.com/office/drawing/2016/5/13/chartex\" \n" +
	            "    xmlns:cx8=\"http://schemas.microsoft.com/office/drawing/2016/5/14/chartex\" \n" +
	            "    xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" \n" +
	            "    xmlns:aink=\"http://schemas.microsoft.com/office/drawing/2016/ink\" \n" +
	            "    xmlns:am3d=\"http://schemas.microsoft.com/office/drawing/2017/model3d\" \n" +
	            "    xmlns:o=\"urn:schemas-microsoft-com:office:office\" \n" +
	            "    xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \n" +
	            "    xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" \n" +
	            "    xmlns:v=\"urn:schemas-microsoft-com:vml\" \n" +
	            "    xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" \n" +
	            "    xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" \n" +
	            "    xmlns:w10=\"urn:schemas-microsoft-com:office:word\" \n" +
	            "    xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" \n" +
	            "    xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" \n" +
	            "    xmlns:w15=\"http://schemas.microsoft.com/office/word/2012/wordml\" \n" +
	            "    xmlns:w16cid=\"http://schemas.microsoft.com/office/word/2016/wordml/cid\" \n" +
	            "    xmlns:w16se=\"http://schemas.microsoft.com/office/word/2015/wordml/symex\" \n" +
	            "    xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" \n" +
	            "    xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" \n" +
	            "    xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" \n" +
	            "    xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 w15 w16se w16cid wp14\">\n" +
	            "    <w:p w:rsidR=\"00B3486B\" w:rsidRDefault=\"00B1706E\">\n" +
	            "        <w:bookmarkStart w:id=\"0\" w:name=\"_GoBack\"/>\n" +
	            "        <w:r>\n" +
	            "            <w:rPr>\n" +
	            "                <w:noProof/>\n" +
	            "            </w:rPr>\n" +
	            "            <w:pict w14:anchorId=\"2E84FB04\">\n" +
	            "                <v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\">\n" +
	            "                    <v:stroke joinstyle=\"miter\"/>\n" +
	            "                    <v:formulas>\n" +
	            "                        <v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>\n" +
	            "                        <v:f eqn=\"sum @0 1 0\"/>\n" +
	            "                        <v:f eqn=\"sum 0 0 @1\"/>\n" +
	            "                        <v:f eqn=\"prod @2 1 2\"/>\n" +
	            "                        <v:f eqn=\"prod @3 21600 pixelWidth\"/>\n" +
	            "                        <v:f eqn=\"prod @3 21600 pixelHeight\"/>\n" +
	            "                        <v:f eqn=\"sum @0 0 1\"/>\n" +
	            "                        <v:f eqn=\"prod @6 1 2\"/>\n" +
	            "                        <v:f eqn=\"prod @7 21600 pixelWidth\"/>\n" +
	            "                        <v:f eqn=\"sum @8 21600 0\"/>\n" +
	            "                        <v:f eqn=\"prod @7 21600 pixelHeight\"/>\n" +
	            "                        <v:f eqn=\"sum @10 21600 0\"/>\n" +
	            "                    </v:formulas>\n" +
	            "                    <v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/>\n" +
	            "                    <o:lock v:ext=\"edit\" aspectratio=\"t\"/>\n" +
	            "                </v:shapetype>\n" +
	            "                <v:shape id=\"WordPictureWatermark27718611\" o:spid=\"_x0000_s3081\" type=\"#_x0000_t75\" style=\"position:absolute;margin-left:-89.85pt;margin-top:-1in;width:595.3pt;height:841.5pt;z-index:-251658752;mso-position-horizontal:absolute;mso-position-horizontal-relative:margin;mso-position-vertical-relative:margin\" o:allowincell=\"f\">\n" +
	            "                    <v:imagedata r:id=\"" + id + "\" o:title=\"sola\"/>\n" +
	            "                    <w10:wrap anchorx=\"margin\" anchory=\"margin\"/>\n" +
	            "                </v:shape>\n" +
	            "            </w:pict>\n" +
	            "        </w:r>\n" +
	            "        <w:bookmarkEnd w:id=\"0\"/>\n" +
	            "    </w:p>\n" +
	            "</w:hdr>";
	     // @formatter:on
	}

	public static String getHeadXmlRels(String id, String picName) {
		// @formatter:off
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
                "    <Relationship Id=\"" + id + "\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\" Target=\"media/" + picName + "\"/>\n" +
                "</Relationships>";
        // @formatter:on
	}

	/* -------------------PPT--------------- */

	public static String getPicText(String id) {
		return
		// @formatter:off
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
        "<p:pic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" \n" +
        "    xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \n" +
        "    xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" +
        "    <p:nvPicPr>\n" +
        "        <p:cNvPr id=\"8\" name=\"sola\">\n" +
        "            <a:extLst>\n" +
        "                <a:ext uri=\"{FF2B5EF4-FFF2-40B4-BE49-F238E27FC236}\">\n" +
        "                    <a16:creationId xmlns:a16=\"http://schemas.microsoft.com/office/drawing/2014/main\" id=\"{2CCA6737-C83F-40C7-B853-9C2AC5EB23E5}\"/>\n" +
        "                </a:ext>\n" +
        "            </a:extLst>\n" +
        "        </p:cNvPr>\n" +
        "        <p:cNvPicPr>\n" +
        "            <a:picLocks noChangeAspect=\"1\"/>\n" +
        "        </p:cNvPicPr>\n" +
        "        <p:nvPr userDrawn=\"1\"/>\n" +
        "    </p:nvPicPr>\n" +
        "    <p:blipFill>\n" +
        "        <a:blip r:embed=\"" + id + "\">\n" +
        "            <a:extLst>\n" +
        "                <a:ext uri=\"{28A0092B-C50C-407E-A947-70E740481C1C}\">\n" +
        "                    <a14:useLocalDpi xmlns:a14=\"http://schemas.microsoft.com/office/drawing/2010/main\" val=\"0\"/>\n" +
        "                </a:ext>\n" +
        "            </a:extLst>\n" +
        "        </a:blip>\n" +
        "        <a:stretch>\n" +
        "            <a:fillRect/>\n" +
        "        </a:stretch>\n" +
        "    </p:blipFill>\n" +
        "    <p:spPr>\n" +
        "        <a:xfrm>\n" +
        "           <a:off x=\"33679\" y=\"0\"/>\n" +
        "           <a:ext cx=\"12124641\" cy=\"6858000\"/>\n" +
        "        </a:xfrm>\n" +
        "        <a:prstGeom prst=\"rect\">\n" +
        "            <a:avLst/>\n" +
        "        </a:prstGeom>\n" +
        "    </p:spPr>\n" +
        "</p:pic>";
        // @formatter:on
	}

	/* -------------------Excel--------------- */

	/**
	 * 获取VMl文件内容
	 * 
	 * @param id 自定义 ID，用于显示图片,保持与下面的方法一致
	 * @return
	 */
	public static String getDrawingVml(String id, boolean ooxml) {
		return
		// @formatter:off
		"<xml xmlns:v=\"urn:schemas-microsoft-com:vml\"\n" +
        " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
        " xmlns:x=\"urn:schemas-microsoft-com:office:excel\">\n" +
        " <o:shapelayout v:ext=\"edit\">\n" +
        "  <o:idmap v:ext=\"edit\" data=\"1\"/>\n" +
        " </o:shapelayout><v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\"\n" +
        "  o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\">\n" +
        "  <v:stroke joinstyle=\"miter\"/>\n" +
        "  <v:formulas>\n" +
        "   <v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>\n" +
        "   <v:f eqn=\"sum @0 1 0\"/>\n" +
        "   <v:f eqn=\"sum 0 0 @1\"/>\n" +
        "   <v:f eqn=\"prod @2 1 2\"/>\n" +
        "   <v:f eqn=\"prod @3 21600 pixelWidth\"/>\n" +
        "   <v:f eqn=\"prod @3 21600 pixelHeight\"/>\n" +
        "   <v:f eqn=\"sum @0 0 1\"/>\n" +
        "   <v:f eqn=\"prod @6 1 2\"/>\n" +
        "   <v:f eqn=\"prod @7 21600 pixelWidth\"/>\n" +
        "   <v:f eqn=\"sum @8 21600 0\"/>\n" +
        "   <v:f eqn=\"prod @7 21600 pixelHeight\"/>\n" +
        "   <v:f eqn=\"sum @10 21600 0\"/>\n" +
        "  </v:formulas>\n" +
        "  <v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/>\n" +
        "  <o:lock v:ext=\"edit\" aspectratio=\"t\"/>\n" +
        " </v:shapetype><v:shape id=\"CH\" o:spid=\"_x0000_s1025\" type=\"#_x0000_t75\"\n" +
        "  style='position:absolute;margin-left:0;margin-top:0;width:" + ((ooxml == true) ? 498 : 513) + "pt;height:" + ((ooxml == true) ? 874.5 : 815) + "pt;\n" +
        "  z-index:1'>\n" +
        "  <v:imagedata o:relid=\"" + id + "\" o:title=\"sola\"/>\n" +
        "  <o:lock v:ext=\"edit\" rotation=\"t\"/>\n" +
        " </v:shape></xml>";
        // @formatter:on
	}

	/**
	 * 获取 VML 文件的关联文件
	 * 
	 * @param id      自定义 ID，用于关联显示的图片,保持与上面的方法一致
	 * @param imgName 上传上去的图片名称，保持于上传目录相同
	 * @return
	 */
	public static String getDrawingVmlRels(String id, String imgName) {
		return
		// @formatter:off
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
        "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
        "    <Relationship Id=\"" + id + "\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\" Target=\"../media/" + imgName + "\"/>\n" +
        "</Relationships>";
		// @formatter:on
	}

	/**
	 * 
	 * @param id
	 * @param vmlName
	 * @return
	 */
	public static String getSheetXmlRels(String id, String vmlName) {
		return
		// @formatter:off
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
        "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n" +
        "    <Relationship Id=\"" + id + "\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing\" Target=\"../drawings/" + vmlName + "\"/>\n" +
        "</Relationships>";
        // @formatter:on
	}
}
