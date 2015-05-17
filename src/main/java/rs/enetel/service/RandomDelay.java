package rs.enetel.service;

public class RandomDelay {

	private final Long trainId;

	private final int randomDelayInMinutes;


	public RandomDelay(Long trainId, int randomDelayInMinutes) {
		this.trainId = trainId;
		this.randomDelayInMinutes = randomDelayInMinutes;
	}

	public Long getTrainId() {
		return trainId;
	}

	public int getRandomDelayInMinutes() {
		return randomDelayInMinutes;
	}

	@Override
	public String toString() {
		return "Random delay [train=" + trainId + ", delay=" + randomDelayInMinutes + "]";
	}
}
