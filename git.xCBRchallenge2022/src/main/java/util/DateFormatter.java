package util;

public class DateFormatter {
	
	public static int monthToNumber(String name) {
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
	
	
	public static String numberToMonthname(int month) {
		String name = "ERROR: monthname not found!"; 
		switch (month) {
		case 1: 
			name = "JANUARY"; 
			break; 
		case 2: 
			name = "FEBURARY";
			break; 
		case 3: 
			name = "MARCH";
			break; 
		case 4: 
			name = "APRIL";
			break; 
		case 5: 
			name = "MAY";
			break; 
		case 6: 
			name = "JUNE";
			break; 
		case 7: 
			name = "JULY";
			break; 
		case 8: 
			name = "AUGUST"; 
			break; 
		case 9: 
			name = "SEPTEMBER";
			break; 
		case 10: 
			name = "OCTOBER";
			break; 
		case 11: 
			name = "NOVEMBER";
			break; 
		case 12: 
			name = "DECEMBER";
			break; 
		}
		return name; 
	}
}
