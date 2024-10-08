package com.aiddoru.dev.DTO.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class BlockUserRequestDto
{
    long blockUserId;
    int blockDay;

}
