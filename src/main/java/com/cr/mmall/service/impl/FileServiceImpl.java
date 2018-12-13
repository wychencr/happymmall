package com.cr.mmall.service.impl;

import com.cr.mmall.service.IFileService;
import com.cr.mmall.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        // 实际的文件名
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        // 扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 上传的文件名(避免重复)
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{}，上传的路径:{}，新文件名:{}", fileName, path, uploadFileName);

        // 创建目录，如果文件路径不存在则赋予可写权限，并创建目录
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
            logger.info("文件路径不存在，创建目录");
        }
        // 创建文件
        File targetFile = new File(path, uploadFileName);

        try {
            // 相当于将原始文件复制并重命名到目标文件夹target-happymmall-upload
            file.transferTo(targetFile);
            logger.info("文件开始上传");
            // 文件开始上传

            boolean result = FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            if (!result) {
                throw new IOException("异常-上传失败");
            }
            logger.info("已经上传到ftp服务器上");
            // 已经上传到ftp服务器上

            targetFile.delete();
            logger.info("将目标文件删除");

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        logger.info("目标文件名称：{}", targetFile.getName());
        return targetFile.getName();
    }
}