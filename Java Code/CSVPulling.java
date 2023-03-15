package eot_nikola_christina;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class CSVPulling {
	List<String> latitudeList = new ArrayList<>();
    List<String> longitudeList = new ArrayList<>();
    List<String> tweetList = new ArrayList<>();
    List<String> createdAtList = new ArrayList<>();

	public CSVPulling() {

		try {
			
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader reader;
			reader = new CSVReaderBuilder(new FileReader("twitter.csv"))
			        .withSkipLines(1)
			        .withCSVParser(parser)
			        .build();
			 
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
			    if (nextLine != null) {
			        latitudeList.add(nextLine[2]);
			        longitudeList.add(nextLine[1]);
			        tweetList.add(nextLine[5]);
			        createdAtList.add(nextLine[6]);
			    }
}
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

        }

	
	 public List<String> getLats() {
	       return latitudeList;
	   }
	 public List<String> getLons() {
	       return longitudeList;
	   }
	 public List<String> getTweets() {
	       return tweetList;
	   }
	 public List<String> getDatetime() {
	       return createdAtList;
	   }
}
