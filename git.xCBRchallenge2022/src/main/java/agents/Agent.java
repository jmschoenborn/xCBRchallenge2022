package agents;

import java.util.List;

import data.Weather;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;

/** Build plan for an agent. Sets all necessary methods as required for new agents and defines
 * the path to save and load the myCBR project file. 
 * @author Jakob
 */
public abstract class Agent {
	protected static String dataPath = "D:\\";
	protected static String projectName = ".prj";
	
	public abstract boolean initCaseBase();
	public abstract boolean initProject();
	public abstract void changeWeights();
	public abstract List<Pair<Instance, Similarity>> startQuery(Weather weather); 
	public abstract void print(List<Pair<Instance, Similarity>> result, int numberOfBestCases); 
	protected abstract void addCases();
}
