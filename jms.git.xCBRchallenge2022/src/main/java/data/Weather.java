package data;

public class Weather {
	private int day; 
	private int month;
	private int year;
	private float temp_max; 
	private float temp_min; 
	private float temp_avg; 
	private float pres_avg; 
	private float pres_max; 
	private float pres_min; 
	private float hum_avg; 
	private float hum_max; 
	private float hum_min;
	
	public Weather(int day, int month, int year, float temp_max, float temp_min, float temp_avg, float pres_avg,
			float pres_max, float pres_min, float hum_avg, float hum_max, float hum_min) {
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
	public float getTemp_max() {
		return temp_max;
	}
	public void setTemp_max(float temp_max) {
		this.temp_max = temp_max;
	}
	public float getTemp_min() {
		return temp_min;
	}
	public void setTemp_min(float temp_min) {
		this.temp_min = temp_min;
	}
	public float getTemp_avg() {
		return temp_avg;
	}
	public void setTemp_avg(float temp_avg) {
		this.temp_avg = temp_avg;
	}
	public float getPres_avg() {
		return pres_avg;
	}
	public void setPres_avg(float pres_avg) {
		this.pres_avg = pres_avg;
	}
	public float getPres_max() {
		return pres_max;
	}
	public void setPres_max(float pres_max) {
		this.pres_max = pres_max;
	}
	public float getPres_min() {
		return pres_min;
	}
	public void setPres_min(float pres_min) {
		this.pres_min = pres_min;
	}
	public float getHum_avg() {
		return hum_avg;
	}
	public void setHum_avg(float hum_avg) {
		this.hum_avg = hum_avg;
	}
	public float getHum_max() {
		return hum_max;
	}
	public void setHum_max(float hum_max) {
		this.hum_max = hum_max;
	}
	public float getHum_min() {
		return hum_min;
	}
	public void setHum_min(float hum_min) {
		this.hum_min = hum_min;
	}
	
	

	
}
