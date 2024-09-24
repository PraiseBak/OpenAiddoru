package com.aiddoru.dev.Domain.Entity.Community;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ThreadStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //오늘 조회수
    private Long todayVisitCount = 0L;

    //오늘 받은 좋아요 수
    private Long todayHeartCount = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Thread thread;


    public void addTodayVisitCount() {
        this.todayVisitCount++;
    }

    public void addTodayHeartCount() {
        this.todayHeartCount++;
    }

}
