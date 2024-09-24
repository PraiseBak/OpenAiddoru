package com.aiddoru.dev.Domain.Entity.Recommend;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.User.TestUser;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String tag;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Idol idol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private IdolVideoInfo idolVideoInfo;

    /**
     * 태그의 가중치 description에 존재하는 태그의 경우 weight를 높게둠
     * MAX 3
     */
    @Getter
    private Integer weight = 1;

    public void setIdol(Idol existsIdol) {
        this.idol = existsIdol;
    }
}

