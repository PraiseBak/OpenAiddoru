package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long>
{


    Optional<Community> findByCommunityURL(String communityURL);

    Optional<Community> findByCommunityNameOrCommunityURL(String communityName,String communityURL);

    List<Community> findByCommunityIdolCountry(String country);

}
