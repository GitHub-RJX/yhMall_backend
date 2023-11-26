package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.AttachFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachFileService extends IService<AttachFile> {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 删除文件
     */
    void deleteFile(String fileName);
}
