package data;


public class Weather {
	private int day; 
	private int month;
	private int year;
	private double temp_max; 
	private double temp_min; 
	private double temp_avg; 
	private double pres_avg; 
	private double pres_max; 
	private double pres_min; 
	private double hum_avg; 
	private double hum_max; 
	private double hum_min;
	private double sim; 
	

	public Weather(int day, int month, int year, double temp_max, double temp_min, double temp_avg, double pres_avg,
			double pres_max, double pres_min, double hum_avg, double hum_max, double hum_min) {
		super();
		this.day = day;
		this.month = month;
		this.year = year;
		this.temp_max = temp_max;
		this.temp_min = temp_min;
		this.temp_avg = temp_avg;
		this.pres_avg = pres_avg;
		this.pres_max = pres_max;
		this.pres_min = pres_min;
		this.hum_avg = hum_avg;
		this.hum_max = hum_max;
		this.hum_min = hum_min;
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getTemp_max() {
		return temp_max;
	}
	public void setTemp_max(double temp_max) {
		this.temp_max = temp_max;
	}
	public double getTemp_min() {
		return temp_min;
	}
	public void setTemp_min(double temp_min) {
		this.temp_min = temp_min;
	}
	public double getTemp_avg() {
		return temp_avg;
	}
	public void setTemp_avg(double temp_avg) {
		this.temp_avg = temp_avg;
	}
	public double getPres_avg() {
		return pres_avg;
	}
	public void setPres_avg(double pres_avg) {
		this.pres_avg = pres_avg;
	}
	public double getPres_max() {
		return pres_max;
	}
	public void setPres_max(double pres_max) {
		this.pres_max = pres_max;
	}
	public double getPres_min() {
		return pres_min;
	}
	public void setPres_min(double pres_min) {
		this.pres_min = pres_min;
	}
	public double getHum_avg() {
		return hum_avg;
	}
	public void setHum_avg(double hum_avg) {
		this.hum_avg = hum_avg;
	}
	public double getHum_max() {
		return hum_max;
	}
	public void setHum_max(double hum_max) {
		this.hum_max = hum_max;
	}
	public double getHum_min() {
		return hum_min;
	}
	public void setHum_min(double hum_min) {
		this.hum_min = hum_min;
	}
	public double getSimilarity() {
		return sim;
	}
	public void setSimilarity(double d) {
		this.sim = d;
	}
	

	
}
