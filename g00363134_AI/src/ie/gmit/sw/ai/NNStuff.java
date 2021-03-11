package ie.gmit.sw.ai;


import ie.gmit.sw.ai.nn.Utils;

public class NNStuff implements Command {
	private int personality;
	private int hunger;
	private int fear;
	private int health;
	
	public NNStuff(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public int getPersonality() throws Exception {
		double[] arr = {hunger, fear, health};
		double[] result;
		int classification = 0;
		
		result = NNTrain.getNN().process(arr);
		classification = Utils.getMaxIndex(result);
		this.personality = classification;
		
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
			

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
