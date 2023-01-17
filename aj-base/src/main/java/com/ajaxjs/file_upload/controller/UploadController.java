package com.ajaxjs.file_upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ajaxjs.file_upload.IFileUpload;
import com.ajaxjs.gateway.PassportFilter;
import com.ajaxjs.my_data_framework.BaseController;
import com.ajaxjs.sql.util.SnowflakeId;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.config.EasyConfig;

@RestController
@RequestMapping("/upload")
public class UploadController {
	/**
	 * 保存文件的目录
	 */
	@Value("${FileUpload.saveFolder}")
	private String saveFolder;

	/**
	 * 上传之后访问的 url 前缀
	 */
	@Value("${FileUpload.FILE_URL_ROOT}")
	private String FILE_URL_ROOT;

	/**
	 * 单次文件上传最大字节
	 */
//	private int maxTotalFileSize = 1024 * 5000;

	/**
	 * 单个文件上传最大字节
	 */
	@Value("${FileUpload.maxSingleFileSize}")
	private int maxSingleFileSize = 1; // 默认 1 MB;

	/**
	 * 允许上传的文件类型，如果为空数组则不限制上传类型。格式如 {".jpg", ".png", ...}
	 */
	@Value("${FileUpload.allowExtFilenames}")
	private String[] allowExtFilenames;

	/**
	 * 是否重命名
	 */
	@Value("${FileUpload.isRename}")
	private boolean isRename;

	@Autowired(required = false)
	IFileUpload fileUpload; // 默认是网易云 若 null 则使用本地上传

	@Autowired
	EasyConfig config;

	/**
	 * 测试文件上传的方法，测试使用的，可以废弃
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String uploadFileHandler(@RequestHeader(PassportFilter.CLIENT_ID) String clientId,
			@RequestHeader(PassportFilter.TOKEN) String token, @RequestParam("file") MultipartFile file,
			HttpServletRequest req) throws IOException {
		List<Map<String, Object>> list = config.getListMap("auth");
		Optional<Map<String, Object>> authO = list.stream().filter(map -> clientId.equals(map.get("clientId").toString())).findFirst();

		if (!authO.isPresent())
			throw new IllegalAccessError("非法客户端 id：" + clientId);

		Map<String, Object> auth = authO.get();

		if (!token.equals(auth.get("token")))
			throw new IllegalAccessError("非法客户端 token：" + token);

		fileCheck(file);

		String originalFilename = file.getOriginalFilename();
		String filename = isRename ? getAutoName(originalFilename) : originalFilename;

		if (fileUpload == null) {
			String newly = localFileUpload(filename, file, req);

			if (newly != null)
				return makeJson(filename, newly);
		} else if (fileUpload.upload(filename, file.getBytes())) {// TODO add folder
			String newly = FILE_URL_ROOT + "/" + filename;// 返回文件 url

			return makeJson(filename, newly);
		}

		return BaseController.jsonNoOk("上传失败");
	}

	private static String makeJson(String filename, String url) {
		String ext = String.format("\"filename\":\"%s\", \"url\": \"%s\"", filename, url);

		return BaseController.jsonOk_Extension("上传成功", ext);
	}

	/**
	 * 上传之后访问的 url 前缀
	 */
	@Value("${S3Storage.LocalStorage.absoluteSavePath}")
	private String absoluteSavePath;

	/**
	 * 本地上传文件
	 * 
	 * @param filename
	 * 
	 * @param file
	 * @param req
	 * @return
	 */
	public String localFileUpload(String filename, MultipartFile file, HttpServletRequest req) {
		String path;

		if (StringUtils.hasText(absoluteSavePath))
			path = absoluteSavePath + File.separator + saveFolder;
		else
			path = WebHelper.mappath(req, saveFolder);

		File dir = new File(path);

		if (!dir.exists())
			dir.mkdirs();

		try {
			file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));// 写文件到服务器
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return null;
		}

		return filename;
	}

	/**
	 * 生成自动名称
	 * 
	 * @param originalFilename
	 * @return
	 */
	public static String getAutoName(String originalFilename) {
		String[] arr = originalFilename.split("\\.");

		String ext = "";

		if (arr.length >= 2)
			ext = "." + arr[arr.length - 1];
		else {
			// 没有扩展名
		}

		return SnowflakeId.get() + ext;
	}

	/**
	 * 文件校验
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void fileCheck(MultipartFile file) throws IOException {
		if (file.isEmpty())
			throw new IllegalArgumentException("没有上传任何文件");

		if (file.getSize() > (maxSingleFileSize * 1024 * 1024)) // MB 转字节
			throw new IOException("文件大小超过系统限制！");

		// 扩展名判断
		String[] arr = file.getOriginalFilename().split("\\.");

		if (arr.length >= 2 && !ObjectUtils.isEmpty(allowExtFilenames)) { // 有可能没有扩展名
			String ext = arr[arr.length - 1];
			boolean isFound = false;

			for (String _ext : allowExtFilenames) {
				if (_ext.equalsIgnoreCase(ext)) {
					isFound = true;
					break;
				}
			}

			if (!isFound)
				throw new IllegalArgumentException(ext + " 上传类型不允许上传");
		}
	}
}
