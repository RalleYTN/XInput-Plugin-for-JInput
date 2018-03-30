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

import net.java.games.input.Component.Identifier.Button;

final class XIButton extends XIComponent {

	protected XIButton(Button id) {
		
		super(XIButton.getMappedName(id), id);
	}
	
	private static final String getMappedName(Button id) {
		
		       if(id == Button._0) {return "a";
		} else if(id == Button._1) {return "b";
		} else if(id == Button._2) {return "x";
		} else if(id == Button._3) {return "y";
		} else if(id == Button._4) {return "lb";
		} else if(id == Button._5) {return "rb";
		} else if(id == Button._6) {return "back";
		} else if(id == Button._7) {return "start";
		} else if(id == Button._8) {return "lthumb";
		} else if(id == Button._9) {return "rthumb";
		}
		
		return null;
	}
}
