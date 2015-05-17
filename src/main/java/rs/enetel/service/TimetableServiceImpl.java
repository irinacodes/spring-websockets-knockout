package rs.enetel.service;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import rs.enetel.Timetable;
import rs.enetel.repository.TimetableRepository;
import rs.enetel.model.TimetableItem;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class TimetableServiceImpl implements TimetableService {

    //todo autowire
    private TimetableRepository timetableRepository;

    // user -> timetable
    private final Map<String, Timetable> timetableLookup = new HashMap<>();
    private static final Logger logger = Logger.getLogger(TimetableServiceImpl.class);


    public TimetableServiceImpl() {

        ClassPathXmlApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");

        timetableRepository = (TimetableRepository) context.getBean("timetableRepository");

        bootstrapTimetable();

        Timetable timetable = new Timetable();
        for (TimetableItem timetableItem : timetableRepository.findByDepartureStation("Beograd")) {
            timetable.addItem(timetableItem);
        }
        timetableLookup.put("stanicna", timetable);

        timetable = new Timetable();
        for (TimetableItem timetableItem : timetableRepository.findByTrack(1)) {
            timetable.addItem(timetableItem);
        }
        timetableLookup.put("peronska", timetable);

        context.close();
    }

    @Transactional
    public void bootstrapTimetable() {
        TimetableItem item = new TimetableItem("101233", "Železnice Srbije", "Beograd", "Niš", 5, 12, 30, 1);
        timetableRepository.save(item);

        item = new TimetableItem("2006", "Železnice Srbije", "Beograd", "Subotica", 0, 12, 40, 2);
        timetableRepository.save(item);

        item = new TimetableItem("100", "Železnice Srbije", "Beograd", "Vršac", 1, 13, 0, 1);
        timetableRepository.save(item);

        item = new TimetableItem("200200", "Železnice Srbije", "Beograd", "Novi Sad", 10, 13, 15, 3);
        timetableRepository.save(item);

        item = new TimetableItem("100900", "Železnice Srbije", "Beograd", "Bar", 0, 13, 30, 2);
        timetableRepository.save(item);

        item = new TimetableItem("456", "Železnice Srbije", "Beograd", "Pančevo", 10, 14, 0, 1);
        timetableRepository.save(item);
    }


    public Timetable findTimetable(String username) {
        Timetable timetable = timetableLookup.get(username);
        if (timetable == null) {
            throw new IllegalArgumentException(username);
        }
        return timetable;
    }

}
