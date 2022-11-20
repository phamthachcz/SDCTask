package SDC.SDCTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Sunshine is a class that will read from the file and store the data on the power used every day. 
 */
public class Sunshine {
	
	private static final String FORMAT_OUTPUT_MONTH = "yyyy-MM";
	private static final String FORMAT_OUTPUT_DAY = "yyyy-MM-dd";
	private static final String FORMAT_OUTPUT_LINE = "%s : %,.2f";
	
	
	/**
	 * The list contains the power that is used every day to read from a file.
	 * @see Data 
	*/
	private List<Data> listData;
	
	private String pathFile = "./Resource/dataexport.csv";
	
	/** 
	* Class constructor.
	*/
	public Sunshine() {
		this.listData = new ArrayList<>();
	}
	
	public Sunshine(String pathFile) {
		this.pathFile = pathFile;
		this.listData = new ArrayList<>();
	}
	/**
	* Returns an List type Data that can then be read from the file. 
	* @return      The list contain data
	* @see         Data
	*/	
	public List<Data> getListData() {
		return listData;
	}
	
	/**
	* This function will show the total amount of power used each day and for the given amount of time.
	* @param from Time from
	* @param to Time to
	* @param formatDate Format of date input (default: yyyyMM)
	*/
	public void searchByMonth(String from, String to, String formatDate) throws IllegalArgumentException {
		
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern(formatDate);
		DateTime fromPeriod = formatter.parseDateTime(from);
		DateTime toPeriod = formatter.parseDateTime(to);

		
		double totalPower = 0;
		
		//Dictionary contain the power each month
		Map<String, Double> mapMonth = new HashMap<>();

		
		for (Data item : listData) {
			if(fromPeriod.isBefore(item.getTimestamp()) && toPeriod.isAfter(item.getTimestamp()) || (toPeriod.getMonthOfYear() == item.getTimestamp().getMonthOfYear() && toPeriod.getYear() == item.getTimestamp().getYear())) {
				
				if(mapMonth.containsKey(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH))) {
					mapMonth.put(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH), mapMonth.get(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH)) + item.getRadiation());
				}
				else {
					mapMonth.put(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH), item.getRadiation());
				}
				totalPower += item.getRadiation();
			}
			
		}

		System.out.println(String.format("Total Power W/m2 from %s to %s : %,.2f", fromPeriod.toString(FORMAT_OUTPUT_MONTH), toPeriod.toString(FORMAT_OUTPUT_MONTH), totalPower));
		printMonthPower(mapMonth);
		
	}
	
	/**
	* This function will show the average, total amount of power used each month, and for the time period specified by the day of the week. 
	* @param from Time from
	* @param to Time to
	* @param dayOfWeek The number day of week (example: 1. Monday, 2. Tuesday ,... , 7. Sunday)
	* @param formatDate Format of date input (default: yyyyMM)
	 * @throws IncorrectFormatException Incorrect weekday format
	*/
	public void searchByGivenDay(String from, String to, int dayOfWeek ,String formatDate) throws IncorrectFormatException {
		if(!checkCorrectFormat(dayOfWeek))
			throw new IncorrectFormatException("Incorrect weekday format! (From 1 to 7)");
		
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern(formatDate);
		DateTime fromPeriod = formatter.parseDateTime(from);
		DateTime toPeriod = formatter.parseDateTime(to);

		
		double totalPower = 0;
		
		//dictionary contain total power each given day.
		Map<String, Double> mapDayOfWeek = new HashMap<>();
		
		//dictionary contain total power each month.
		Map<String, Double> mapMonth = new HashMap<>();
	
		
		for (Data item : listData) {
			if(isInPeriod(fromPeriod, toPeriod, item.getTimestamp()) && item.getTimestamp().dayOfWeek().get() == dayOfWeek) {
				
				if(mapDayOfWeek.containsKey(item.getTimestamp().toString(FORMAT_OUTPUT_DAY))) {
					mapDayOfWeek.put(item.getTimestamp().toString(FORMAT_OUTPUT_DAY), mapDayOfWeek.get(item.getTimestamp().toString(FORMAT_OUTPUT_DAY)) + item.getRadiation());
				}
				else {
					mapDayOfWeek.put(item.getTimestamp().toString(FORMAT_OUTPUT_DAY), item.getRadiation());
				}
				
				if(mapMonth.containsKey(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH))) {
					mapMonth.put(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH), mapMonth.get(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH)) + item.getRadiation());
				}
				else {
					mapMonth.put(item.getTimestamp().toString(FORMAT_OUTPUT_MONTH), item.getRadiation());
				}
				
				
				totalPower += item.getRadiation();
				
			}
			
		}
		
		System.out.println(String.format("Total Power W/m2 from %s to %s : %,.2f", fromPeriod.toString(FORMAT_OUTPUT_MONTH), toPeriod.toString(FORMAT_OUTPUT_MONTH), totalPower));
		printDayofWeekPower(mapDayOfWeek, mapMonth);
	}
	
	
	/**
	* This function will print the average, total amount of power used each month, and for the time period specified by the day of the week. 
	* @param mapDayOfWeek The dictionary contains the total power of each given day.
	* @param mapMonth The dictionary contain the total power of each month
	* @param formatDate Format of date input (default: yyyyMM)
	*/
	public void printDayofWeekPower(Map<String, Double> mapDayOfWeek, Map<String, Double> mapMonth) {
		List<String> sortDayOfWeek = new ArrayList<>(mapDayOfWeek.keySet());
		Collections.sort(sortDayOfWeek, new StringComparator());
		
		List<String> sortMonth = new ArrayList<>(mapMonth.keySet());
		Collections.sort(sortMonth, new StringComparator());
		
		int countDay = 0;
		int indexMonth = 0; 
		
		for(int i = 0; i < sortDayOfWeek.size(); i++) {
			
			if(i !=  sortDayOfWeek.size() - 1) {
				if(sortDayOfWeek.get(i).contains(sortMonth.get(indexMonth))) {
					System.out.println(String.format(FORMAT_OUTPUT_LINE, sortDayOfWeek.get(i), mapDayOfWeek.get(sortDayOfWeek.get(i))));
					countDay = countDay + 1;
				}
				else {
					System.out.println(String.format("Total Power in " + FORMAT_OUTPUT_LINE, sortMonth.get(indexMonth), mapMonth.get(sortMonth.get(indexMonth))));
					System.out.println(String.format("Average " + FORMAT_OUTPUT_LINE, sortMonth.get(indexMonth), mapMonth.get(sortMonth.get(indexMonth)) / countDay));
					System.out.println(String.format(FORMAT_OUTPUT_LINE, sortDayOfWeek.get(i), mapDayOfWeek.get(sortDayOfWeek.get(i))));
					countDay = 1;
					indexMonth += 1;
					
				}
			}
			else {
				System.out.println(String.format(FORMAT_OUTPUT_LINE, sortDayOfWeek.get(i), mapDayOfWeek.get(sortDayOfWeek.get(i))));
				countDay = countDay + 1;
				System.out.println(String.format("Total Power in " + FORMAT_OUTPUT_LINE, sortMonth.get(indexMonth), mapMonth.get(sortMonth.get(indexMonth))));
				System.out.println(String.format("Average in " + FORMAT_OUTPUT_LINE, sortMonth.get(indexMonth), mapMonth.get(sortMonth.get(indexMonth)) / countDay));
			}
			
		}
		
		
	}
	
	/**
	* This function will print the result by format.
	* •	Total for the selected period
	* •	Total by month for the selected period (one month per line)
	* @param mapMonth The dictionary contains the power of each month.
	*/
	public void printMonthPower(Map<String, Double> mapMonth) {
		
		List<String> mapByKey = new ArrayList<>(mapMonth.keySet());
		//Sort the dictionary
		Collections.sort(mapByKey,new StringComparator());
		
		for (String key : mapByKey) {
			System.out.println(String.format(FORMAT_OUTPUT_LINE, key, mapMonth.get(key)));
		}
		
	}
	
	
	/**
	* This function checks if the time is in the given period or not.
	* @param from Time from
	* @param to Time to
	* @param input Test time
	* @return true or false
	* @see DateTime
	*/
	public boolean isInPeriod(DateTime from, DateTime to, DateTime input) {
		return (from.isBefore(input) && to.isAfter(input)) || (to.getMonthOfYear() == input.getMonthOfYear() && to.getYear() == input.getYear());
	}
	
	
	
	
	public boolean readDataFromFile() {
		
		CSVReader csvReader = new CSVReader(this.pathFile);
		try {
			this.listData = csvReader.readAllFile();
			
			return true;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	* This function determines whether or not the input number is in the format day number of the week. 
	* @param dayOfWeek Specify the weekday.
	* @return true or false
	*/
	public boolean checkCorrectFormat(int dayOfWeek) {
		return dayOfWeek >= 1 && dayOfWeek <= 7;
		
	}
	
}
