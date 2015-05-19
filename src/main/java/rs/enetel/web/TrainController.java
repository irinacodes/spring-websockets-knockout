package rs.enetel.web;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.enetel.model.Train;
import rs.enetel.repository.TrainRepository;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {

    private TrainRepository trainRepository;

    public TrainController() {

        AbstractApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");

        trainRepository = context.getBean(TrainRepository.class);
    }

    @RequestMapping(value= "/", method = RequestMethod.GET)
    public List<Train> getTrains() {
        return (List<Train>) trainRepository.findAll();
    }

    @RequestMapping(value = "/{trainId}", method = RequestMethod.GET)
    Train getTrain(@PathVariable Long trainId) {
        return trainRepository.findOne(trainId);
    }


}
