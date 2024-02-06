package cn.piesat.tests.file.s3.controller;


import cn.piesat.framework.file.s3.service.OssService;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouxp
 */

@RestController
@RequestMapping("/api/file")
@Slf4j
public class MinioApi {
    @Resource
    private OssService ossService;
    public static final String POLICY = """
            {
              "Statement": [
                {
                  "Resource": "arn:aws:s3:::test-bucket/*",
                  "Action": "s3:GetObject",
                  "Principal": "*",
                  "Effect": "Allow",
                  "Sid": "AddPerm"
                }
              ],
              "Version": "2012-10-17"
            }""";
    @GetMapping("/getObjectURL")
    public Object getObjectURL(@RequestParam("bucketName") String bucketName, @RequestParam("objectName") String objectName) {
        return ossService.getObjectURL(bucketName, objectName);
    }
    @GetMapping("/getObject")
    public S3Object getObject(@RequestParam("bucketName") String bucketName, @RequestParam("objectName") String objectName) {
        S3Object object = ossService.getObject(bucketName, objectName);
        final File localFile = new File("1.jpg");
        try {
            Files.copy(object.getObjectContent(), localFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    @PostMapping("/putObject")
    public PutObjectResult putObject(@RequestParam("bucketName") String bucketName, @RequestParam("objectName") String objectName, @RequestParam("file")MultipartFile file) throws IOException {
        return ossService.putObject(bucketName, objectName, file.getInputStream());
    }
    @PostMapping("/bucketPolicy")
    public Map<String,Object> bucketPolicy(@RequestParam(value = "file") MultipartFile file) throws IOException {
        ossService.createBucket("test-bucket");
        ossService.bucketPolicy("test-bucket", POLICY);
        ossService.putObject("test-bucket", file.getOriginalFilename(), file.getInputStream());
        final S3Object object = ossService.getObject("test-bucket", file.getOriginalFilename());
        final String objectURL = ossService.getObjectURL("test-bucket", file.getOriginalFilename());
        Map<String,Object> map =new HashMap<>();
        map.put("objectURL",objectURL);
        map.put("object",object);
        return map;
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("objectName") String objectName){
         ossService.removeObject(objectName)  ;
         return true;
    }


}
