package com.aiddoru.dev.DTO.Community;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThreadRequestDto {

    @Size(min = 2,max = 40)
    private String title;

    @Size(min = 2,max = 40)
    private String content;


}
