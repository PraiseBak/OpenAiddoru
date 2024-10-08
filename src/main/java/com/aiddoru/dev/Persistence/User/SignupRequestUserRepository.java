package com.aiddoru.dev.Persistence.User;

import com.aiddoru.dev.Domain.Entity.User.SignupRequestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupRequestUserRepository extends JpaRepository<SignupRequestUser,Long>
{
    Optional<SignupRequestUser> findByUsernameAndConfirmCode(String username, String confirmCode);

    Optional<SignupRequestUser> findByUsername(String username);
    Optional<SignupRequestUser> findByUsernameAndEmail(String username, String email);
}
