package com.lec.spring.util;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class U {
    // 현재 request 구하기
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest();
    }

    // 현재 session 구하기
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    // 현재 로그인 한 사용자 User구하기
    public static User getLoggedUser(){
        // 현재 로그인 한 사용자
        PrincipalDetails userDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user;
    }

    // 첨부파일 정보(MultipartFile) 출력하기
    public static void printFileInfo(MultipartFile file) {
        String originalFileName = file.getOriginalFilename(); //업로드 당시 원본 이름

        if(originalFileName == null || originalFileName.isEmpty()){
            System.out.println("\t파일이 없습니다");
            return;
        }

        System.out.println("""
                Original File Name: %s
                CleanPath: %s
                File Size: %s
                MIME: %s
                """.formatted(originalFileName,
                //   ※  cleanPath 는  C:\Users\aaa\bbbb/dsaf/asdfsafd.ddd
                //       "\" -> "/" 로 변경
                StringUtils.cleanPath(originalFileName),
                file.getSize() + "bytes", //byte 단위 용량.
                file.getContentType() //MIME TYPE
                ));

        //이미지 파일인 경우
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage != null){ //이미지 파일인 경우
                System.out.printf("\t이미지 파일입니다: %d x %d\n", bufferedImage.getWidth(), bufferedImage.getHeight());
            }
            else{ //아닌 경우
                System.out.println("\t이미지 파일이 아닙니다");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
