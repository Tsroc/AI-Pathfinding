package ie.gmit.sw.ai;


import ie.gmit.sw.ai.nn.Utils;

public class PersonalityNN implements Personality{
	private int personality;
	private int hunger;
	private int fear;
	private int health;
	
	public PersonalityNN(int hunger, int fear, int health) {
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
			this.personality = classification;
		} catch (Exception e) {
			System.out.println("x");
			e.printStackTrace();
		}
		
		return classification;
	}
	
	public String toString() {
		String str = "Unknown";
		
		switch(this.personality) {
		case 2:
			str = "Aggressive";
			break;
		case 1:
			str = "Friendly";
			break;
		case 0:
			str = "Scared";
			break;
		}
		return str;
	}

}
