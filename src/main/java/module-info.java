module de.ralleytn.plugins.jinput.xinput {
	
	requires transitive jinput; // Warning exists because JInput was written in Java 8
	requires transitive java.desktop;
	requires de.ralleytn.wrapper.microsoft.xinput;
	
	exports de.ralleytn.plugins.jinput.xinput;
}