module gmit.software {
	requires javafx.base;
	requires javafx.graphics;
	requires transitive javafx.controls;
	requires jFuzzyLogic;
	
	requires java.desktop;
	requires encog.core;
	
	exports ie.gmit.sw.ai;
	exports ie.gmit.sw.ai.nn;
	exports ie.gmit.sw.ai.nn.activator;
}