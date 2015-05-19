package rs.enetel.repository;

import org.springframework.data.repository.CrudRepository;
import rs.enetel.model.Train;

public interface TrainRepository extends CrudRepository<Train, Long> {

    Train findByName(String name);

    Train findByTrainId(Long trainId);
}