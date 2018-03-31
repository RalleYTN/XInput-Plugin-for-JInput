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

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

final class CheckList {

	private final JPanel panel;
	private final List<JCheckBox> checkBoxes;
	
	protected CheckList(String name, String[] items) {
		
		this.checkBoxes = new ArrayList<>();
		
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.setBorder(BorderFactory.createTitledBorder(name));
		
		for(String item : items) {
			
			JCheckBox checkBox = new JCheckBox(item.toString());
			checkBox.setEnabled(false);
			this.checkBoxes.add(checkBox);
			this.panel.add(checkBox);
		}
	}
	
	protected final void check(String item) {
		
		for(JCheckBox checkBox : this.checkBoxes) {
			
			if(checkBox.getText().equals(item.toString())) {
				
				checkBox.setSelected(true);
				break;
			}
		}
	}
	
	protected final boolean hasUncheckedItems() {
		
		for(JCheckBox checkbox : this.checkBoxes) {
			
			if(!checkbox.isSelected()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	protected final JPanel getPanel() {
		
		return this.panel;
	}
}
