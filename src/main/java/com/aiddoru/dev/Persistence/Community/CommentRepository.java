package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Domain.Entity.Community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>
{
}
