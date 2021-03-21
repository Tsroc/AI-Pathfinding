package ie.gmit.sw.ai.personality;

public class PersonalityFactory {
	private static PersonalityFactory pf = new PersonalityFactory();

	private PersonalityFactory() {

	}

	public static PersonalityFactory getInstance() {
		return pf;
	}

	public Personality getPersonality(PersonalityCreatorType p, int hunger, int fear, int health) {
		return switch (p) {
			case NN -> new NeuralPersonality(hunger, fear, health);
			case FL -> new FuzzyPersonality(hunger, fear, health);
		};
	}
}
