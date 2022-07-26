package util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


import data.Weather;


public class Stats {
	private ArrayList<Weather> weatherData; 
	DecimalFormat df = new DecimalFormat("#.#####");
	private String leftAlignFormat;

	/** Constructor to create stats about the dataset. 
	 * @param weatherData Contains the data of the dataset in abstract objects. 
	 */
	public Stats(ArrayList<Weather> weatherData) {
		this.weatherData = weatherData; 
		df.setRoundingMode(RoundingMode.CEILING);
	}


	public void printAttribute(ArrayList<Weather> weatherList, int attribute, String name, int index) {
		leftAlignFormat = " %30s |";
		switch (attribute) {
		case 0: 
			System.out.format(leftAlignFormat, name);
			break;
		case 1:  
			break; 
		case 2: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getDay()));
			break; 
		case 3: 
			System.out.format(leftAlignFormat, weatherList.get(index).getMonth());
			break; 
		case 4: 
			System.out.format(leftAlignFormat, weatherList.get(index).getYear());
			break;
		case 5: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getTemp_max()));
			break; 
		case 6: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getTemp_min()));
			break; 
		case 7: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getTemp_avg()));
			break; 
		case 8: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getPres_avg()));
			break; 
		case 9: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getPres_max()));
			break; 
		case 10: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getPres_min()));
			break; 
		case 11: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getHum_avg()));
			break; 
		case 12: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getHum_max()));
			break;
		case 13: 
			System.out.format(leftAlignFormat, df.format(weatherList.get(index).getHum_min()));
			break; 
		}
	}
}
