package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdolRepository extends JpaRepository<Idol, Long> {

    Optional<Idol> findByChannelId(String channelId);

    Optional<Idol> findByChannelNameIs(String channelName);

    int countIdolByChannelNameStartingWith(String channelName);
    Optional<Idol> findByChannelNameStartingWith(String channelName);


    //idol update용
//    List<Idol> findTop300ByOrderBySubscriberCountDesc(Pageable pageable);
    List<Idol>  findTop300ByOrderBySubscriberCountDesc();
    List<Idol> findTop300ByOrderBySubscriberCountDesc(Pageable pageable);

    List<Idol> findAllByOrderBySubscriberCountDesc(Pageable pageable);

    List<Idol> findAllByCountryOrderBySubscriberCountDesc(Pageable pageable,String country);

    //test용 메서드
    List<Idol> findTop10ByOrderBySubscriberCountDesc();

    //idolToday update용
    List<Idol> findTop400ByOrderBySubscriberCountDesc();


    List<Idol> findTop300ByCountryOrderBySubscriberCountDesc(Pageable pageable, String country);
    List<Idol> findTop300ByCountryOrderBySubscriberCountDesc(String country);

    List<Idol> findTop30ByCountryOrderBySubscriberCountDesc(String country);

    Optional<Idol> findDistinctByChannelNameContaining(String recommendedAiddoruName);
}
