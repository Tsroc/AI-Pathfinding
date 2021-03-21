package ie.gmit.sw.ai.command;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.personality.PersonalityType;

public class CommandFactory {
	private static CommandFactory cf = new CommandFactory();

	private CommandFactory() {

	}

	public static CommandFactory getInstance() {
		return cf;
	}

	public Command getCommand(PersonalityType p, char enemyID, GameModel model, int row, int col) {
		return switch (p) {
			case AGGRESSIVE -> new AggressiveCommand(enemyID, model, row, col);
			case FRIENDLY -> new FriendlyCommand(enemyID, model, row, col);
			case SCARED -> new ScaredCommand(enemyID, model, row, col);
			case UNKNOWN -> new FriendlyCommand(enemyID, model, row, col);
		};
	}

}
