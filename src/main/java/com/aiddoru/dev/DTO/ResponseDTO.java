package com.aiddoru.dev.DTO;

import java.util.*;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDTO<T> {
    
    private List<T> data;
    private String error;
}
