package com.aiddoru.dev.Domain.Entity.Video;


import com.aiddoru.dev.Domain.Entity.BaseTimeEntity;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class IdolVideoInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String videoId;

    @NotNull
    private String videoThumbnailURL;

    @NotNull
    private LocalDateTime publishedAt;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "idol_video_info_id")
    private List<Tag> tagList = new ArrayList<>();

}
