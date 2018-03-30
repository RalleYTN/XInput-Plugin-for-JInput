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

import de.ralleytn.wrapper.microsoft.xinput.XInputGamepad;
import net.java.games.input.Component;
import static de.ralleytn.wrapper.microsoft.xinput.XInput.*;

final class XIPollData {

	private final XInputGamepad gamepad;
	
	private float x;
	private float y;
	private float a;
	private float b;
	private float lb;
	private float rb;
	private float lthumb;
	private float rthumb;
	private float lt;
	private float rt;
	private float lx;
	private float ly;
	private float rx;
	private float ry;
	private float pov;
	
	protected XIPollData(XInputGamepad gamepad) {
		
		this.gamepad = gamepad;
		this.fetchData();
	}
	
	private final void fetchData() {
		
		this.fetchButtonData();
		this.fetchTriggerData();
		this.fetchThumbStickData();
		this.fetchPOVData();
	}
	
	private final void fetchPOVData() {
		
		short buttons = this.gamepad.wButtons;
		
		       if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_UP) && this.isDown(buttons, XINPUT_GAMEPAD_DPAD_LEFT)) {this.pov = 0.125F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_UP) && this.isDown(buttons, XINPUT_GAMEPAD_DPAD_RIGHT)) {this.pov = 0.375F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_DOWN) && this.isDown(buttons, XINPUT_GAMEPAD_DPAD_LEFT)) {this.pov = 0.875F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_DOWN) && this.isDown(buttons, XINPUT_GAMEPAD_DPAD_RIGHT)) {this.pov = 0.625F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_UP)) {this.pov = 0.25F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_LEFT)) {this.pov = 1.0F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_RIGHT)) {this.pov = 0.5F;
		} else if(this.isDown(buttons, XINPUT_GAMEPAD_DPAD_DOWN)) {this.pov = 0.75F;
		}
	}
	
	private final void fetchTriggerData() {
		
		this.lt = this.calcTriggerData(this.gamepad.bLeftTrigger);
		this.rt = this.calcTriggerData(this.gamepad.bRightTrigger);
	}
	
	private final float calcTriggerData(byte value) {
		
		float factor = (1.0F / 255) * Byte.toUnsignedInt(value);
		return Math.min(1.0F, factor);
	}
	
	private final void fetchThumbStickData() {
		
		this.lx = this.calcThumbStickData(this.gamepad.sThumbLX);
		this.ly = this.calcThumbStickData(this.gamepad.sThumbLY);
		this.rx = this.calcThumbStickData(this.gamepad.sThumbRX);
		this.ry = this.calcThumbStickData(this.gamepad.sThumbRY);
	}
	
	private final float calcThumbStickData(short value) {
		
		float factor = (1.0F / Short.MAX_VALUE) * Math.abs(value);
		factor = Math.min(1.0F, factor);
		return value < 0 ? -factor : factor;
	}
	
	private final void fetchButtonData() {
		
		short buttons = this.gamepad.wButtons;
		
		this.x = this.calcButtonData(buttons, XINPUT_GAMEPAD_X);
		this.y = this.calcButtonData(buttons, XINPUT_GAMEPAD_Y);
		this.a = this.calcButtonData(buttons, XINPUT_GAMEPAD_A);
		this.b = this.calcButtonData(buttons, XINPUT_GAMEPAD_B);
		this.lb = this.calcButtonData(buttons, XINPUT_GAMEPAD_LEFT_SHOULDER);
		this.rb = this.calcButtonData(buttons, XINPUT_GAMEPAD_RIGHT_SHOULDER);
		this.lthumb = this.calcButtonData(buttons, XINPUT_GAMEPAD_LEFT_THUMB);
		this.rthumb = this.calcButtonData(buttons, XINPUT_GAMEPAD_RIGHT_THUMB);
	}
	
	private final float calcButtonData(short buttons, int button) {
		
		return ((buttons & button) != 0) ? 1.0F : 0.0F;
	}
	
	private final boolean isDown(short buttons, int button) {
		
		return this.calcButtonData(buttons, button) == 1.0F;
	}
	
	protected final float getDataByComponent(Component component) {
		
		String name = component.getName();
		
		       if(name.equals("x")) {return this.x;
		} else if(name.equals("y")) {return this.y;
		} else if(name.equals("a")) {return this.a;
		} else if(name.equals("b")) {return this.b;
		} else if(name.equals("lb")) {return this.lb;
		} else if(name.equals("rb")) {return this.rb;
		} else if(name.equals("lthumb")) {return this.lthumb;
		} else if(name.equals("rthumb")) {return this.rthumb;
		} else if(name.equals("lt")) {return this.lt;
		} else if(name.equals("rt")) {return this.rt;
		} else if(name.equals("lx")) {return this.lx;
		} else if(name.equals("ly")) {return this.ly;
		} else if(name.equals("rx")) {return this.rx;
		} else if(name.equals("ry")) {return this.ry;
		} else if(name.equals("pov")) {return this.pov;
		}
		       
		return 0.0F;
	}
}
