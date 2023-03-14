package cn.dinner.order.controller;

import cn.dinner.order.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 * @author 86139
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${dinner.path}")
    private String path;

    /**
     * 文件上传
     * file是一个临时文件，需要转存到文件夹中，否则就会消失
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("上传文件,{}",file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成文件名
        String fileNewName = UUID.randomUUID().toString()+suffix;
        //创建一个目录对象，如果目录不存在，则创建目录
        File dir=new File(path);
        if(!dir.exists()){
            dir.mkdirs();

        }
        try {
            file.transferTo(new File(path+fileNewName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileNewName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(path+name));
            //输出流，通过输出流将文件写回浏览器,在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("/image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
