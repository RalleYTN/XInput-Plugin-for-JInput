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

import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import de.ralleytn.wrapper.microsoft.xinput.XInputVibration;
import net.java.games.input.Rumbler;

final class XIRumbler implements Rumbler {

	private static final int MAX_VALUE = 65535;
	
	private final int userIndex;
	private final Axis axis;
	private final XInputVibration vibration;
	
	protected XIRumbler(Axis axis, XInputVibration vibration, int userIndex) {
		
		this.axis = axis;
		this.vibration = vibration;
		this.userIndex = userIndex;
	}
	
	@Override
	public final Identifier getAxisIdentifier() {
		
		return this.axis;
	}

	@Override
	public final String getAxisName() {
		
		return this.axis.getName();
	}

	@Override
	public final void rumble(float intensity) {
		
		if(Axis.RX.equals(this.axis)) {
			
			this.vibration.wRightMotorSpeed = (int) (intensity * MAX_VALUE);
			
		} else if(Axis.X.equals(this.axis)) {
			
			this.vibration.wLeftMotorSpeed = (int) (intensity * MAX_VALUE);
		}
		
		XInputEnvironmentPlugin.XINPUT.XInputSetState(this.userIndex, this.vibration);
	}
}
