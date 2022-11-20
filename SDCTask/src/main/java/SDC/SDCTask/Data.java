package SDC.SDCTask;

import org.joda.time.DateTime;

/**
 * Data is a class that contains the date and radiation (W/m²) values.
 */
public class Data{
	private DateTime timestamp;
	private double radiation;
	
	public DateTime getTimestamp() {
		return timestamp;
	}
	
	public void setRadiation(double radiation) {
		this.radiation = radiation;
	}
	
	public double getRadiation() {
		return radiation;
	}
	
	/** 
	* Class constructor.
	* @param timestamp the time at which radiation value is recognized
	* @param radiation The Basel Shortwave Radiation
	*/
	public Data(DateTime timestamp, double radiation) {
		this.timestamp = timestamp;
		this.radiation = radiation;
	}

	
	
	
	
	
}
