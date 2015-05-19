package rs.enetel;

import rs.enetel.model.TimetableItem;
import rs.enetel.repository.TimetableRepository;
import java.util.*;

public class Timetable {

    private TimetableRepository timetableRepository;

    public Timetable() {
        timetableRepository = ApplicationContextProvider.getApplicationContext().getBean(TimetableRepository.class);
    }

    private final Map<Long,TimetableItem> timetableLookup = new HashMap<>();

    public List<TimetableItem> getItems() {
        return new ArrayList<>(timetableLookup.values());
    }

    public void addItem(TimetableItem item) {
        timetableLookup.put(item.getTrainId(), item);
    }

    public TimetableItem addDelay(Long trainId, int delayInMinutes) {
        TimetableItem item = timetableLookup.get(trainId);
        if ((item == null) || (delayInMinutes < 1)) {
            return null;
        }
        item.setDelayInMinutes(item.getDelayInMinutes() + delayInMinutes);
        timetableRepository.save(item);
        timetableLookup.put(trainId, item);
        return item;
    }


    }
