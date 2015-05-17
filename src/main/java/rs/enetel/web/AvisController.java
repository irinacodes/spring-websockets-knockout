package rs.enetel.web;


import java.security.Principal;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import rs.enetel.*;
import rs.enetel.model.TimetableItem;
import rs.enetel.service.*;


@Controller
public class AvisController {

    private static final Logger logger = Logger.getLogger(AvisController.class);


    private final TimetableService timetableService;

    private final DelayService delayService;


    @Autowired
    public AvisController(TimetableService timetableService, DelayService delayService) {
        this.timetableService = timetableService;
        this.delayService = delayService;
    }

    @SubscribeMapping("/items")
    public List<TimetableItem> getItems(Principal principal) throws Exception {
        logger.debug("Items for " + principal.getName());
        Timetable timetable = timetableService.findTimetable(principal.getName());
        return timetable.getItems();
    }

    @MessageMapping("/delay")
    public void addDelay(Delay delay, Principal principal) {
        delay.setUsername(principal.getName());
        logger.debug("Delay: " + delay);
        delayService.addDelay(delay);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }


}