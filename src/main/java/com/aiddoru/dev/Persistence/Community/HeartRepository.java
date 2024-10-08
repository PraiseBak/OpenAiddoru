package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart,Long>
{

}
