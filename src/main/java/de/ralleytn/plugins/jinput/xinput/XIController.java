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

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import de.ralleytn.wrapper.microsoft.xinput.XInput;
import de.ralleytn.wrapper.microsoft.xinput.XInputState;
import net.java.games.input.AbstractController;
import net.java.games.input.Event;

final class XIController extends AbstractController {

	private int dwUserIndex;
	private XInputState oldState;
	private Queue<Event> events;
	private int eventQueueSize;
	
	protected XIController(int dwUserIndex, String name, XIComponent[] components, XIRumbler[] rumblers) {
		
		super(name, components, null, rumblers);
		
		for(XIRumbler rumbler : rumblers) {
			
			rumbler.setController(this);
		}
		
		this.eventQueueSize = 32;
		this.events = new ArrayBlockingQueue<>(this.eventQueueSize);
		this.dwUserIndex = dwUserIndex;
		this.oldState = new XInputState();
	}
	
	@Override
	public final Type getType() {
		
		return Type.GAMEPAD;
	}
	
	protected final int getEventQueueSize() {
		
		return this.eventQueueSize;
	}
	
	protected final int getUserIndex() {
		
		return this.dwUserIndex;
	}
	
	@Override
	protected final synchronized void setDeviceEventQueueSize(int size) throws IOException {
		
		this.eventQueueSize = size;
		Queue<Event> events = new ArrayBlockingQueue<>(size);
		
		while(!this.events.isEmpty()) {
			
			events.offer(this.events.poll());
		}
		
		this.events = events;
	}
	
	@Override
	protected final synchronized void pollDevice() throws IOException {
		
		XInputState newState = new XInputState();
		int code = XInputEnvironmentPlugin.XINPUT.XInputGetState(this.dwUserIndex, newState);
		
		if(code == XInput.ERROR_SUCCESS) {
			
			if(newState.dwPacketNumber != this.oldState.dwPacketNumber) {
				
				XIPollData oldPollData = new XIPollData(this.oldState.Gamepad);
				XIPollData newPollData = new XIPollData(newState.Gamepad);
				XIEventFactory difference = new XIEventFactory(this, oldPollData, newPollData);
				List<Event> events = difference.createEvents();
				
				for(Event event : events) {
					
					if(this.events.size() < this.eventQueueSize) {
						
						this.events.offer(event);
					}
				}
				
				this.oldState = newState;
			}
		
		} else {
			
			throw new IOException("XUser" + this.dwUserIndex);
		}
	}

	@Override
	protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
		
		if(!this.events.isEmpty()) {
			
			event.set(this.events.poll());
			return true;
		}
		
		return false;
	}
}
