package com.miniblog.controller;

import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg"};
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Value("${upload.base-url:http://localhost:9090/api/uploads}")
    private String baseUrl;

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return doUpload(file);
    }

    private Result<String> doUpload(MultipartFile file) {
        // 校验文件
        if (file == null || file.isEmpty()) {
            return Result.fail(400, "请选择文件");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return Result.fail(400, "文件大小不能超过 10MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.fail(400, "无效的文件名");
        }

        String ext = getFileExtension(originalFilename).toLowerCase();
        if (!isImageExtension(ext)) {
            return Result.fail(400, "仅支持图片格式：jpg、jpeg、png、gif、webp、svg");
        }

        // 按日期分目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dirPath = uploadPath + File.separator + datePath;

        // 生成唯一文件名
        String newFilename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path targetPath = Paths.get(dirPath, newFilename);

        try {
            // 创建目录
            Files.createDirectories(targetPath.getParent());
            // 写入文件
            file.transferTo(targetPath.toFile());

            // 返回访问 URL（用正斜杠）
            String url = baseUrl + "/" + datePath + "/" + newFilename;
            return Result.ok(url);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(500, "文件保存失败：" + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }

    private boolean isImageExtension(String ext) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(ext)) return true;
        }
        return false;
    }
}
