package de.ralleytn.plugins.jinput.xinput.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.ralleytn.plugins.jinput.xinput.XInputEnvironmentPlugin;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

class XInputPluginTest {

	@Test
	public void testGetControllers() {
		
		ControllerEnvironment environment = new XInputEnvironmentPlugin();
		
		if(!environment.isSupported()) {
			
			environment = ControllerEnvironment.getDefaultEnvironment();
		}
		
		Controller[] controllers = environment.getControllers();
		boolean hasXInputController = false;
		
		for(Controller controller : controllers) {
			
			if(controller.getClass().getSimpleName().equals("XIController")) {
				
				hasXInputController = true;
			}
			
			System.out.println(controller.getName() + ": " + controller.getClass().getName());
		}
		
		assertTrue(hasXInputController);
	}
	
	@Test
	public void testPolling() {
		
		ControllerEnvironment environment = new XInputEnvironmentPlugin();
		
		if(!environment.isSupported()) {
			
			environment = ControllerEnvironment.getDefaultEnvironment();
		}
		
		Controller[] controllers = environment.getControllers();
		Controller gamepad = null;
		
		for(Controller controller : controllers) {
			
			if(controller.getClass().getSimpleName().equals("XIController")) {
				
				gamepad = controller;
				break;
			}
		}
		
		assertNotNull(gamepad);
		
		boolean xPressed = false;
		boolean yPressed = false;
		boolean aPressed = false;
		boolean bPressed = false;
		boolean ltPushed = false;
		boolean rtPushed = false;
		boolean lthumbPressed = false;
		boolean rthumbPressed = false;
		boolean povUp = false;
		boolean povDown = false;
		boolean povLeft = false;
		boolean povRight = false;
		boolean povUpRight = false;
		boolean povUpLeft = false;
		boolean povDownRight = false;
		boolean povDownLeft = false;
		boolean lxPushedLeft = false;
		
		while(gamepad.poll()) {
			
			EventQueue queue = gamepad.getEventQueue();
			Event event = new Event();
			
			while(queue.getNextEvent(event)) {
				
				Component component = event.getComponent();
				float value = event.getValue();
				
				System.out.println("Name: " + component.getName() + ", ID: " + component.getIdentifier() + ", Value: " + value);
			}
		}
	}
}
