package agents;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import data.Vote;
import data.Weather;
import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.DoubleDesc;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.DoubleFct;
import de.dfki.mycbr.core.similarity.IntegerFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.similarity.config.AmalgamationConfig;
import de.dfki.mycbr.core.similarity.config.NumberConfig;
import de.dfki.mycbr.io.XMLExporter;
import de.dfki.mycbr.util.Pair;
import util.CSVmanager;
import util.DateFormatter;
import util.Stats;

/** Agent, which is built with a given month, e.g., 
 * NovemberAgent who stores all data to its corresponding month. 
 * 
 * @author Jakob
 */
public class MonthAgent extends Agent {
	
	/** Debugger variable. If true: More output on the console. */
	private boolean verbose = true; 
	
	private AmalgamationFct amalFunction;
	
	/** weights for all attributes.*/
	private int[] weights = new int[12]; 
	private static final int weightFactor = 1; 
	
	/** List of Weather Data which is usually used to be filled with Weather-Cases of the agents casebase.  */
	private ArrayList<Weather> weatherData; 
	private Weather queryWeather; 
	private String month; 
	
	// myCBR variables
	protected Project project;
	protected Concept weatherConcept;
	protected ICaseBase casebase;
	protected Retrieval retrieve;
	
	private IntegerDesc dayDesc; 
	private IntegerDesc monthDesc; 
	private IntegerDesc yearDesc; 
	private DoubleDesc temp_maxDesc; 
	private DoubleDesc temp_minDesc; 
	private DoubleDesc temp_avgDesc; 
	private DoubleDesc pres_avgDesc; 
	private DoubleDesc pres_maxDesc; 
	private DoubleDesc pres_minDesc; 
	private DoubleDesc hum_avgDesc; 
	private DoubleDesc hum_maxDesc; 
	private DoubleDesc hum_minDesc; 
	
