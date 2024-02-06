package com.bookJourney.springboot.service;

import com.bookJourney.springboot.dto.NotificationDTO;
import com.bookJourney.springboot.dto.ReviewDTO;
import com.bookJourney.springboot.entity.SharedBook;
import com.bookJourney.springboot.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);



    public SseEmitter createEmitter() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }

    public void triggerNotification(SharedBook book) {
        String title = book.getBook().getBookDetail().getTitle();
        String author = book.getBook().getBookDetail().getAuthor();
        ReviewDTO reviewDTO = mapper.reviewToReviewDTO(book.getReview());
        NotificationDTO notificationDTO = new NotificationDTO(book.getOwner().getUsername(), title, author, reviewDTO, book.isRecommended(), book.getDateShared());

        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notificationDTO));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        this.emitters.removeAll(deadEmitters);
    }
}
