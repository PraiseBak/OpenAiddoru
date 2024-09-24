package com.aiddoru.dev.Domain.Entity.Rank;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.math.BigInteger;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdolToday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //전날대비 구독자수
    private BigInteger subscribeDiff = BigInteger.valueOf(0);

    //전날대비 구독자수 퍼센테이지
    private Double subscribeDiffPercentage = 0.0;

    //오늘 클릭된 횟수
    private Long todayClicked = 0L;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonBackReference
    private Idol idol;

    public void clickUpdate() {
        todayClicked++;
    }
}
