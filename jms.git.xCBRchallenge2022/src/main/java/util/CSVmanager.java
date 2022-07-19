package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.Weather;

public class CSVmanager {

	public static ArrayList<Weather> readCSV(String filename) {
		ArrayList<Weather> content = new ArrayList<Weather>();
		String dayPattern = "dd";
		String monthPattern = "MM";
		String yearPattern = "yyyy";
		SimpleDateFormat dayFormat = new SimpleDateFormat(dayPattern);
		SimpleDateFormat monthFormat = new SimpleDateFormat(monthPattern);
		SimpleDateFormat yearFormat = new SimpleDateFormat(yearPattern);
		boolean header = true; 

		try {
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] tokens = line.split(",");
					String[] results = new String[11];
					if (tokens.length > 0) {
						for (int i = 0; i < tokens.length; i++) {
							results[i] = tokens[i];
						}
					}
					if (!header) {
					Date dayDate = dayFormat.parse(results[0]); 
					Date monthDate = monthFormat.parse(results[0]); 
					Date yearDate = yearFormat.parse(results[0]); 
						
					Weather weather = new Weather(
							Integer.parseInt(dayFormat.format(dayDate)), 		// Day
							Integer.parseInt(monthFormat.format(monthDate)),	// Month
							Integer.parseInt(yearFormat.format(yearDate)),		// Year
							Float.parseFloat(results[2]), 						// Temp_max
							Float.parseFloat(results[3]), 						// Temp_min
							Float.parseFloat(results[4]),						// Temp_avg
							Float.parseFloat(results[5]), 						// Pres_avg
							Float.parseFloat(results[6]), 						// Pres_max
							Float.parseFloat(results[7]),						// Pres_min
							Float.parseFloat(results[8]), 						// Hum_avg
							Float.parseFloat(results[9]), 						// Hum_max
							Float.parseFloat(results[10]));						// Hum_min
					content.add(weather);
					}
					header = false;
				}
			} 

		} catch (NumberFormatException e) {
			System.err.println("[CSVmanager] Input is not a number.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("[CSVmanager] Could not find the specified CSV file.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("[CSVmanager] Random I/O error.");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("[CSVmanager] Something went wrong with date parsing.");
			e.printStackTrace();
		} 
		return content;
	}
	
	
	public static void printCSV(String filename) {
		ArrayList<Weather> content = readCSV(filename); 
		System.out.format(
				"+---------+-------+-------+-------+---------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+%n");
		System.out.format(
				"|    Line |   Day | Month |  Year |      Temp_max |    Temp_min |    Temp_avg |    Pres_avg |    Pres_max |    Pres_min |     Hum_avg |     Hum_max |     Hum_min |%n");
		System.out.format(
				"+---------+-------+-------+-------+---------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+%n");
		String leftAlignFormat = " %11s |";
		for (int i = 0; i < 10; i++) {
			System.out.format("| %7s |", i);
			System.out.format(" %5s |", content.get(i).getDay());
			System.out.format(" %5s |", content.get(i).getMonth());
			System.out.format(" %5s |", content.get(i).getYear());
			System.out.format(" %13s |", content.get(i).getTemp_max());
			System.out.format(leftAlignFormat, content.get(i).getTemp_min());
			System.out.format(leftAlignFormat, content.get(i).getTemp_avg());
			System.out.format(leftAlignFormat, content.get(i).getPres_avg());
			System.out.format(leftAlignFormat, content.get(i).getPres_max());
			System.out.format(leftAlignFormat, content.get(i).getPres_min());
			System.out.format(leftAlignFormat, content.get(i).getHum_avg());
			System.out.format(leftAlignFormat, content.get(i).getHum_max());
			System.out.format(leftAlignFormat, content.get(i).getHum_min());
			System.out.format("%n");
		}
		System.out.format(
				"+---------+-------+-------+-------+---------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+%n");

		
	
	
	}

}
