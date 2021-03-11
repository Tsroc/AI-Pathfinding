package ie.gmit.sw.ai;

public class CharacterTaskFuzzy extends CharacterTask{
	
	private Command cmd;
	private int health;
	private int fear;
	private int hunger;
	private double personality;
	
	public CharacterTaskFuzzy(GameModel model, char enemyID, int row, int col, int health, int fear, int hunger) {
		// TODO Auto-generated constructor stub
		super(model, enemyID, row, col);
		this.health = health;
		this.fear = fear;
		this.hunger = hunger;
		this.cmd = new FuzzyStuff(health, fear, hunger);
		
		super.setCmd(cmd);
		this.personality = ((FuzzyStuff) super.getCmd()).getPersonality();
		
		System.out.printf("Personality: %s(%.2f)\n", super.getCmd().toString(), this.personality);
		System.out.printf("\t[health: %d, Fear: %d, Hunger: %d]\n", health, fear, hunger);
		
	}
}
