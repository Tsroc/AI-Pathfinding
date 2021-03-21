package ie.gmit.sw.ai.personality;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyPersonality implements Personality{
	
	private static final String FILE = "./resources/fuzzy/enemy.fcl";
	private int hunger;
	private int fear;
	private int health;
	private PersonalityType personality;

	public FuzzyPersonality(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public int determinePersonality() {
		FIS fis = FIS.load(FILE, true); //Load and parse the FCL
		
		FunctionBlock fb = fis.getFunctionBlock("EnemyLogic");
		
		
		//JFuzzyChart.get().chart(fb); //Display the linguistic variables and terms
		fis.setVariable("hunger", this.hunger); //Apply a value to a variable
		fis.setVariable("fear", this.fear); //Apply a value to a variable
		fis.setVariable("health", this.health); //Apply a value to a variable
		fis.evaluate(); //Execute the fuzzy inference engine
		
		Variable action = fb.getVariable("personality");
		//JFuzzyChart.get().chart(action, action.getDefuzzifier(), true);
		
		setPersonality((int)action.getValue());
		return (int)action.getValue();
	}
	
	public void setPersonality(int p) {
		if(p > 55)
			this.personality = PersonalityType.AGGRESSIVE;
		else if (p > 35)
			this.personality = PersonalityType.FRIENDLY;
		else if (p >= 0)
			this.personality = PersonalityType.SCARED;
		else
			this.personality = PersonalityType.UNKNOWN;
	}
	
	public PersonalityType getPersonality() {
		return personality;
	}
	
}

