package com.aiddoru.dev.Utility.Page;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class PageUtility
{
    public static final int perSize = 30;

    /*
    start index = 0
    default size = 30
     */
    public static Pageable getPageable(long page){
        return PageRequest.of((int) page-1,perSize);
    }


    /*
    start index = 0
    설정한 사이즈만큼의 페이징
     */
    public static Pageable getPageable(long page,int size){
        return PageRequest.of((int) page,size);
    }




}
