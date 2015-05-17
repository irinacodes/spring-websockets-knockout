package rs.enetel;

import rs.enetel.model.TimetableItem;
import java.util.*;

public class Timetable {

    private final Map<Long,TimetableItem> timetableLookup = new HashMap<>();

    public List<TimetableItem> getItems() {
        return new ArrayList<>(timetableLookup.values());
    }

    public void addItem(TimetableItem item) {
        timetableLookup.put(item.getTrainId(), item);
    }

    public TimetableItem getItem(String trainId) {
        return timetableLookup.get(trainId);
    }

    public TimetableItem addDelay(Long trainId, int delayInMinutes) {
        TimetableItem item = timetableLookup.get(trainId);
        if ((item == null) || (delayInMinutes < 1)) {
            return null;
        }
        item = new TimetableItem(item, delayInMinutes);
        timetableLookup.put(trainId, item);
        return item;
    }

}
