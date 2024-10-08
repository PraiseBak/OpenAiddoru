package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.aiddoru.dev.Persistence.Rank.IdolSubscriberTraceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdolSubscriberTraceService {

    private final IdolSubscriberTraceRepository idolSubscriberTraceRepository;

    @Transactional
    public void saveSubscriberTrace(Idol idol) {
        IdolSubscriberTrace idolSubscriberTrace = IdolSubscriberTrace.builder()
                .subscriberCount(idol.getSubscriberCount())
                .viewCount(idol.getViewCount())
                .build();
        idol.getIdolSubscriberTraceList().add(idolSubscriberTrace);
    }
}
