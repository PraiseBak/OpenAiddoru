package com.aiddoru.dev.Domain.Entity.Rank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BlockedIdol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //차단된 aiddoru name
    private String blockedIdolName;


}
