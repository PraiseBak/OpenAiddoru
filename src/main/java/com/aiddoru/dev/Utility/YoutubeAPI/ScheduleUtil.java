package com.aiddoru.dev.Utility.YoutubeAPI;

import java.util.Calendar;

public class ScheduleUtil {

    /**
     * 100단위로 날짜가 1,2,3일때 실행함
     * @return
     */
    public static boolean isIdolScheduleRunnable(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day % 4 != 0;
    }

    /**
     * 최근 영상 비디오 업데이트를 위한 몇번째 범위를 리턴할지 결정하는 함수입니다
     * 최대 300명까지 계산하기 떄문에 일수를 12로 나누어서 계산하도록 구현하였습니다.
     * @return
     */
    public static int getVideoScheduleStartUnitNum(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        return day % 13 * 50;
    }


    public static int getVideoScheduleStartUnitNumForKorean(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        return (day % 3) * 30;
    }

    public static boolean initIdolTodayClicked(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        return day % 5 == 0;
    }

}
