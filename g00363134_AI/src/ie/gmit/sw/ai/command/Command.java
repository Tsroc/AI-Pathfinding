package ie.gmit.sw.ai.command;

/*
 * Use implementations of this functional interface to specify
 * how a computer controlled game character should behave.
 */

@FunctionalInterface
public interface Command {
	public int execute(boolean alive);
}