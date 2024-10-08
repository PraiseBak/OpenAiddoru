package com.aiddoru.dev.Domain.Entity.Community;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class CommunityRecommend
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(10)
    private int recommendStandard= 10;

    private Long recommendedThreadCount = 0L;

    private String communityName;

    private Long recommendedHeartCount = 0L;

    public void addRecommendedThreadCount() {
        recommendedThreadCount++;
    }

    public void addRecommendedHeartCount(){
        this.recommendedHeartCount++;
    }


    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
