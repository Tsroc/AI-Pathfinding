package ie.gmit.sw.ai.personality;


import ie.gmit.sw.ai.nn.Utils;

public class NeuralPersonality implements Personality{
	private int hunger;
	private int fear;
	private int health;
	private PersonalityType personality;
	
	public NeuralPersonality(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public int determinePersonality() {
		double[] arr = {hunger, fear, health};
		double[] result;
		int classification = 0;
		
		try {
			result = TrainPersonalityNN.getNN().process(arr);
			classification = Utils.getMaxIndex(result);
			setPersonality(classification);
		} catch (Exception e) {
			System.out.println("x");
			e.printStackTrace();
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
				this.personality = PersonalityType.UNKNOWN;
		}
	}
	
	public PersonalityType getPersonality() {
		return personality;
	}

}
