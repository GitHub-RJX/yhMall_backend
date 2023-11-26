package com.yami.shop.common.serializer.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yami.shop.common.bean.Qiniu;
import com.yami.shop.common.util.ImgUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class ImgJsonSerializer extends JsonSerializer<String> {

    @Autowired
    private ImgUploadUtil imgUploadUtil;
    //    @Autowired
//    private Qiniu qiniu;
    @Value("${shop.aLiDaYu.resourcesUrl}")
    private String ossResourceUrl;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StrUtil.isBlank(value)) {
            gen.writeString(StrUtil.EMPTY);
            return;
        }
        String[] imgs = value.split(StrUtil.COMMA);
        StringBuilder sb = new StringBuilder();
        String resourceUrl = "";
        if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
            resourceUrl = imgUploadUtil.getResourceUrl();
        } else {
//            resourceUrl = qiniu.getResourcesUrl();
//            resourceUrl = "https://rjx-yhmall.oss-cn-beijing.aliyuncs.com/";
            resourceUrl = ossResourceUrl;
        }
        for (String img : imgs) {
            sb.append(resourceUrl).append(img).append(StrUtil.COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);
        gen.writeString(sb.toString());
    }
}
