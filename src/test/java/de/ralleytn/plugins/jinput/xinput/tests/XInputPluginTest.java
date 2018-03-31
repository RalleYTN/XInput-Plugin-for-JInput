/*
 * MIT License
 * 
 * Copyright (c) 2018 Ralph Niemitz
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.ralleytn.plugins.jinput.xinput.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.ralleytn.plugins.jinput.xinput.XInputEnvironmentPlugin;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;

class XInputPluginTest {

	private static final Controller[] getControllers() {
		
		ControllerEnvironment environment = new XInputEnvironmentPlugin();
		
		if(!environment.isSupported()) {
			
			environment = ControllerEnvironment.getDefaultEnvironment();
		}
		
		return environment.getControllers();
	}
	
	private static final Controller getXInputGamepad(Controller[] controllers) {
		
		for(Controller controller : controllers) {
			
			if(controller.getClass().getSimpleName().equals("XIController")) {
				
				return controller;
			}
		}
		
		return null;
	}
	
	@Test
	public void testRumblers() {
		
		try {
			
			Controller gamepad = getXInputGamepad(getControllers());
			assertNotNull(gamepad);
			Rumbler[] rumblers = gamepad.getRumblers();
			
			for(Rumbler rumbler : rumblers) {
				
				System.out.println(rumbler.getAxisName() + ", " + rumbler.getAxisIdentifier());
			}
			
			System.out.println();
			rumblers[0].rumble(0.5F);
			Thread.sleep(2000);
			rumblers[1].rumble(0.5F);
			Thread.sleep(2000);
			rumblers[0].rumble(0);
			rumblers[1].rumble(0);
			
		} catch(InterruptedException exception) {}
	}
	
	@Test
	public void testGetControllers() {
		
		Controller[] controllers = getControllers();
		
		for(Controller controller : controllers) {

			System.out.println(controller.getName() + ": " + controller.getClass().getName());
		}

		System.out.println();
		assertNotNull(getXInputGamepad(controllers));
	}
	
	@Test
	public void testPolling() {
		
		Controller gamepad = getXInputGamepad(getControllers());
		assertNotNull(gamepad);
		
		XIControllerTestFrame frame = new XIControllerTestFrame();
		frame.setVisible(true);
		
		while(gamepad.poll() && frame.hasUncheckedItems()) {
			
			EventQueue queue = gamepad.getEventQueue();
			Event event = new Event();
			
			while(queue.getNextEvent(event)) {
				
				Component component = event.getComponent();
				float value = event.getValue();
				String name = component.getName();
				
				System.out.println("Name: " + name + ", Value: " + value);
				
				if(component.getClass().getSimpleName().equals("XIButton") && value == 1.0F) {
					
					frame.checkButton(name);
					
				} else if(name.equals("lt")) {
					
					if(value > 0.25F) frame.checkLT(25);
					if(value > 0.5F) frame.checkLT(50);
					if(value > 0.75F) frame.checkLT(75);
					if(value == 1.0F) frame.checkLT(100);
					
				} else if(name.equals("rt")) {
					
					if(value > 0.25F) frame.checkRT(25);
					if(value > 0.5F) frame.checkRT(50);
					if(value > 0.75F) frame.checkRT(75);
					if(value == 1.0F) frame.checkRT(100);
					
				} else if(name.equals("lx")) {
					
					if(value == -1.0F) {
						
						frame.checkLThumb("LEFT");
						
					} else if(value == 1.0F) {
						
						frame.checkLThumb("RIGHT");
					}
					
				} else if(name.equals("ly")) {
					
					if(value == -1.0F) {
						
						frame.checkLThumb("DOWN");
						
					} else if(value == 1.0F) {
						
						frame.checkLThumb("UP");
					}
					
				} else if(name.equals("rx")) {
					
					if(value == -1.0F) {
						
						frame.checkRThumb("LEFT");
						
					} else if(value == 1.0F) {
						
						frame.checkRThumb("RIGHT");
					}
					
				} else if(name.equals("ry")) {
					
					if(value == -1.0F) {
						
						frame.checkRThumb("DOWN");
						
					} else if(value == 1.0F) {
						
						frame.checkRThumb("UP");
					}
					
				} else if(name.equals("pov")) {
					
					       if(value == 0.125F) {frame.checkPOV("NORTH_WEST");
					} else if(value == 0.375F) {frame.checkPOV("NORTH_EAST");
					} else if(value == 0.875F) {frame.checkPOV("SOUTH_WEST");
					} else if(value == 0.625F) {frame.checkPOV("SOUTH_EAST");
					} else if(value == 0.25F) {frame.checkPOV("NORTH");
					} else if(value == 1.0F) {frame.checkPOV("WEST");
					} else if(value == 0.5F) {frame.checkPOV("EAST");
					} else if(value == 0.75F) {frame.checkPOV("SOUTH");
					}
				}
			}
		}
		
		System.out.println();
		frame.dispose();
	}
}
