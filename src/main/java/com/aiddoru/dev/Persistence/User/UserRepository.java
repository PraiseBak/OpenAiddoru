package com.aiddoru.dev.Persistence.User;

import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Authority;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{

//    @Query("SELECT u FROM User u JOIN FETCH u.writeThreadList WHERE u.username = :username")
//Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from User u")
    List<User> findAllTest();





    Optional<User> findByUsername(@Param("username") String username);
//    Optional<User> findByUsername(String username);


    boolean existsByUsername(String username);

    List<User> findByUsernameContains(String searchParam, Pageable pageable);

    List<User> findAllByAuthority(Authority authority);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUsername(String email, String username);
    Optional<User> findByEmailOrUsername(String email, String username);

}
