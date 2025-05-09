package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    private Long id;
    private Long post_id; // 어느 글의 첨부파일(FK)

    private String sourcename; //원본파일명
    private String filename; //저장된 파일명(rename 된 파일명)

    private boolean isImage; //이미지 여부
}
