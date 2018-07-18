package blog.web;

import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ${MIND-ZR} on 2017/12/7.
 */

@Service
public class AliUpLoad {
  //  Logger logger = Logger.getLogger(AliUpLoad.class);




    public static String purObjectByBytes(MultipartFile file) {

        OSSClient ossClient = new OSSClient("https://oss-cn-beijing.aliyuncs.com", "LTAIZyl7AnHs699l", "xrDdkEtvW1aKSo8tnRCQxrUPz6Ojhg");

        //用UUID生成文件名
        String uuid = UUID.randomUUID().toString();

        //获取文件后缀
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));//拿到 .* 形式的后缀

        String key = uuid + suffix;

        try {
            byte[] context = file.getBytes();

            ossClient.putObject("mind222", key, new ByteArrayInputStream(context));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        //返回文件的URl
        return "https://mind222.oss-cn-beijing.aliyuncs.com/" + key;
    }
}
