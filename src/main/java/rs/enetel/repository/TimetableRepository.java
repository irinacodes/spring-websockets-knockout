package rs.enetel.repository;

import org.springframework.data.repository.CrudRepository;
import rs.enetel.model.TimetableItem;
import java.util.List;

public interface TimetableRepository extends CrudRepository<TimetableItem, Long> {

    List<TimetableItem> findByDepartureStation(String departureStation);

    List<TimetableItem> findByTrack(int track);

}