package de.ralleytn.plugins.jinput.xinput;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import de.ralleytn.wrapper.microsoft.xinput.XInputVibration;
import net.java.games.input.Rumbler;

public class XInputRumbler implements Rumbler {

	private static final int MAX_VALUE = 65535;
	
	private XInputVibration vibration;
	private Axis axis;
	
	XInputRumbler(XInputVibration vibration, Axis axis) {
		
		this.vibration = vibration;
		this.axis = axis;
	}
	
	@Override
	public Identifier getAxisIdentifier() {
		
		return this.axis;
	}

	@Override
	public String getAxisName() {
		
		return this.axis.getName();
	}

	@Override
	public void rumble(float intensity) {
		
		if(Axis.RX.equals(this.axis)) {
			
			this.vibration.wRightMotorSpeed = (short)((int)(intensity * MAX_VALUE) + Short.MIN_VALUE);
			
		} else if(Axis.X.equals(this.axis)) {
			
			this.vibration.wLeftMotorSpeed = (short)((int)(intensity * MAX_VALUE) + Short.MIN_VALUE);
		}
	}
}
