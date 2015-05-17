package rs.enetel.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import rs.enetel.Timetable;
import rs.enetel.model.TimetableItem;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class DelayServiceImpl implements DelayService {

    private static final Logger logger = Logger.getLogger(DelayServiceImpl.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final TimetableService timetableService;


    private final List<AddDelayResult> addDelayResults = new CopyOnWriteArrayList<>();


    @Autowired
    public DelayServiceImpl(SimpMessageSendingOperations messagingTemplate, TimetableService timetableService) {
        this.messagingTemplate = messagingTemplate;
        this.timetableService = timetableService;
    }

    @Transactional
    public void addDelay(Delay delay) {

        Timetable timetable = timetableService.findTimetable(delay.getUsername());
        Long trainId = delay.getTrainId();
        int delayInMinutes = delay.getDelayInMinutes();

        TimetableItem newItem = timetable.addDelay(trainId, delayInMinutes);

        if (newItem == null) {
            String errorMessage = "Rejected adding delay for train " + trainId;
            messagingTemplate.convertAndSendToUser(delay.getUsername(), "/queue/errors", errorMessage);
            return;
        }

        addDelayResults.add(new AddDelayResult(delay.getUsername(), newItem));
    }

    @Scheduled(fixedDelay = 1500)
    public void sendDelayNotifications() {

        Map<String, Object> map = new HashMap<>();
        map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

        for (AddDelayResult result : addDelayResults) {
            if (System.currentTimeMillis() >= (result.timestamp + 1500)) {
                logger.debug("Sending train delay update: " + result.timetableItem);
                messagingTemplate.convertAndSendToUser(result.user, "/queue/delay-updates", result.timetableItem, map);
                addDelayResults.remove(result);
            }
        }
    }

    private static class AddDelayResult {

        private final String user;
        private final TimetableItem timetableItem;
        private final long timestamp;

        public AddDelayResult(String user, TimetableItem timetableItem) {
            this.user = user;
            this.timetableItem = timetableItem;
            this.timestamp = System.currentTimeMillis();
        }
    }

}
