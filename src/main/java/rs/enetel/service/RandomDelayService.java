/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rs.enetel.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.enetel.Timetable;
import rs.enetel.model.TimetableItem;
import rs.enetel.repository.TimetableRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
public class RandomDelayService implements ApplicationListener<BrokerAvailabilityEvent> {

    private static final Logger logger = Logger.getLogger(RandomDelayService.class);

    private final MessageSendingOperations<String> messagingTemplate;

    @Autowired
    private TimetableService timetableService;

    private AtomicBoolean brokerAvailable = new AtomicBoolean();

    private Random random;


    @Autowired
    public RandomDelayService(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        random = new Random();
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    @Scheduled(fixedDelay = 2000)
    public void sendRandomDelays() {
        for (RandomDelay delay : generateRandomDelays()) {
            logger.trace("Sending random delay " + delay.getTrainId());
            if (this.brokerAvailable.get()) {
                this.messagingTemplate.convertAndSend("/topic/random.delay." + delay.getTrainId(), delay);
            }
        }
    }

    private Set<RandomDelay> generateRandomDelays() {
        Set<RandomDelay> randomDelays = new HashSet<>();
        Timetable timetable = timetableService.findTimetable("stanicna");
//        for(int i=1; i<7;i++) {
//            randomDelays.add(new RandomDelay(new Long(i++), (random.nextInt(40) - 20)));
//        }
        for (TimetableItem item : timetable.getItems()) {
            //generate random offset in mins between -20 and 20
            randomDelays.add(new RandomDelay(item.getTrainId(), (random.nextInt(40) - 20)));
        }
        return randomDelays;
    }


}
