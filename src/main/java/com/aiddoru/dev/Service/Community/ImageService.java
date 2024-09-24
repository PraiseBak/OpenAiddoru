package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {
    @Getter
    @Value("${upload.path}") // properties 파일에서 이미지 저장 경로를 지정하세요
    private String uploadPath;
    private String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};


    public String saveImage(MultipartFile img) throws IOException {
        if(!validation(img)){
            throw new CustomException(CustomErrorCode.INVALID_RESOURCE);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String today = formatter.format(new Date());
        String originalName = img.getOriginalFilename(); // 실제 파일명
        String originalNameExtension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase(); // 실제파일 확장자 (소문자변경)
        String modifyName = today + "-" + UUID.randomUUID().toString().substring(20) + "." + originalNameExtension;
        String uploadPath = getUploadPath();
        File outputFile = new File(uploadPath + File.separator + modifyName);
        img.transferTo(outputFile);
        return modifyName;
    }

    private boolean validation(MultipartFile img){
        // 예시: 이미지 크기 제한 (10MB)
        if (img.getSize() > 10 * 1024 * 1024) {
            log.info("이미지 크기 제한 초과");
            return false;
        }

        String originalFilename = img.getOriginalFilename();
        if (originalFilename != null) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            boolean isAllowedExtension = false;
            for (String allowedExtension : allowedExtensions) {
                if (allowedExtension.equals(extension)) {
                    isAllowedExtension = true;
                    break;
                }
            }
            if (!isAllowedExtension) {
                log.info("유효하지 않은 확장자");
                return false;
            }
        }

        return true;
    }
}
