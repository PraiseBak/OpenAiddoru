package com.aiddoru.dev.Controller.Community;

import com.aiddoru.dev.Service.Community.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/img")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @RequestMapping("/get.cf")
    public void getFile(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        //응답할 타입 설정
        response.setContentType("application/jpeg");
        String url = "file:///"+ imageService.getUploadPath();

        URL fileUrl = new URL(url + File.separator + fileName);
        try{
            //보여줘야할 최종 url 만들기
            IOUtils.copy(fileUrl.openStream(), response.getOutputStream());
            //fileUrl을 읽어와서 응답(써준)해준다.
        }catch(FileNotFoundException e){
            System.out.println(url + " 파일 찾는데 실패");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> saveImage(MultipartFile img) throws IOException {
        String fileName = imageService.saveImage(img);
        return ResponseEntity.ok(fileName);
    }

}
