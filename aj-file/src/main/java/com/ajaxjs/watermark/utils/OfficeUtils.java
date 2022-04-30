package com.ajaxjs.watermark.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.ZipHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.watermark.type.OfficeTypeEnum;

public class OfficeUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(OfficeUtils.class);

	private static String blipId = "bingo";

	private Document document;

	private FileInputStream fileInputStream;

	/**
	 * 特殊字段， 用于某些时候知道该文件是不是从非x版本转过来的
	 */
	private boolean ooxml = true;

	public OfficeUtils() {
	}

	private boolean createDocument(String path) throws FileNotFoundException, DocumentException {
		SAXReader reader = new SAXReader();

		try {
			fileInputStream = new FileInputStream(path);
			document = reader.read(fileInputStream);
		} catch (FileNotFoundException e) {
			LOGGER.warning("File \"" + path + "\" not found.");
			throw e;
		} catch (DocumentException e) {
			LOGGER.warning("\"" + path + "\" can no be read as a xml document.");
			throw e;
		}

		return true;
	}

	private XMLUtils.Pair[] getSuggestionByImageType(String imageType) {
		XMLUtils.Pair pairs[] = new XMLUtils.Pair[2];

		// 设置默认值，避免系统未设置上传的图片
		// String defaultType = "jpeg";
		pairs[0] = XMLUtils.prop("Extension", "jpeg");
		pairs[1] = XMLUtils.prop("ContentType", "image/jpeg");

		switch (imageType.toUpperCase()) {
		case "EMF":
			pairs[0] = XMLUtils.prop("Extension", "emf");
			pairs[1] = XMLUtils.prop("ContentType", "image/x-emf");
			break;
		case "WMF":
			pairs[0] = XMLUtils.prop("Extension", "wmf");
			pairs[1] = XMLUtils.prop("ContentType", "image/x-wmf");
			break;
		case "PICT":
			pairs[0] = XMLUtils.prop("Extension", "pict");
			pairs[1] = XMLUtils.prop("ContentType", "image/pict");
			break;
		case "JPEG":
			pairs[0] = XMLUtils.prop("Extension", "jpeg");
			pairs[1] = XMLUtils.prop("ContentType", "image/jpeg");
			break;
		case "PNG":
			pairs[0] = XMLUtils.prop("Extension", "png");
			pairs[1] = XMLUtils.prop("ContentType", "image/png");
			break;
		case "DIB":
			pairs[0] = XMLUtils.prop("Extension", "dib");
			pairs[1] = XMLUtils.prop("ContentType", "image/dib");
			break;
		case "GIF":
			pairs[0] = XMLUtils.prop("Extension", "gif");
			pairs[1] = XMLUtils.prop("ContentType", "image/gif");
			break;
		case "TIFF":
			pairs[0] = XMLUtils.prop("Extension", "tiff");
			pairs[1] = XMLUtils.prop("ContentType", "image/tiff");
			break;
		case "EPS":
			pairs[0] = XMLUtils.prop("Extension", "eps");
			pairs[1] = XMLUtils.prop("ContentType", "image/x-eps");
			break;
		case "BMP":
			pairs[0] = XMLUtils.prop("Extension", "bmp");
			pairs[1] = XMLUtils.prop("ContentType", "image/x-ms-bmp");
			break;
		case "WPG":
			pairs[0] = XMLUtils.prop("Extension", "wpg");
			pairs[1] = XMLUtils.prop("ContentType", "image/x-wpg");
			break;
		}

		return pairs;
	}

	private boolean writeDocument(String path) throws IOException {
		XMLWriter writer = null;

		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			LOGGER.warning("IOException was throw when write stream to \"" + path + "\".");
			throw e;
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				LOGGER.info("fileInputStream can not be close.");
			}
		}

		return true;
	}

	public boolean officeWatermark(InputStream in, OfficeTypeEnum type, String picPath, String optPath, OutputStream out) {
		String officePath = optPath + "srcFile." + type.getType();
		String desPath = optPath + "watermark." + type.getType();

		if (!desPath.endsWith("x"))
			desPath = desPath + "x";

		try {
			FileUtils.inputStream2File(in, officePath);
		} catch (IOException e) {
			LOGGER.warning("Failed transform inputStream to file.");
			return false;
		}

		if (officeWatermark(officePath, picPath, desPath, optPath, type) && FileUtils.file2OutputStream(desPath, out))
			return true;

		return false;
	}

	/**
	 * @param officePath office 文件的所在位置
	 * @param picPath    指定水印的地址
	 * @param desPath    指定添加完水印之后的文件的地址
	 * @param optPath    记住，对于每一个文档的处理只能使用一个文件夹，不可重复，将以使用随机，之后将目录删除
	 * @param type       office 文件类型
	 * @return true 成功添加水印 false 添加水印失败
	 */
	public boolean officeWatermark(String officePath, String picPath, String desPath, String optPath, OfficeTypeEnum type) {
		if (!desPath.endsWith(".docx") && !desPath.endsWith(".xlsx") && !desPath.endsWith(".pptx")) {
			LOGGER.warning("Your output dir<desPath> has a wrong format.");
			return false;
		}

		String cmd = null;

		switch (type) {
		case DOC:
			cmd = "soffice --convert-to docx " + officePath + " --outdir " + optPath;

			try {
				Process exec = Runtime.getRuntime().exec(cmd);
				LOGGER.info("The command \"" + cmd + "\" was exec.");
				int i = exec.waitFor();
			} catch (Exception e) {
				LOGGER.warning("Exception was throw when conver doc to docx.");
				return false;
			}

			officePath = optPath + officePath.substring(officePath.lastIndexOf('/') + 1, officePath.lastIndexOf('.')) + ".docx";
		case DOCX:
			try {
				wordWatermark(officePath, picPath, desPath, optPath);
			} catch (Exception e) {
				LOGGER.warning("There are something wrong when adding watermark to word.\"" + officePath + "\"");
				return false;
			}

			break;
		case PPT:
			cmd = "soffice --convert-to pptx " + officePath + " --outdir " + optPath;

			try {
				Process exec = Runtime.getRuntime().exec(cmd);
				LOGGER.info("The command \"" + cmd + "\" was exec.");
				int i = exec.waitFor();
			} catch (Exception e) {
				LOGGER.warning("Exception was throw when conver ppt to pptx.");
				return false;
			}

			officePath = optPath + officePath.substring(officePath.lastIndexOf('/') + 1, officePath.lastIndexOf('.')) + ".pptx";
		case PPTX:
			try {
				pptWatermark(officePath, picPath, desPath, optPath);
			} catch (Exception e) {
				LOGGER.warning("There are something wrong when adding watermark to ppt.\"" + officePath + "\"");
				return false;
			}

			break;
		case XLS:
			cmd = "soffice --convert-to xlsx " + officePath + " --outdir " + optPath;
			try {
				Process exec = Runtime.getRuntime().exec(cmd);
				LOGGER.info("The command \"" + cmd + "\" was exec.");
				int i = exec.waitFor();
			} catch (Exception e) {
				LOGGER.warning("Exception was throw when conver xls to xlsx.");
				return false;
			}

			this.ooxml = false;
			officePath = optPath + officePath.substring(officePath.lastIndexOf('/') + 1, officePath.lastIndexOf('.')) + ".xlsx";
		case XLSX:
			try {
				excelWatermark(officePath, picPath, desPath, optPath);
			} catch (Exception e) {
				LOGGER.warning("There are something wrong when adding watermark to excel.\"" + officePath + "\"");
				return false;
			}

			break;
		default:
			return true;
		}

		return true;
	}

	/**
	 * 实现在 word 上添加一张水印图片, 仅支持 ooxml 版本
	 * 
	 * @param wordPath word 路径地址
	 * @param picPath  水印图片的路径地址
	 * @param desPath  添加水印之后的 word 的目标地址
	 * @param optPath  操作 word 添加水印的文件夹，本文件夹保持干净，无其他文件
	 * @return true 成功添加水印 false 添加水印失败
	 */
	private boolean wordWatermark(String wordPath, String picPath, String desPath, String optPath) throws IOException, InterruptedException, DocumentException {
		LOGGER.info("The directory of this time to handle WORD is \"" + optPath + "\"");
		FileHelper.mkDir(optPath);
		String imageType = picPath.substring(picPath.lastIndexOf('.') + 1);

		if ("jpg".equals(imageType))
			imageType = "jpeg";

		// 对 word 进行解压
		ZipHelper.unzip(optPath, wordPath);
		FileHelper.mkDir(optPath + "word/media");
		FileHelper.mkDir(optPath + "word/_rels");

		LOGGER.info("updating [Content_Types].xml to add picture support and header link.");
		// 修改 [Content_Types].xml 文件
		String target = optPath + "[Content_Types].xml";

		if (createDocument(target)) {
			Element ctRootElement = document.getRootElement();
			XMLUtils.addElementToElement(ctRootElement, "Default", getSuggestionByImageType(imageType));
			XMLUtils.addElementToElement(ctRootElement, "Override", XMLUtils.prop("PartName", "/word/bingo.xml"),
					XMLUtils.prop("ContentType", "application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml"));
			writeDocument(target);
		} else {
			FileHelper.delete(optPath);
			return false;
		}

//        修改 word\document.xml 文件
		LOGGER.info("updating document.xml to add header reference to show water picture.");
		target = optPath + "word/document.xml";

		if (createDocument(target)) {
			Element dxRootElement = document.getRootElement(), body = dxRootElement.element("body"), sectPr = body.element("sectPr");
			List<Element> headerReference = sectPr.elements("headerReference");
			boolean hasDefault = false;

			for (Element element : headerReference) {
				if ("default".equals(element.attributeValue("type"))) {
					element.setAttributeValue("id", blipId);
					hasDefault = true;
				}
			}

			if (!hasDefault) {
				Element element = sectPr.addElement("w:headerReference");
				element.addAttribute("w:type", "default");
				element.addAttribute("r:id", blipId);
			}

			writeDocument(target);
		} else
			return false;

        // 修改 document.xml.rels 文件
		LOGGER.info("updating document.xml.rels to add header link.");
		target = optPath + "word/_rels/document.xml.rels";

		if (createDocument(target)) {
			Element dxrRootElement = document.getRootElement();
			XMLUtils.addElementToElement(dxrRootElement, "Relationship", XMLUtils.prop("Id", blipId),
					XMLUtils.prop("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/header"), XMLUtils.prop("Target", "bingo.xml"));
			writeDocument(target);
		} else
			return false;

		FileHelper.saveText(optPath + "word/bingo.xml", Helper.getHeaderXml(blipId));
		FileHelper.saveText(optPath + "word/_rels/bingo.xml.rels", Helper.getHeadXmlRels(blipId, "bingo." + imageType));
		FileHelper.copy(picPath, optPath + "word/media/bingo." + imageType, true);
		ZipHelper.zip(optPath, desPath);

		return true;
	}

	/**
	 * 实现在 excel 上添加一张水印图片, 仅支持 ooxml 版本
	 * 
	 * @param excelPath excel 路径地址
	 * @param picPath   水印图片的路径地址
	 * @param desPath   添加水印之后的 word 的目标地址
	 * @param optPath   操作 word 添加水印的文件夹，本文件夹保持干净，无其他文件
	 * @return true 成功添加水印 false 添加水印失败
	 */
	private boolean excelWatermark(String excelPath, String picPath, String desPath, String optPath) throws IOException, InterruptedException, DocumentException {
		LOGGER.info("The directory of this time to handle WORD is \"" + optPath + "\"");
		FileHelper.mkDir(optPath);
		String imageType = picPath.substring(picPath.lastIndexOf('.') + 1);

		if ("jpg".equals(imageType))
			imageType = "jpeg";

		// 对 Excel 进行解压 -> 成功
		ZipHelper.unzip(optPath, excelPath);
		FileHelper.mkDir(optPath + "xl/media");
		FileHelper.mkDir(optPath + "xl/drawings");
		FileHelper.mkDir(optPath + "xl/drawings/_rels");
		FileHelper.mkDir(optPath + "xl/worksheets/_rels");

		LOGGER.info("updating [Content_Types].xml to add vml support and image support.");
		// [Content_Types].xml 添加两个 <Default/> 标签 -> 成功
		String target = optPath + "[Content_Types].xml";

		if (createDocument(target)) {
			Element ctRootElement = document.getRootElement();
			XMLUtils.addElementToElement(ctRootElement, "Default", XMLUtils.prop("Extension", "vml"),
					XMLUtils.prop("ContentType", "application/vnd.openxmlformats-officedocument.vmlDrawing"));
			XMLUtils.addElementToElement(ctRootElement, "Default", getSuggestionByImageType(imageType));
			writeDocument(target);
		}

		int next = 1;

		while (next != -1) {
//       SheetX.xml 添加<header>及其子标签<oddHeader>、<legacyDrawingHF>共三个标签
			target = optPath + "xl/worksheets/sheet" + next + ".xml";

			try {
				createDocument(target);
			} catch (FileNotFoundException | DocumentException e) {
				next = -1;
				LOGGER.info("Total " + (next - 1) + " sheets file has been updated.");
				break;
			}

			Element sxRootElement = document.getRootElement();
			XMLUtils.addElementToElement(sxRootElement, "headerFooter");
			Element sxHeaderFooter = sxRootElement.element("headerFooter");
			XMLUtils.addElementToElement(sxHeaderFooter, "oddHeader", "&C&G");

//            XMLUtils.addElementToElement(sxRootElement, "legacyDrawingHF", XMLUtils.prop("r:id", blipId));
			if (sxRootElement.element("legacyDrawingHF") != null) {
				Element legacyDrawingHF = sxRootElement.element("legacyDrawingHF");
				legacyDrawingHF.setAttributeValue("id", blipId);
			} else {
				Element legacyDrawingHF = sxRootElement.addElement("legacyDrawingHF");
				legacyDrawingHF.addAttribute("r:id", blipId);
			}

			sxRootElement.element("sheetViews").element("sheetView").addAttribute("view", "pageLayout");
			writeDocument(target);

//           SheetX.xml.rels 中添加 <Relationship>
			target = optPath + "xl/worksheets/_rels/sheet" + next + ".xml.rels";

			// 可以打开说明该 EXCEL 存在 RELS 文件，不需要进行手动创建文件，如果没有，就需要手动创建
			try {
				createDocument(target);
				Element sxrRootElement = document.getRootElement();
				XMLUtils.addElementToElement(sxrRootElement, "Relationship", XMLUtils.prop("Id", blipId),
						XMLUtils.prop("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing"), XMLUtils.prop("Target", "../drawings/bingo.vml"));
				writeDocument(target);
			} catch (Exception e) {
				FileHelper.saveText(target, Helper.getSheetXmlRels("bingo", "bingo.vml"));
//				FileUtils.string2File(Helper.getSheetXmlRels("bingo", "bingo.vml"), target);
			}

			next++;
		}
		
		// 复制图片
		FileHelper.copy(picPath, optPath + "xl/media/bingo." + imageType, true);
		// 写入 vmlDrawing1.vml 文件
		FileHelper.saveText(optPath + "xl/drawings/bingo.vml", Helper.getDrawingVml(blipId, this.ooxml));
		// 写入 vmlDrawing1.vml.rels 文件
		FileHelper.saveText(optPath + "xl/drawings/_rels/bingo.vml.rels", Helper.getDrawingVmlRels(blipId, "bingo." + imageType));
		// 压缩为目标文件
		ZipHelper.zip(optPath, desPath);

		return true;
	}

	/**
	 * 实现在ppt上添加一张水印图片, 仅支持ooxml版本
	 * 
	 * @param pptPath ppt路径地址
	 * @param picPath 水印图片的路径地址
	 * @param desPath 添加水印之后的word的目标地址
	 * @param optPath 操作word添加水印的文件夹，本文件夹保持干净，无其他文件
	 * @return true 成功添加水印 false添加水印失败
	 */
	private Boolean pptWatermark(String pptPath, String picPath, String desPath, String optPath) throws DocumentException, IOException, InterruptedException {
		LOGGER.info("The directory of this time to handle WORD is \"" + optPath + "\"");
		FileHelper.mkDir(optPath);
		String imageType = picPath.substring(picPath.lastIndexOf('.') + 1);

		if ("jpg".equals(imageType))
			imageType = "jpeg";

		// 对 word 进行解压
		ZipHelper.unzip(optPath, pptPath);
		FileHelper.mkDir(optPath + "ppt/media");

		LOGGER.info("updating [Content_Types].xml to support image.");
//        [Content_Types].xml 添加图片支持
		String target = optPath + "[Content_Types].xml";

		if (createDocument(target)) {
			Element ctRootElement = document.getRootElement();
			XMLUtils.addElementToElement(ctRootElement, "Default", getSuggestionByImageType(imageType));
			writeDocument(target);
		}

//        ppt\slideMasters\slideMaster1.xml 添加pic标签
		LOGGER.info("updating slideMaster1.xml to add picture link.");
		target = optPath + "ppt/slideMasters/slideMaster1.xml";

		if (createDocument(target)) {
			Element smxRootElement = document.getRootElement();
			Element spTree = smxRootElement.element("cSld").element("spTree");
			Document picDocument = DocumentHelper.parseText(Helper.getPicText("bingo"));
			Element picElement = picDocument.getRootElement();
			spTree.add(picElement);
			writeDocument(target);
		}

//        ppt\slideMasters\_rels\slideMaster1.xml.rels 添加 图片链接
		LOGGER.info("updating slideMaster1.xml.rels to create image link.");
		target = optPath + "ppt/slideMasters/_rels/slideMaster1.xml.rels";

		if (createDocument(target)) {
			Element smxrRootElement = document.getRootElement();
			XMLUtils.addElementToElement(smxrRootElement, "Relationship", XMLUtils.prop("Id", "bingo"),
					XMLUtils.prop("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"), XMLUtils.prop("Target", "../media/bingo." + imageType));
			writeDocument(target);
		}

		// 复制图片
		FileHelper.copy(picPath, optPath + "ppt/media/bingo." + imageType, true);
		ZipHelper.zip(optPath, desPath);

		return true;
	}
}