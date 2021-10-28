package ie.gmit.sw.ai.command;

import ie.gmit.sw.ai.personality.PersonalityType;

public class CommandFactory {
	private static CommandFactory cf = new CommandFactory();

	private CommandFactory() {

	}

	public static CommandFactory getInstance() {
		return cf;
	}

	public Command getCommand(PersonalityType p, char enemyID, int row, int col) {
		return switch (p) {
			case AGGRESSIVE -> new AggressiveCommand(enemyID, row, col);
			case FRIENDLY -> new FriendlyCommand(enemyID, row, col);
			case SCARED -> new ScaredCommand(enemyID, row, col);
		};
	}

}
