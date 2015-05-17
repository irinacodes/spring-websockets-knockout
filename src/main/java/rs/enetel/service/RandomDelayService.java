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
        for (TimetableItem item : timetable.getItems()) {
            //generate random offset in mins between -20 and 20
            randomDelays.add(new RandomDelay(item.getTrainId(), (random.nextInt(40) - 20)));
        }
        return randomDelays;
    }


}
