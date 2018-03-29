package de.ralleytn.plugins.jinput.xinput;

import java.io.IOException;

import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Rumbler;

public class XInputController extends AbstractController {

	private int dwUserIndex = 0;
	
	XInputController(int dwUserIndex, String name, Component[] components, Controller[] children, Rumbler[] rumblers) {
		
		super(name, components, children, rumblers);
		
		this.dwUserIndex = dwUserIndex;
	}

	@Override
	protected boolean getNextDeviceEvent(Event event) throws IOException {
		
		return false;
	}
}
