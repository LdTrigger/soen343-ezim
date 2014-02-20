/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2014  Chun-Kwong Wong
    chunkwong.wong@gmail.com
    http://ezim.sourceforge.net/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ezim.core;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EzimPlainDocument extends PlainDocument
{
	private int maxLength;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the plain document
	 * @param iIn maximum length of the document
	 */
	public EzimPlainDocument(int iIn)
	{
		this.maxLength = iIn;
	}

	// O V E R R I D D E N   F U N C T I O N -------------------------------
	/**
	 * insert contents into the document
	 * @param offset the starting offset >= 0
	 * @param str the string to insert, does nothing with null/empty strings
	 * @param a the attributes for the inserted content
	 */
	public void insertString(int offset, String str, AttributeSet a)
		throws BadLocationException
	{
		if (! ((this.getLength() + str.length()) > this.maxLength))
			super.insertString(offset, str, a);
	}
}
