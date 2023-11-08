package com.ajaxjs.base.service.file_upload;

import com.ajaxjs.base.controller.FileUploadController;
import com.ajaxjs.base.model.UploadResult;
import com.ajaxjs.data.util.SnowflakeId;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class FileUploadService implements FileUploadController {
    private static final LogHelper LOGGER = LogHelper.getLog(FileUploadService.class);

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
     * 单个文件上传最大字节。
     * 默认 1 MB
     */
    @Value("${FileUpload.maxSingleFileSize: 1}")
    private int maxSingleFileSize;

    /**
     * 允许上传的文件类型，如果为空数组则不限制上传类型。格式如 {".jpg", ".png", ...}
     */
    @Value("${FileUpload.allowExtFilenames}")
    private String[] allowExtFilenames;

    /**
     * 上传之后访问的 url 前缀
     */
    @Value("${S3Storage.LocalStorage.absoluteSavePath}")
    private String absoluteSavePath;

    @Override
    public UploadResult local(MultipartFile file, HttpServletRequest req, boolean isRename) {
        String filename = getFileName(file, isRename);
        String path = StringUtils.hasText(absoluteSavePath) ? absoluteSavePath + File.separator + saveFolder : WebHelper.mapPath(req, saveFolder);
        File dir = new File(path);

        if (!dir.exists()) dir.mkdirs();

        String diskPath = dir.getAbsolutePath() + File.separator + filename;
        LOGGER.info("文件上传，本地路径： " + diskPath);

        try {
            file.transferTo(new File(diskPath));// 写文件到服务器
        } catch (IllegalStateException | IOException e) {
            LOGGER.warning(e);
            throw new BusinessException(e.toString());
        }

        return getResult(filename);
    }

    @Autowired(required = false)
    OssUpload ossUpload;

    @Override
    public UploadResult oss(MultipartFile file, HttpServletRequest req, boolean isRename) {
        String filename = getFileName(file, isRename);

        try {
            ossUpload.upload(filename, file.getBytes());
        } catch (IllegalStateException | IOException e) {
            LOGGER.warning(e);
            throw new BusinessException(e.toString());
        }

        return getResult(filename);
    }


    @Autowired(required = false)
    NsoHttpUpload nsoHttpUpload;

    @Override
    public UploadResult nso(MultipartFile file, HttpServletRequest req, boolean isRename) {
        String filename = getFileName(file, isRename);

        try {
            nsoHttpUpload.upload(filename, file.getBytes());
        } catch (IllegalStateException | IOException e) {
            LOGGER.warning(e);
            throw new BusinessException(e.toString());
        }

        return getResult(filename);
    }


    private UploadResult getResult(String filename) {
        UploadResult result = new UploadResult();
        result.setFilename(filename);
        result.setUrl(FILE_URL_ROOT + "/" + filename); // 返回文件 url

        return result;
    }

    /**
     * 获取文件名
     */
    private String getFileName(MultipartFile file, boolean isRename) {
        String originalFilename = file.getOriginalFilename();
        return isRename || originalFilename == null ? getAutoName(originalFilename) : originalFilename; // 文件名
    }

    /**
     * 生成自动名称
     */
    public static String getAutoName(String originalFilename) {
        String ext = "";

        if (StringUtils.hasText(originalFilename)) {
            String[] arr = originalFilename.split("\\.");

            if (arr.length >= 2) ext = "." + arr[arr.length - 1];
//            else {
//                // 没有扩展名
//            }
        }

        return SnowflakeId.get() + ext;
    }

    /**
     * 文件校验
     */
    private void fileCheck(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("没有上传任何文件");

        if (file.getSize() > ((long) maxSingleFileSize * 1024 * 1024)) // MB 转字节
            throw new IOException("文件大小超过系统限制！");

        // 扩展名判断
        String[] arr = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

        if (arr.length >= 2 && !ObjectUtils.isEmpty(allowExtFilenames)) { // 有可能没有扩展名
            String ext = arr[arr.length - 1];
            boolean isFound = false;

            for (String _ext : allowExtFilenames) {
                if (_ext.equalsIgnoreCase(ext)) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound) throw new IllegalArgumentException(ext + " 上传类型不允许上传");
        }
    }
}
