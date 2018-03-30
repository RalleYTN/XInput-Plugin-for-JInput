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

import net.java.games.input.Component;
import net.java.games.input.Event;

final class XIEventFactory {

	private final XIController controller;
	private final XIPollData oldPollData;
	private final XIPollData newPollData;
	
	protected XIEventFactory(XIController controller, XIPollData oldPollData, XIPollData newPollData) {
		
		this.controller = controller;
		this.oldPollData = oldPollData;
		this.newPollData = newPollData;
	}
	
	protected final List<Event> createEvents() {
		
		List<Event> events = new ArrayList<>();
		
		for(Component component : this.controller.getComponents()) {
			
			float oldValue = this.oldPollData.getDataByComponent(component);
			float newValue = this.newPollData.getDataByComponent(component);
			
			boolean buttonReleased = (component instanceof XIButton && (oldValue == 1.0F && newValue == 0.0F));
			boolean povReleased = (component instanceof XIPOV && (oldValue == 1.0F && newValue == 0.0F));
			boolean eventQueueHasSpace = events.size() < this.controller.getEventQueueSize();
			boolean somethingHappened = (oldValue != newValue);
			boolean shouldCreateEvent = (eventQueueHasSpace && (somethingHappened || povReleased || buttonReleased));
			
			if(shouldCreateEvent) {
				
				events.add(createEvent(component, newValue));
			}
		}
		
		return events;
	}
	
	private static final Event createEvent(Component component, float value) {
		
		Event event = new Event();
		event.set(component, value, 0);
		return event;
	}
}
