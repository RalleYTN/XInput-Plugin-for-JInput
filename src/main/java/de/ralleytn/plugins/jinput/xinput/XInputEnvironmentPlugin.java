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
package de.ralleytn.plugins.jinput.xinput;

import java.util.ArrayList;
import java.util.List;

import de.ralleytn.wrapper.microsoft.xinput.XInput;
import de.ralleytn.wrapper.microsoft.xinput.XInputState;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.DirectAndRawInputEnvironmentPlugin;
import net.java.games.util.plugins.Plugin;

/**
 * Provides full support for XInput gamepads.
 * This plugin will fetch the XInput gamepads and let the {@linkplain DirectAndRawInputEnvironmentPlugin} fetch all other devices.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class XInputEnvironmentPlugin extends ControllerEnvironment implements Plugin {

	protected static final XInput XINPUT = isWindows() ? XInput.create() : null;
	
	private Controller[] controllers;
	private boolean supported;
	private List<Integer> alreadyUsedUserIndices;
	
	/**
	 * @since 1.0.0
	 */
	public XInputEnvironmentPlugin() {

		this.supported = XINPUT != null;
			
		if(this.supported) {
				
			this.alreadyUsedUserIndices = new ArrayList<>();
			this.controllers = this.createControllers();
		}
	}
	
	@Override
	public final Controller[] getControllers() {
		
		return this.controllers;
	}

	@Override
	public final boolean isSupported() {
		
		return this.supported;
	}
	
	private final int getNextUserIndex() {
		
		for(int dwUserIndex = 0; dwUserIndex < XInput.XUSER_MAX_COUNT; dwUserIndex++) {
			
			int code = XINPUT.XInputGetState(dwUserIndex, new XInputState());
			boolean connected = (code == XInput.ERROR_SUCCESS);
			
			if(!this.alreadyUsedUserIndices.contains(dwUserIndex) && connected) {
				
				this.alreadyUsedUserIndices.add(dwUserIndex);
				return dwUserIndex;
			}
		}
		
		return -1;
	}
	
	private final Controller[] createControllers() {
		
		// Step 1: Let the DirectAndRawInputEnvironmentPlugin fetch some Controllers.
		// Step 2: Replace the Controllers that are most likely XInput Controllers with the Controllers of this plugin.
		
		Controller[] defaultControllers = new DirectAndRawInputEnvironmentPlugin().getControllers();
		Controller[] controllers = new Controller[defaultControllers.length];
		
		for(int index = 0; index < defaultControllers.length; index++) {
			
			boolean replaced = false;
			Controller controller = defaultControllers[index];
			
			if(isXInput(controller)) {
				
				int dwUserIndex = this.getNextUserIndex();
				
				if(dwUserIndex != -1) {
					
					controllers[index] = new XIController(dwUserIndex, controller.getName(), this.createComponents(), this.createRumblers());
					replaced = true;
				}
			}
			
			if(!replaced) {
				
				controllers[index] = controller;
			}
		}
		
		return controllers;
	}
	
	private final XIComponent[] createComponents() {
		
		return new XIComponent[] {
				
			new XIButton(Button._0),
			new XIButton(Button._1),
			new XIButton(Button._2),
			new XIButton(Button._3),
			new XIButton(Button._4),
			new XIButton(Button._5),
			new XIButton(Button._6),
			new XIButton(Button._7),
			new XIButton(Button._8),
			new XIButton(Button._9),
			new XIPOV(),
			new XITrigger(Axis.Z),
			new XITrigger(Axis.RZ),
			new XILeftThumbStick(Axis.X),
			new XILeftThumbStick(Axis.Y),
			new XIRightThumbStick(Axis.RX),
			new XIRightThumbStick(Axis.RY)
		};
	}
	
	private final XIRumbler[] createRumblers() {
		
		return new XIRumbler[] {
				
			new XIRumbler(Axis.X),
			new XIRumbler(Axis.RX)
		};
	}
	
	private static final boolean isXInput(Controller controller) {
		
		// From what I know all XInput gamepads have a POV, a left X and Y axis, a right X and Y axis,
		// a z axis for a trigger and the GAMEPAD type when they are read by the DirectAndRawEnvionmentPlugin.
		// So by checking if all these things are true I can find out if I should replace the Controller by a XIController.
		
		Component[] components = controller.getComponents();
		
		boolean hasXAxis = false;
		boolean hasYAxis = false;
		boolean hasZAxis = false;
		boolean hasRXAxis = false;
		boolean hasRYAxis = false;
		boolean hasPOV = false;
		
		for(Component component : components) {
			
			Identifier id = component.getIdentifier();
			
				   if(Axis.X.equals(id))   {hasXAxis = true;
			} else if(Axis.Y.equals(id))   {hasYAxis = true;
			} else if(Axis.RX.equals(id))  {hasRXAxis = true;
			} else if(Axis.RY.equals(id))  {hasRYAxis = true;
			} else if(Axis.Z.equals(id))   {hasZAxis = true;
			} else if(Axis.POV.equals(id)) {hasPOV = true;
			}
		}
			
		return controller.getType() == Type.GAMEPAD && hasPOV && hasRXAxis && hasRYAxis && hasXAxis && hasYAxis && hasZAxis;
	}
	
	private static final boolean isWindows() {
		
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
