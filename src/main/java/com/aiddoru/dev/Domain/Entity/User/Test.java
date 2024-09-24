package com.aiddoru.dev.Domain.Entity.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TestUser user;
}
