package SDC.SDCTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CSVReader {
	private String path;
	
	public CSVReader(String path) {
		this.path = path;
	}
	
	public List<Data> readAllFile() throws FileNotFoundException{
		List<Data> listResult = new ArrayList<>();
		
		try (Scanner scanner = new Scanner(new File(this.path));) {
		    while (scanner.hasNextLine()) {
		    	Data item = getRecordFromLine(scanner.nextLine());
		    	if(item != null) {
		    		listResult.add(item);
		    	}
		    	
		    }
		}
		
		
		return listResult;
	}
	
	public Data getRecordFromLine(String textLine) {
		String[] items = textLine.split(",");
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYYMMdd'T'HHmm");
		
		Data item = null;
		
		try {
			DateTime date = formatter.parseDateTime(items[0]);
			if(items.length < 2) {
				item = new Data(date, 0);
			}
			else {
				double radiation = Double.parseDouble(items[1]);
				item = new Data(date, radiation);
			}
			
			
			return item;
		} catch (IllegalArgumentException e) {
			return item;
		}  
		
			
	}
	
}
