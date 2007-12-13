/*
    EZ Intranet Messenger
    Copyright (C) 2007  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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
package org.ezim.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimPlainDocument;
import org.ezim.ui.EzimMain;

public class EzimPlaza
	extends JFrame
	implements WindowListener
{
	private EzimMain emHwnd;

	private static SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");

	private JPanel jpnlBase;
	private JTextArea jtaPlaza;
	private JScrollPane jspPlaza;
	private JTextField jtfdSpeak;
	private JButton jbtnSpeak;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimPlaza(EzimMain emIn)
	{
		this.emHwnd = emIn;

		this.initGUI();

		this.setIconImage(EzimImage.icoPlaza.getImage());
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle(EzimLang.PlazaOfSpeech);
		this.setMinimumSize(new Dimension(320, 200));
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jtaPlaza = new JTextArea();
		this.jtaPlaza.setLineWrap(true);
		this.jtaPlaza.setWrapStyleWord(true);
		this.jtaPlaza.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.jtaPlaza.setEditable(false);

		this.jspPlaza = new JScrollPane(this.jtaPlaza);

		this.jtfdSpeak = new JTextField
		(
			new EzimPlainDocument(Ezim.maxAckLength)
			, ""
			, 0
		);
		this.jtfdSpeak.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jtfdSpeak_ActionPerformed(evtTmp);
					return;
				}
			}
		);

		this.jbtnSpeak = new JButton(EzimLang.Speak);
		this.jbtnSpeak.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnSpeak_ActionPerformed(evtTmp);
					return;
				}
			}
		);

		this.jpnlBase = new JPanel();
		this.add(this.jpnlBase);

		// L A Y O U T -----------------------------------------------------
		GroupLayout glBase = new GroupLayout(this.jpnlBase);
		this.jpnlBase.setLayout(glBase);

		glBase.setAutoCreateGaps(true);
		glBase.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hGrp = glBase.createSequentialGroup();

		hGrp.addGroup
		(
			glBase.createParallelGroup()
			.addGroup
			(
				glBase.createSequentialGroup()
				.addComponent(this.jspPlaza)
			)
			.addGroup
			(
				glBase.createSequentialGroup()
				.addComponent
				(
					this.jtfdSpeak
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Integer.MAX_VALUE
				)
				.addComponent(this.jbtnSpeak)
			)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jspPlaza)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jtfdSpeak)
			.addComponent(this.jbtnSpeak)
		);

		glBase.setVerticalGroup(vGrp);

		// W I N D O W   L I S T E N E R -----------------------------------
		this.addWindowListener(this);

		return;
	}

	// W I N D O W   L I S T E N E R ---------------------------------------
	public void windowActivated(WindowEvent e)
	{
		return;
	}

	public void windowClosed(WindowEvent e)
	{
		// comment out the setDefaultCloseOperation to make this usable
		return;
	}

	public void windowClosing(WindowEvent e)
	{
		this.emHwnd.localState = EzimContact.DEFAULT_STATE;

		EzimAckSender easTmp = new EzimAckSender
		(
			this.emHwnd
			, EzimAckSemantics.state(EzimContact.DEFAULT_STATE)
		);
		easTmp.start();

		return;
	}

	public void windowDeactivated(WindowEvent e)
	{
		return;
	}

	public void windowDeiconified(WindowEvent e)
	{
		return;
	}

	public void windowIconified(WindowEvent e)
	{
		return;
	}

	public void windowOpened(WindowEvent e)
	{
		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
	private void sendSpeech()
	{
		String strTmp = this.jtfdSpeak.getText().trim();
		EzimAckSender easTmp = null;

		if (strTmp.length() > 0)
		{
			this.jtfdSpeak.setText("");

			easTmp = new EzimAckSender
			(
				this.emHwnd
				, EzimAckSemantics.speech(strTmp)
			);
			easTmp.start();
		}

		return;
	}

	private void jtfdSpeak_ActionPerformed(ActionEvent evt)
	{
		sendSpeech();
		return;
	}

	private void jbtnSpeak_ActionPerformed(ActionEvent evt)
	{
		sendSpeech();
		return;
	}

	// ---------------------------------------------------------------------
	/**
	 * empty contents in speech window and speech input textbox
	 */
	public void reset()
	{
		this.jtaPlaza.setText("");
		this.jtfdSpeak.setText("");
		return;
	}

	/**
	 * add content to the window and scroll to the updated position
	 * @param strContent contents to be added
	 */
	public synchronized void addPlazaContents(String strContent)
	{
		if (this.isVisible())
		{
			// add contents to the contents text area
			this.jtaPlaza.append(strContent);
			this.jtaPlaza.append("\n");

			// update scrollbar position
			this.jtaPlaza.setCaretPosition
			(
				this.jtaPlaza.getText().length()
			);
		}

		return;
	}

	/**
	 * add speech to the window with the given IP and contents
	 * @param strIp IP address of the contact who makes the speech
	 * @param strSpeech contents of the speech
	 */
	public void addSpeech(String strIp, String strSpeech)
	{
		if (this.isVisible())
		{
			EzimContact ecTmp = this.emHwnd.getContact(strIp);

			if (ecTmp != null)
			{
				StringBuffer sbTmp = new StringBuffer();

				// add timestamp
				sbTmp.append
				(
					"[" + sdfHHmm.format(new Date()) + "] "
				);
				// add contact name
				sbTmp.append
				(
					"<" + ecTmp.getName() + "> "
				);
				// add speech contents
				sbTmp.append
				(
					strSpeech
				);

				this.addPlazaContents(sbTmp.toString());
			}
		}

		return;
	}

	/**
	 * add narration to the window with the given IP and contents
	 * @param strIp IP address of the contact to be narrated
	 * @param strNarration contents of the narration
	 */
	public void addNarration(String strIp, String strNarration)
	{
		if (this.isVisible())
		{
			EzimContact ecTmp = this.emHwnd.getContact(strIp);

			if (ecTmp != null)
			{
				StringBuffer sbTmp = new StringBuffer();

				// add contact name
				sbTmp.append
				(
					"* " + ecTmp.getName() + " "
				);
				// add narration contents
				sbTmp.append
				(
					strNarration
				);

				this.addPlazaContents(sbTmp.toString());
			}
		}

		return;
	}
}
