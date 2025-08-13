package com.oyproj.portal;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oyproj.mall.mapper.*;
import com.oyproj.mall.model.PmsBrand;
import com.oyproj.mall.model.PmsProduct;
import com.oyproj.mall.model.SmsHomeAdvertise;
import com.oyproj.portal.service.MinioService;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@SpringBootTest
public class FixPic {
    
    @Autowired
    private MinioService minioService;
    @Autowired
    private UmsMemberMapper mapper;
    String getUrl = "getIcon";
    String setUrl = "setIcon";
    @Test
    void changeUrl(){
        savePic(mapper,"img");
    }

    // 定义一个接口，用于规范获取图片 URL 的方法
    interface HasPic {
        String getPic();
        void setPic(String pic);
    }

    /**
     * 下载指定 URL 的图片并上传到 MinIO
     * @param imageUrl 图片的 URL 地址
     * @return 上传到 MinIO 后的图片路径
     * @throws IOException 下载或上传过程中出现的 IO 异常
     */
    private String downloadImage(String imageUrl,String subDicName) throws IOException {
        // 创建 URL 对象
        URL url = new URL(imageUrl);
        // 打开 HTTP 连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // 获取输入流
        try (InputStream inputStream = connection.getInputStream()) {
            // 读取图片数据到字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();

            // 生成文件名，从 URL 中提取
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

            // 创建 MockMultipartFile 对象
            MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    fileName,
                    "image/*",
                    imageBytes
            );

            // 调用 MinIO 上传方法
            var uploadResult = minioService.upload(multipartFile,subDicName);
            if (uploadResult != null) {
                System.out.println("图片上传成功: " + imageUrl + "，上传后的路径: " + uploadResult.getUrl());
                return uploadResult.getUrl();
            } else {
                System.err.println("图片上传失败: " + imageUrl);
                return null;
            }
        } finally {
            // 断开连接
            connection.disconnect();
        }
    }
    //1.下载数距库中对应的图片并存储在当前文件夹中
    @Transactional
    <T> void savePic(BaseMapper<T> baseMapper, String subDicName){
        List<T> list = baseMapper.selectList(new LambdaQueryWrapper<>());
        list.forEach(item->{
            try{
                // 假设对象有 getPic() 方法获取图片 URL
                Method getPicMethod = item.getClass().getMethod(getUrl);
                String picUrl = (String)getPicMethod.invoke(item);
                if (picUrl != null && !picUrl.isEmpty()) {
                    String url = downloadImage(picUrl,subDicName);
                    //跟新该url
                    if(url!=null){
                        Method setPic = item.getClass().getMethod(setUrl, String.class);
                        setPic.invoke(item, url);
                        baseMapper.updateById(item);
                    }
                }
            } catch (ClassCastException e) {
                System.err.println("对象不包含 getPic() 方法: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("下载图片失败: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