	DecimalFormat df = new DecimalFormat("#.#######");
	
	
	/** Constructor to build a myCBR project (myCBR.prj - File), including other files (.cb / .exp /...) 
	 * The agent shall be filled with a list of type Weather (which contains weather data) and initializes
	 * based on this data all knowledge containers (casebase, vocabulary, similarity measure, adaptation knowledge)
	 */
	public MonthAgent(ArrayList<Weather> weather, String month) {
		this.weatherData = weather; 
		this.month = month;
		initProject(); 
		initCaseBase(); 
	}
	
	
	/** Contructor to import an already existing myCBR project. Shall be used to build an agent and 
	 * start a query based on the agents casebase. The agent will only contain cases of its given 
	 * month. 
	 */
	public MonthAgent(String month) {
		this.month = month;
		importProject();
	}
	
	
	/** Importing an already existing myCBR project. Might take a while. 
	 * In case of errors, increase the Thread sleeping time, especially for larger projects. 
	 */
	private boolean importProject() {
		try {
			if (verbose) 
				System.out.println("[MonthAgent] Import project: " + dataPath + month + projectName);
			project = new Project(dataPath + month + projectName);
			Thread.sleep(10000);
			if (verbose) 
				System.out.println("[MonthAgent] Woke up!");
			weatherConcept = project.getConceptByID(month);
			casebase = project.getCB(month);
			if (verbose) 
				System.out.println("[MonthAgent] Project import complete.");
			return true;
		} catch (Exception e) {
			System.err.println("[MonthAgent.importProject()] Projectpath not found.");
			return false;
		}
	}
	
	
	/** Initializing a casebase based on the given data in the agents constructor. */
	public boolean initCaseBase() {
		try {
			if (verbose)
				System.out.println("[MonthAgent] Initialize casebase...");
			casebase = project.createDefaultCB(month);
			addCases();
			XMLExporter.save(project, dataPath + month + projectName);
			if (verbose)
				System.out.println("[MonthAgent] Casebase initialized!");
		} catch (Exception e) {
			System.err.println("[AnalysisAgent.initCasebase()]: Projectpath not found."
					+ " Possible reason: Do you have permission to write at given path?");
			return false;
		}
		return true;
	}
	
	
	/** Initializing a project file, especially the similarity measurements and weights. 
	 */
	public boolean initProject() {
		try {
			// Start in modeling view	
			if (verbose)
				System.out.println("[MonthAgent] Initialize project...");
			project = new Project();
			project.setName(month);
			project.setAuthor("JMS");
			
			// Create a new concept
			weatherConcept = project.createTopConcept(month);

			// Define the global similarities
			amalFunction = weatherConcept.addAmalgamationFct(AmalgamationConfig.WEIGHTED_SUM, month + "Fct", true);
			
			// Initialize weights
			int[] tempWeights = {
					1,		// day
					1,		// month
					1,		// year
					1, 		// temp_max
					1,		// temp_min
					1, 		// temp_avg
					1, 		// pres_avg
					1, 		// pres_max
					1,		// pres_min
					1, 		// hum_avg
					1, 		// hum_max
					1 		// hum_min	
			};
			
			for (int j = 0; j < tempWeights.length; j++) {
				tempWeights[j] = tempWeights[j] * weightFactor;
			}
			weights = tempWeights; 
			
			int i = 0;
			
			
			// TODO: Cyclic behaviour needs to be considered (31 similar to 1)
			dayDesc = new IntegerDesc(weatherConcept, "day", 1, 31); 
			IntegerFct dayFct = dayDesc.addIntegerFct("dayFct", true); 
			dayFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			dayFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			dayFct.setFunctionParameterL(1);
			dayFct.setFunctionParameterR(1);
			amalFunction.setWeight("day", weights[i++]);
			
			// TODO: Cyclic behaviour needs to be considered (December similar to January)
			monthDesc = new IntegerDesc(weatherConcept, "month", 1, 31); 
			IntegerFct monthFct = monthDesc.addIntegerFct("monthFct", true); 
			monthFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			monthFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			monthFct.setFunctionParameterL(1);
			monthFct.setFunctionParameterR(1);
			amalFunction.setWeight("month", weights[i++]);
			
			// 2000 - 2018 is the range of the given dataset
			yearDesc = new IntegerDesc(weatherConcept, "year", 2000, 2018); 
			IntegerFct yearFct = yearDesc.addIntegerFct("yearFct", true); 
			yearFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			yearFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			yearFct.setFunctionParameterL(1);
			yearFct.setFunctionParameterR(1);
			amalFunction.setWeight("year", weights[i++]);
			
			temp_maxDesc = new DoubleDesc(weatherConcept, "temp_max", 0, 50);
			DoubleFct temp_maxFct = temp_maxDesc.addDoubleFct("temp_maxFct", true); 
			temp_maxFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			temp_maxFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			temp_maxFct.setFunctionParameterL(1);
			temp_maxFct.setFunctionParameterR(1);
			amalFunction.setWeight("temp_max", weights[i++]);

			temp_minDesc = new DoubleDesc(weatherConcept, "temp_min", 0, 50);
			DoubleFct temp_minFct = temp_minDesc.addDoubleFct("temp_minFct", true); 
			temp_minFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			temp_minFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			temp_minFct.setFunctionParameterL(1);
			temp_minFct.setFunctionParameterR(1);
			amalFunction.setWeight("temp_min", weights[i++]);
			
			temp_avgDesc = new DoubleDesc(weatherConcept, "temp_avg", 0, 50);
			DoubleFct temp_avgFct = temp_avgDesc.addDoubleFct("temp_avgFct", true); 
			temp_avgFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			temp_avgFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			temp_avgFct.setFunctionParameterL(1);
			temp_avgFct.setFunctionParameterR(1);
			amalFunction.setWeight("temp_avg", weights[i++]);
			
			pres_avgDesc = new DoubleDesc(weatherConcept, "pres_avg", 0, 50);
			DoubleFct pres_avgFct = pres_avgDesc.addDoubleFct("pres_avgFct", true); 
			pres_avgFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			pres_avgFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			pres_avgFct.setFunctionParameterL(1);
			pres_avgFct.setFunctionParameterR(1);
			amalFunction.setWeight("pres_avg", weights[i++]);
			
			pres_maxDesc = new DoubleDesc(weatherConcept, "pres_max", 0, 50);
			DoubleFct pres_maxFct = pres_maxDesc.addDoubleFct("pres_maxFct", true); 
			pres_maxFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			pres_maxFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			pres_maxFct.setFunctionParameterL(1);
			pres_maxFct.setFunctionParameterR(1);
			amalFunction.setWeight("pres_max", weights[i++]);
			
			pres_minDesc = new DoubleDesc(weatherConcept, "pres_min", 0, 50);
			DoubleFct pres_minFct = pres_minDesc.addDoubleFct("pres_minFct", true); 
			pres_minFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			pres_minFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			pres_minFct.setFunctionParameterL(1);
			pres_minFct.setFunctionParameterR(1);
			amalFunction.setWeight("pres_min", weights[i++]);
			
			hum_avgDesc = new DoubleDesc(weatherConcept, "hum_avg", 15, 120);
			DoubleFct hum_avgFct = hum_avgDesc.addDoubleFct("hum_avgFct", true); 
			hum_avgFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			hum_avgFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			hum_avgFct.setFunctionParameterL(1);
			hum_avgFct.setFunctionParameterR(1);
			amalFunction.setWeight("hum_avg", weights[i++]);
			
			hum_maxDesc = new DoubleDesc(weatherConcept, "hum_max", 15, 120);
			DoubleFct hum_maxFct = hum_maxDesc.addDoubleFct("hum_maxFct", true); 
			hum_maxFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			hum_maxFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			hum_maxFct.setFunctionParameterL(1);
			hum_maxFct.setFunctionParameterR(1);
			amalFunction.setWeight("hum_max", weights[i++]);
			
			hum_minDesc = new DoubleDesc(weatherConcept, "hum_min", 15, 120);
			DoubleFct hum_minFct = hum_minDesc.addDoubleFct("hum_minFct", true); 
			hum_minFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
			hum_minFct.setFunctionTypeR(NumberConfig.POLYNOMIAL_WITH);
			hum_minFct.setFunctionParameterL(1);
			hum_minFct.setFunctionParameterR(1);
			amalFunction.setWeight("hum_min", weights[i++]);
			

			if (verbose)
				System.out.println("[MonthAgent] Project initialized!");

		} catch (Exception e) {
			System.err.println("[MonthAgent.initProject()] Agent: Project could not be created." + e.getMessage());
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/** Adding cases to a new casebase, based on the given data in the constructor of the agent. */
	protected void addCases() {
		
		if (verbose)
			System.out.println("[MonthAgent] Filling casebase...");
		
		int i = 0; 
		for (Weather weather : weatherData) {
			try {
				Instance instance = weatherConcept.addInstance(weather.getYear() + "/" + weather.getMonth() + "/" + weather.getDay());
				instance.addAttribute(dayDesc, weather.getDay());
				instance.addAttribute(monthDesc, weather.getMonth());
				instance.addAttribute(yearDesc, weather.getYear());
				instance.addAttribute(temp_maxDesc, weather.getTemp_max());
				instance.addAttribute(temp_minDesc, weather.getTemp_min());
				instance.addAttribute(temp_avgDesc, weather.getTemp_avg());
				instance.addAttribute(pres_avgDesc, weather.getPres_avg());
				instance.addAttribute(pres_minDesc, weather.getPres_min());
				instance.addAttribute(pres_maxDesc, weather.getPres_max());
				instance.addAttribute(hum_avgDesc, weather.getHum_avg());
				instance.addAttribute(hum_maxDesc, weather.getHum_max());
				instance.addAttribute(hum_minDesc, weather.getHum_min());

				casebase.addCase(instance);
				i++;
				if (verbose)
					System.out.println("[MonthAgent] Case " + weather.getYear() + "/" + weather.getMonth() + "/" + weather.getDay() + ", Temp(avg): " + weather.getTemp_avg() + " added.");

			} catch (Exception e) {
				System.err.println("[MonthAgent] Error while adding new cases!");
				e.printStackTrace();
				System.exit(0);
			}
		}
		if (verbose)
			System.out.println("[MonthAgent] Casebase filled with " + i + " cases.");
	}
	
	@Override
	public void changeWeights() {
		// TODO Auto-generated method stub

	}
	
	
	/** Starting a query to the given myCBR project. 
	 */
	public List<Pair<Instance, Similarity>> startQuery(Weather weather) {
		queryWeather = weather; 
		dayDesc = (IntegerDesc) this.weatherConcept.getAllAttributeDescs().get("day"); 
		monthDesc = (IntegerDesc) this.weatherConcept.getAllAttributeDescs().get("month"); 
		yearDesc = (IntegerDesc) this.weatherConcept.getAllAttributeDescs().get("year"); 
		temp_maxDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("temp_max"); 
		temp_minDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("temp_min"); 
		temp_avgDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("temp_avg"); 
		pres_maxDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("pres_max"); 
		pres_minDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("pres_min"); 
		pres_avgDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("pres_avg"); 
		hum_maxDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("hum_max"); 
		hum_minDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("hum_min"); 
		hum_avgDesc = (DoubleDesc) this.weatherConcept.getAllAttributeDescs().get("hum_avg"); 

		try {
			retrieve = new Retrieval(weatherConcept, casebase);
			retrieve.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
			Instance query = retrieve.getQueryInstance();
			query.addAttribute(dayDesc, dayDesc.getAttribute(weather.getDay()));
			query.addAttribute(monthDesc, monthDesc.getAttribute(weather.getMonth()));
			query.addAttribute(yearDesc, yearDesc.getAttribute(weather.getYear()));
			query.addAttribute(temp_maxDesc, temp_maxDesc.getAttribute(weather.getTemp_max()));
			query.addAttribute(temp_minDesc, temp_minDesc.getAttribute(weather.getTemp_min()));
			query.addAttribute(temp_avgDesc, temp_avgDesc.getAttribute(weather.getTemp_avg()));
			query.addAttribute(pres_avgDesc, pres_avgDesc.getAttribute(weather.getPres_avg()));
			query.addAttribute(pres_maxDesc, pres_maxDesc.getAttribute(weather.getPres_max()));
			query.addAttribute(pres_minDesc, pres_minDesc.getAttribute(weather.getPres_min()));
			query.addAttribute(hum_avgDesc, hum_avgDesc.getAttribute(weather.getHum_avg()));
			query.addAttribute(hum_maxDesc, hum_maxDesc.getAttribute(weather.getHum_max()));
			query.addAttribute(hum_minDesc, hum_minDesc.getAttribute(weather.getHum_min()));
		} catch (ParseException e) {
			System.err.println("[MonthAgent] Error while creating the query! " + e.getMessage());
		}

		// Send query
		if (verbose)
			System.out.println("[MonthAgent] Starting Query...");
		retrieve.start();
		if (verbose)
			System.out.println("[MonthAgent] Query successful!");
		return retrieve.getResult();
	}
	
	
	/** Based on the results of the Retrieval, this method prints out the n most similar cases. 
	 * The String Formatter function has been used to provide the user with a visually appealing overview.
	 */
	public void print(List<Pair<Instance, Similarity>> result, int numberOfBestCases) {
		ArrayList<Weather> resultingWeather = new ArrayList<Weather>();
		
		// result is already ordered, so we can just loop over the results. 
		for (int i = 0; i < numberOfBestCases; i++) {
			Instance obj = weatherConcept.getInstance(result.get(i).getFirst().getName()); 
			// wrap a package to have all the data in one object
			Weather weather = new Weather(
					Integer.parseInt(obj.getAttForDesc(dayDesc).getValueAsString()), 
					Integer.parseInt(obj.getAttForDesc(monthDesc).getValueAsString()), 
					Integer.parseInt(obj.getAttForDesc(yearDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(temp_maxDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(temp_minDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(temp_avgDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(pres_avgDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(pres_maxDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(pres_minDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(hum_avgDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(hum_maxDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(hum_minDesc).getValueAsString())
					);
			
			resultingWeather.add(weather);
			resultingWeather.get(i).setSimilarity(result.get(i).getSecond().getValue());
		}

		// Preparing to build the (beautiful! ;)) data table
		Stats stats = new Stats(resultingWeather);
		String line = "+----------------------------";
		String leftAlignFormat = "%20s ";
		for (int j = 0; j < resultingWeather.size(); j++) {
			line += "---------------------------------";
		}

		int k = 0;
		String name = "";
		// Defining row names and horizontal lines)
		for (int i = -1; i < 15; i++) {
			if (i == -1) {
				leftAlignFormat = "%25s ";
				System.out.format(leftAlignFormat, line);
			}
			if (i == 0) {
				leftAlignFormat = "| %25s |";
				System.out.format(leftAlignFormat, "Attribute");
				k++;
			}
			if (i == 1) {
				leftAlignFormat = "%25s ";
				System.out.format(leftAlignFormat, line);
			}
			if (i >= 2) {
				leftAlignFormat = "| %25s |";
			}
			if (i == 2) {
				System.out.format(leftAlignFormat, "day");
			}
			if (i == 3) {
				System.out.format(leftAlignFormat, "month");
			}
			if (i == 4) {
				System.out.format(leftAlignFormat, "year");
			}
			if (i == 5) {
				System.out.format(leftAlignFormat, "temp_max");
			}
			if (i == 6) {
				System.out.format(leftAlignFormat, "temp_min");
			}
			if (i == 7) {
				System.out.format(leftAlignFormat, "temp_avg");
			}
			if (i == 8) {
				System.out.format(leftAlignFormat, "pres_avg");
			}
			if (i == 9) {
				System.out.format(leftAlignFormat, "pres_max");
			}
			if (i == 10) {
				System.out.format(leftAlignFormat, "pres_min");
			}
			if (i == 11) {
				System.out.format(leftAlignFormat, "hum_avg");
			}
			if (i == 12) {
				System.out.format(leftAlignFormat, "hum_max");
			}
			if (i == 13) {
				System.out.format(leftAlignFormat, "hum_min");
			}

			
			// Printing out the query as a reminder/comparison to the most similar cases
			if (queryWeather != null) {
				ArrayList<Weather> queryList = new ArrayList<Weather>();
				queryList.add(queryWeather); 
				stats.printAttribute(queryList, i, "QUERY: " + queryWeather.getYear() + "/" + queryWeather.getMonth() + "/" + queryWeather.getDay(), 0);
			} else {
				System.err.println("QueryPacket == null");
			}
			
			// Printing out the most similar cases and their attribute-value-pairs
			for (k = 0; k < resultingWeather.size(); k++) {
				name = "Case: " + result.get(k).getFirst().getName() + " ("
						+ df.format(result.get(k).getSecond().getValue()) + ")";
				stats.printAttribute(resultingWeather, i, name, k);
			}
			System.out.println();
		}
		// Finish line
		System.out.format(line + "%n");
		System.out.println("");
	}
	
	
	public ArrayList<Vote> vote(List<Pair<Instance, Similarity>> result, int numberOfBestCases) {
		ArrayList<Vote> votes = new ArrayList<Vote>(); 
		ArrayList<Weather> resultingWeather = new ArrayList<Weather>();
		
		for (int i = 0; i < numberOfBestCases; i++) {
			Instance obj = weatherConcept.getInstance(result.get(i).getFirst().getName()); 
			// wrap a package to have all the data in one object
			Weather weather = new Weather(
					Integer.parseInt(obj.getAttForDesc(dayDesc).getValueAsString()), 
					Integer.parseInt(obj.getAttForDesc(monthDesc).getValueAsString()), 
					Integer.parseInt(obj.getAttForDesc(yearDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(temp_maxDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(temp_minDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(temp_avgDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(pres_avgDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(pres_maxDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(pres_minDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(hum_avgDesc).getValueAsString()),
					Double.parseDouble(obj.getAttForDesc(hum_maxDesc).getValueAsString()), 
					Double.parseDouble(obj.getAttForDesc(hum_minDesc).getValueAsString())
					);
			resultingWeather.add(weather);
			resultingWeather.get(i).setSimilarity(result.get(i).getSecond().getValue());
		}
		for (int k = 0; k < resultingWeather.size(); k++) {
			Vote vote = new Vote(
					result.get(k).getFirst().getName(), 
					result.get(k).getSecond().getValue(), 
					DateFormatter.numberToMonthname(resultingWeather.get(k).getMonth())
					); 
			if (verbose)
				System.out.println("Vote " + (k+1) + ".) " + vote.getId() + " (" + vote.getSim() + ")");
			votes.add(vote); 
		}
		return votes; 
	}
	
	// Main method for testing purposes
	// Just copy one line of the data table (excel file) and insert the data as arguments to the agent. 
	// Remember to set String values. 
	public static void main(String[] args) {
		
		// TEST INIT PROJECT/CASEBASE
		ArrayList<Weather> content = new ArrayList<Weather>();
		content = CSVmanager.readCSV(dataPath + "weatherdata.csv", "NOVEMBER");
		MonthAgent ma = new MonthAgent(content, "NOVEMBER"); 
	
		
		// TEST IMPORT + QUERY
		MonthAgent agent = new MonthAgent("NOVEMBER");
		Weather weather = new Weather(2, 11, 2000, 33.3, 20.0, 26.65, 23.9, 26.1, 21.2, 74.0, 96.0, 50.0);
		List<Pair<Instance, Similarity>> queryResult = agent.startQuery(weather);
		agent.print(queryResult, 10);
		
	}

}
