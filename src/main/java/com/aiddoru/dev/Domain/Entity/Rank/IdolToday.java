package com.aiddoru.dev.Domain.Entity.Rank;


import com.aiddoru.dev.Utility.BigIntegerDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private BigInteger subscribeDiff = BigInteger.valueOf(0);

    //전날대비 구독자수 퍼센테이지
    private Double subscribeDiffPercentage = 0.0;

    //오늘 클릭된 횟수
    private Long todayClicked = 0L;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Idol idol;

    public void clickUpdate() {
        todayClicked++;
    }
}
