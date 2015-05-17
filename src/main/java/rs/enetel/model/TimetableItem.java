package rs.enetel.model;

import javax.persistence.*;

@Entity
@Table(name="TIMETABLE")
public class TimetableItem {

    @Id
    @GeneratedValue
    private Long trainId;

    private String trainCode;

    private String carrier;

    private String departureStation;

    private String destinationStation;

    private int delayInMinutes;

    private int plannedArrivalHour;

    private int plannedArrivalMinute;

    private int track;

    private long updateTime;

    public TimetableItem() {}

    public TimetableItem(String trainCode,String carrier, String departureStation,
                         String destinationStation, int delayInMinutes, int plannedArrivalHour,
                         int plannedArrivalMinute, int track) {
        this.trainCode = trainCode;
        this.carrier = carrier;
        this.departureStation = departureStation;
        this.destinationStation = destinationStation;
        this.plannedArrivalHour = plannedArrivalHour;
        this.plannedArrivalMinute = plannedArrivalMinute;
        this.delayInMinutes = delayInMinutes;
        this.track = track;
        this.updateTime = System.currentTimeMillis();
    }

    public TimetableItem(TimetableItem other, int delayInMinutes) {
        this.trainId = other.trainId;
        this.carrier = other.carrier;
        this.trainCode = other.trainCode;
        this.departureStation = other.departureStation;
        this.destinationStation = other.destinationStation;
        this.plannedArrivalHour = other.plannedArrivalHour;
        this.plannedArrivalMinute = other.plannedArrivalMinute;
        this.track = other.track;
        this.delayInMinutes = other.delayInMinutes + delayInMinutes;
        this.updateTime = System.currentTimeMillis();
    }

    public Long getTrainId() {
        return trainId;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public int getDelayInMinutes() {
        return delayInMinutes;
    }

    public int getPlannedArrivalHour() {
        return plannedArrivalHour;
    }

    public int getPlannedArrivalMinute() {
        return plannedArrivalMinute;
    }

    public int getTrack() {
        return track;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public void setDelayInMinutes(int delayInMinutes) {
        this.delayInMinutes = delayInMinutes;
    }

    public void setPlannedArrivalHour(int plannedArrivalHour) {
        this.plannedArrivalHour = plannedArrivalHour;
    }

    public void setPlannedArrivalMinute(int plannedArrivalMinute) {
        this.plannedArrivalMinute = plannedArrivalMinute;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TimetableItem [trainId=" + trainId + ", carrier=" + carrier
                + ", starting station=" + departureStation + ", destination station=" + destinationStation
                + ", plannedArrivalTime=" + plannedArrivalHour + ":" + plannedArrivalMinute
                + ", delay=" + delayInMinutes + " min]";
    }
}
