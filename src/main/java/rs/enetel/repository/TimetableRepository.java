package rs.enetel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.enetel.model.TimetableItem;
import java.util.List;


@Repository
public interface TimetableRepository extends CrudRepository<TimetableItem, Long> {

    List<TimetableItem> findByDepartureStation(String departureStation);

    List<TimetableItem> findByTrack(int track);

    TimetableItem findByTrainId(Long trainId);
}