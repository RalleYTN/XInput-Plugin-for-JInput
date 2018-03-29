package de.ralleytn.plugins.jinput.xinput;

import java.util.ArrayList;
import java.util.List;

import de.ralleytn.wrapper.microsoft.xinput.XInput;
import de.ralleytn.wrapper.microsoft.xinput.XInputCapabilities;
import de.ralleytn.wrapper.microsoft.xinput.XInputFactory;
import de.ralleytn.wrapper.microsoft.xinput.XInputLibraryNotFound;
import de.ralleytn.wrapper.microsoft.xinput.XInputState;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugin;

public class XInputEnvironmentPlugin extends ControllerEnvironment implements Plugin {

	private Controller[] controllers;
	private boolean supported;
	private XInput xinput;
	private List<Integer> alreadyUsedUserIndices;
	
	public XInputEnvironmentPlugin() {
		
		if(isWindows()) {
			
			this.xinput = createXInput();
			this.supported = this.xinput != null;
			
			if(this.supported) {
				
				this.alreadyUsedUserIndices = new ArrayList<>();
				this.controllers = this.createControllers();
			}
		}
	}
	
	@Override
	public Controller[] getControllers() {
		
		return this.controllers;
	}

	@Override
	public boolean isSupported() {
		
		return this.supported;
	}
	
	private int getNextUserIndex() {
		
		for(int dwUserIndex = 0; dwUserIndex < XInput.XUSER_MAX_COUNT; dwUserIndex++) {
			
			if(!this.alreadyUsedUserIndices.contains(dwUserIndex) && this.xinput.XInputGetState(dwUserIndex, new XInputState()) == XInput.ERROR_SUCCESS) {
				
				this.alreadyUsedUserIndices.add(dwUserIndex);
				return dwUserIndex;
			}
		}
		
		return -1;
	}
	
	private Controller[] createControllers() {
		
		Controller[] defaultControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		Controller[] controllers = new Controller[defaultControllers.length];
		
		for(int index = 0; index < defaultControllers.length; index++) {
			
			if(defaultControllers[index].getType() == Type.GAMEPAD && isXInput(defaultControllers[index])) {
				
				int dwUserIndex = this.getNextUserIndex();
				
				if(dwUserIndex != -1) {
					
					controllers[index] = this.createXInputController(defaultControllers[index], dwUserIndex);
					
				} else {
					
					controllers[index] = defaultControllers[index];
				}
				
			} else {
				
				controllers[index] = defaultControllers[index];
			}
		}
		
		return controllers;
	}
	
	private XInputController createXInputController(Controller parent, int dwUserIndex) {
		
		XInputCapabilities capabilities = new XInputCapabilities();
		this.xinput.XInputGetCapabilities(dwUserIndex, XInput.XINPUT_FLAG_GAMEPAD, capabilities);
		
		return new XInputController(dwUserIndex, parent.getName(), null, null, null);
	}
	
	private static final boolean isXInput(Controller controller) {

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
			
		return hasPOV && hasRXAxis && hasRYAxis && hasXAxis && hasYAxis && hasZAxis;
	}
	
	private static final XInput createXInput() {
		
		try {
			
			return XInputFactory.getInstance();
			
		} catch(XInputLibraryNotFound exception) {
			
			return null;
		}
	}
	
	private static final boolean isWindows() {
		
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
