package com.aiddoru.dev.Utility.Tag;

import java.time.LocalDateTime;

public class TagHelper {

    //태그 기준
    public static LocalDateTime getTagStandardDate(){
        return LocalDateTime.now().minusDays(3L);
    }
}
