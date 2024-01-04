package com.yami.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.common.bean.Qiniu;
import com.yami.shop.common.util.ImgUploadUtil;
import com.yami.shop.common.util.Json;
import com.yami.shop.dao.AttachFileMapper;
import com.yami.shop.service.AttachFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

@Service
public class AttachFileServiceImpl extends ServiceImpl<AttachFileMapper, AttachFile> implements AttachFileService {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    private String accessKeyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Autowired
    private AttachFileMapper attachFileMapper;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private Qiniu qiniu;
    @Autowired
    private Auth auth;
    @Autowired
    private ImgUploadUtil imgUploadUtil;

    public final static String NORM_MONTH_PATTERN = "yyyy/MM/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file) throws IOException {
        String extName = FileUtil.extName(file.getOriginalFilename());
        String fileName = DateUtil.format(new Date(), NORM_MONTH_PATTERN) + IdUtil.simpleUUID() + "." + extName;
        if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
            // 本地文件上传
            AttachFile attachFile = new AttachFile();
            attachFile.setFilePath(fileName);
            attachFile.setFileSize(file.getBytes().length);
            attachFile.setFileType(extName);
            attachFile.setUploadTime(new Date());
            attachFileMapper.insert(attachFile);
            return imgUploadUtil.upload(file, fileName);
        } else {
            // 阿里云文件上传
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(bucketName, fileName, inputStream);
            ossClient.shutdown();
            return fileName;
        }
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String uploadFile(MultipartFile file) throws IOException {
//        String extName = FileUtil.extName(file.getOriginalFilename());
//        String fileName = DateUtil.format(new Date(), NORM_MONTH_PATTERN) + IdUtil.simpleUUID() + "." + extName;
//        AttachFile attachFile = new AttachFile();
//        attachFile.setFilePath(fileName);
//        attachFile.setFileSize(file.getBytes().length);
//        attachFile.setFileType(extName);
//        attachFile.setUploadTime(new Date());
//        if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
//            // 本地文件上传
//            attachFileMapper.insert(attachFile);
//            return imgUploadUtil.upload(file, fileName);
//        } else {
//            // 七牛云文件上传
//            String upToken = auth.uploadToken(qiniu.getBucket(), fileName);
//            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
//            Json.parseObject(response.bodyString(), DefaultPutRet.class);
//            return fileName;
//        }
//    }

    @Override
    public void deleteFile(String fileName) {
        attachFileMapper.delete(new LambdaQueryWrapper<AttachFile>().eq(AttachFile::getFilePath, fileName));
        try {
            if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
                imgUploadUtil.delete(fileName);
            } else if (Objects.equals(imgUploadUtil.getUploadType(), 2)) {
                bucketManager.delete(qiniu.getBucket(), fileName);
            }
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void deleteFile(String fileName) {
//        attachFileMapper.delete(new LambdaQueryWrapper<AttachFile>().eq(AttachFile::getFilePath, fileName));
//        try {
//            if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
//                imgUploadUtil.delete(fileName);
//            } else if (Objects.equals(imgUploadUtil.getUploadType(), 2)) {
//                bucketManager.delete(qiniu.getBucket(), fileName);
//            }
//        } catch (QiniuException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
