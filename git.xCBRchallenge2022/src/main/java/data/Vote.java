package data;

public class Vote {
	
	private String id; 
	private double sim;
	private String month; 
	
	public Vote(String id, double sim, String month) {
		super();
		this.id = id;
		this.sim = sim;
		this.month = month;
	}

	public String getId() {
		return id;
	}


	public double getSim() {
		return sim;
	} 
	
	public String getMonth() {
		return month;
	}
}
