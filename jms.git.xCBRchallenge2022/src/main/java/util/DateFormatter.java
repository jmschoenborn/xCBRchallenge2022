package util;

public class DateFormatter {
	
	public int MonthnameToNumber(String name) {
		int month = 0; 
		switch (name) {
		case "JANUARY": 
			month = 1; 
			break; 
		case "FEBURARY": 
			month = 2; 
			break; 
		case "MARCH": 
			month = 3; 
			break; 
		case "APRIL": 
			month = 4; 
			break; 
		case "MAY": 
			month = 5; 
			break; 
		case "JUNE": 
			month = 6; 
			break; 
		case "JULY": 
			month = 7; 
			break; 
		case "AUGUST": 
			month = 8; 
			break; 
		case "SEPTEMBER": 
			month = 9; 
			break; 
		case "OCTOBER": 
			month = 10; 
			break; 
		case "NOVEMBER": 
			month = 11; 
			break; 
		case "DECEMBER": 
			month = 12; 
			break; 
		}
		return month; 
	}
}