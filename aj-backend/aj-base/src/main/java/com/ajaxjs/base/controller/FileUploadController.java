package com.ajaxjs.base.controller;

import com.ajaxjs.base.model.UploadResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/upload")
public interface FileUploadController {
    /**
     * 服务器本地上传
     *
     * @param file     文件
     * @param req      请求对象
     * @param isRename 是否改名为 uuid 的名称
     * @return 上传结果
     */
    @PostMapping("/local")
    UploadResult local(@RequestParam("file") MultipartFile file, HttpServletRequest req, @RequestParam(required = false) boolean isRename);

    /**
     * 阿里云 OSS 上传
     *
     * @param file     文件
     * @param req      请求对象
     * @param isRename 是否改名为 uuid 的名称
     * @return 上传结果
     */
    @PostMapping("/oss")
    UploadResult oss(@RequestParam("file") MultipartFile file, HttpServletRequest req, @RequestParam(required = false) boolean isRename);

    /**
     * 网易云 NSO 上传
     *
     * @param file     文件
     * @param req      请求对象
     * @param isRename 是否改名为 uuid 的名称
     * @return 上传结果
     */
    @PostMapping("/nso")
    UploadResult nso(@RequestParam("file") MultipartFile file, HttpServletRequest req, @RequestParam(required = false) boolean isRename);
}
