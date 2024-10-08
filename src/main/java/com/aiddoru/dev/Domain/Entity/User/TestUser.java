package com.aiddoru.dev.Domain.Entity.User;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Test> testList = new ArrayList<>();
}
