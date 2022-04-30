package com.ajaxjs.watermark.type;

/**
 * 图片类型
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public enum PictureTypeEnum {
    JPEG("jpeg"),
    PNG("png"),
    EMF("emf"),
    WMF("wmf"),
    PICT("pict"),
    DIB("dib"),
    GIF("gif"),
    TIFF("tiff"),
    EPS("eps"),
    BMP("bmp"),
    WPG("wpg");

    private String type;
    
    PictureTypeEnum(String type){
        this.setType(type);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
