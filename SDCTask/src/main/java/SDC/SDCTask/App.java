package SDC.SDCTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	if(args.length >= 2) {
    		Sunshine sunshine = new Sunshine();
    		if(sunshine.readDataFromFile()) {
            	try {
            		if(args.length == 2) {
            			sunshine.searchByMonth(args[0], args[1], "yyyyMM");
            		}
            		else if(args.length == 3) {
            			int dayOfWeek = Integer.parseInt(args[2]);
            			sunshine.searchByGivenDay(args[0], args[1], dayOfWeek  ,"yyyyMM");           			
            		}
            	}
            	catch (Exception e) {
					e.printStackTrace();
				}
            	
            }
    	}
    	
    	
        
    }
}
