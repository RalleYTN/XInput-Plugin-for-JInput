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
import de.ralleytn.wrapper.microsoft.xinput.XInputCapabilities;
import de.ralleytn.wrapper.microsoft.xinput.XInputState;
import de.ralleytn.wrapper.microsoft.xinput.XInputVibration;
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
					
					XInputCapabilities capabilities = new XInputCapabilities();
					int code = XINPUT.XInputGetCapabilities(dwUserIndex, XInput.XINPUT_FLAG_GAMEPAD, capabilities);
					
					if(code == XInput.ERROR_SUCCESS) {
						
						boolean canVibrate = (capabilities.Flags & XInput.XINPUT_CAPS_FFB_SUPPORTED) != 0;
						boolean noNavigation = (capabilities.Flags & XInput.XINPUT_CAPS_NO_NAVIGATION) != 0;
						XIComponent[] components = this.createComponents(noNavigation);
						XIRumbler[] rumblers = this.createRumblers(dwUserIndex, canVibrate);
						controllers[index] = new XIController(dwUserIndex, controller.getName(), components, rumblers);
						replaced = true;
					}
				}
			}
			
			if(!replaced) {
				
				controllers[index] = controller;
			}
		}
		
		return controllers;
	}
	
	private final XIComponent[] createComponents(boolean noNavigation) {
		
		ArrayList<XIComponent> components = new ArrayList<>();
		
		components.add(new XIButton("a", Button._0));
		components.add(new XIButton("b", Button._1));
		components.add(new XIButton("x", Button._2));
		components.add(new XIButton("y", Button._3));
		components.add(new XIButton("lb", Button._4));
		components.add(new XIButton("rb", Button._5));
		
		if(noNavigation) {
			
			components.add(new XIButton("lthumb", Button._6));
			components.add(new XIButton("rthumb", Button._7));
			
		} else {
			
			components.add(new XIButton("back", Button._6));
			components.add(new XIButton("start", Button._7));
			components.add(new XIButton("lthumb", Button._8));
			components.add(new XIButton("rthumb", Button._9));
			components.add(new XIPOV());
		}
		
		components.add(new XITrigger(Axis.Z));
		components.add(new XITrigger(Axis.RZ));
		components.add(new XILeftThumbStick(Axis.X));
		components.add(new XILeftThumbStick(Axis.Y));
		components.add(new XIRightThumbStick(Axis.RX));
		components.add(new XIRightThumbStick(Axis.RY));
		
		return components.toArray(new XIComponent[components.size()]);
	}
	
	private final XIRumbler[] createRumblers(int userIndex, boolean canVibrate) {
		
		// It is important that both rumblers share the same vibration object.
		// If they do not, only one can rumble at a time.
		
		if(canVibrate) {
			
			XInputVibration vibration = new XInputVibration();
			
			return new XIRumbler[] {
					
				new XIRumbler(Axis.X, vibration, userIndex),
				new XIRumbler(Axis.RX, vibration, userIndex)
			};
		}

		return new XIRumbler[0];
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
