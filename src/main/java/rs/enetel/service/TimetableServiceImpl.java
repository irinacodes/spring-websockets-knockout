package rs.enetel.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import rs.enetel.ApplicationContextProvider;
import rs.enetel.Timetable;
import rs.enetel.repository.TimetableRepository;
import rs.enetel.model.TimetableItem;
import java.util.*;


@Service
public class TimetableServiceImpl implements TimetableService {


    // user -> timetable
    private final Map<String, Timetable> timetableLookup = new HashMap<>();
    private static final Logger logger = Logger.getLogger(TimetableServiceImpl.class);


    public TimetableServiceImpl() {

        TimetableRepository timetableRepository = ApplicationContextProvider.getApplicationContext().getBean(TimetableRepository.class);

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

    }

    public Timetable findTimetable(String username) {
        Timetable timetable = timetableLookup.get(username);
        if (timetable == null) {
            throw new IllegalArgumentException(username);
        }
        return timetable;
    }

}
