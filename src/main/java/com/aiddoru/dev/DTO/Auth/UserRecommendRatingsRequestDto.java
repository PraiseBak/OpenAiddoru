package com.aiddoru.dev.DTO.Auth;


import lombok.Data;

import java.util.List;

@Data
public class UserRecommendRatingsRequestDto
{
    List<Integer> ratings;
}
