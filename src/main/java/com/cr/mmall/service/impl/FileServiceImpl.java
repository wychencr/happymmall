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
        }
        // 创建文件
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            // 文件已经上传成功

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 已经上传到ftp服务器上

            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
