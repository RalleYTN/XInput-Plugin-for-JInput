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

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class TestFrame extends JFrame{

	private static final long serialVersionUID = -3213276390930472608L;
	
	private final TestCheckList buttons;
	private final TestCheckList lt;
	private final TestCheckList rt;
	private final TestCheckList lthumb;
	private final TestCheckList rthumb;
	private final TestCheckList pov;
	
	protected TestFrame() {
		
		super("Do the following things");
		
		this.buttons = new TestCheckList("Buttons", new String[] {"A", "B", "X", "Y", "LB", "RB", "START", "BACK", "LTHUMB", "RTHUMB"});
		this.lt = new TestCheckList("Left Trigger", new String[] {"25%", "50%", "75%", "100%"});
		this.rt = new TestCheckList("Right Trigger", new String[] {"25%", "50%", "75%", "100%"});
		this.lthumb = new TestCheckList("Left Thumb Stick", new String[] {"UP", "DOWN", "LEFT", "RIGHT"});
		this.rthumb = new TestCheckList("Right Thumb Stick", new String[] {"UP", "DOWN", "LEFT", "RIGHT"});
		this.pov = new TestCheckList("POV", new String[] {"NORTH", "NORTH_EAST", "EAST", "SOUTH_EAST", "SOUTH", "SOUTH_WEST", "WEST", "NORTH_WEST"});
		
		JPanel contentPane = new JPanel();
		contentPane.add(this.buttons.getPanel());
		contentPane.add(this.lt.getPanel());
		contentPane.add(this.rt.getPanel());
		contentPane.add(this.lthumb.getPanel());
		contentPane.add(this.rthumb.getPanel());
		contentPane.add(this.pov.getPanel());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(contentPane);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	protected final void checkPOV(String direction) {
		
		this.pov.check(direction);
	}
	
	protected final void checkLThumb(String direction) {
		
		this.lthumb.check(direction);
	}
	
	protected final void checkRThumb(String direction) {
		
		this.rthumb.check(direction);
	}
	
	protected final void checkLT(int percent) {
		
		this.lt.check(percent + "%");
	}
	
	protected final void checkRT(int percent) {
		
		this.rt.check(percent + "%");
	}
	
	protected final void checkButton(String name) {
		
		this.buttons.check(name.toUpperCase());
	}
	
	protected final boolean hasUncheckedItems() {
		
		return this.buttons.hasUncheckedItems() ||
			   this.lt.hasUncheckedItems() ||
			   this.rt.hasUncheckedItems() ||
			   this.lthumb.hasUncheckedItems() ||
			   this.rthumb.hasUncheckedItems() ||
			   this.pov.hasUncheckedItems();
	}
}
