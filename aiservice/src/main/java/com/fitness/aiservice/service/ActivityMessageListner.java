package com.fitness.aiservice.service;

import com.fitness.aiservice.entity.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListner {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void activityListner(Activity activity){
        log.info("Received activity message: {}", activity.getId());
    }
}
