package com.ajaxjs.watermark.type;

/**
 * Office 文件类型
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public enum OfficeTypeEnum {
    DOC("doc", 798, 1128),
    DOCX("docx", 798, 1128),
    XLS("xls", 683, 1084),
    XLSX("xlsx", 664, 1166),
    PPT("ppt", 1280, 724),
    PPTX("pptx", 1280, 724);

	/**
	 * 类型
	 */
    private String type;
    
    /**
     * 宽
     */
    private int width;
    
    /**
     * 高
     */
    private int height;
    
    /**
     * 
     * @param type
     * @param width
     * @param height
     */
    OfficeTypeEnum(String type, int width, int height){
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
