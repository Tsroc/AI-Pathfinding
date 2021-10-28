package ie.gmit.sw.ai.personality;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;


public class EncogPersonality implements Personality {
	private int hunger;
	private int fear;
	private int health;
	private PersonalityType personality;
	
	public EncogPersonality(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public int determinePersonality() {
		double[] arr = {hunger, fear, health};
		int classification = 0;
		
		try {

			classification = EncogNeuralNetwork.getClassification(arr);
			setPersonality(classification);
		} catch (Exception e) {
			
		}
		
		return classification;
	}
	
	public void setPersonality(int p) {
		switch(p) {
			case 0:
				this.personality = PersonalityType.SCARED;
				break;
			case 1:
				this.personality = PersonalityType.FRIENDLY;
				break;
			case 2:
				this.personality = PersonalityType.AGGRESSIVE;
				break;
				default:
				this.personality = PersonalityType.FRIENDLY;
		}
	}
	
	public PersonalityType getPersonality() {
		return personality;
	}
}
