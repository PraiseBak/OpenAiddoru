package com.aiddoru.dev.Domain.Entity.Recommend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;
    @Getter
    private Long weight;

    public void updateWeight(long l) {
        this.weight += l;
    }
}
