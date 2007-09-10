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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimMsgSender;
import org.ezim.core.EzimLang;

public class EzimMsgOut extends JFrame
{
	private EzimContact ec;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JTextArea jtaMsg;
	private JScrollPane jspMsg;
	private JButton jbtnSend;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimMsgOut(EzimContact ecIn)
	{
		init(ecIn, (String) null);
	}

	public EzimMsgOut(EzimContact ecIn, String strIn)
	{
		init(ecIn, strIn);
	}

	private void init(EzimContact ecIn, String strIn)
	{
		this.ec = ecIn;
		this.initGUI();

		if (strIn != null && strIn.length() > 0)
		{
			this.jtaMsg.setText(strIn);
			this.jtaMsg.setCaretPosition(0);
		}

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingMessage);
		this.setMinimumSize(new Dimension(320, 200));
		this.setVisible(true);

		return;
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.To);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEnabled(false);

		this.jtaMsg = new JTextArea();
		this.jtaMsg.setLineWrap(true);
		this.jtaMsg.setWrapStyleWord(true);
		this.jtaMsg.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		this.jspMsg = new JScrollPane(this.jtaMsg);

		this.jbtnSend = new JButton(EzimLang.Send);
		this.jbtnSend.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnSend_ActionPerformed(evtTmp);
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
			glBase.createParallelGroup(Alignment.TRAILING)
			.addGroup
			(
				glBase.createSequentialGroup()
				.addComponent(this.jlblName)
				.addComponent
				(
					this.jtfdName
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Integer.MAX_VALUE
				)
			)
			.addComponent
			(
				this.jspMsg
				, GroupLayout.DEFAULT_SIZE
				, GroupLayout.DEFAULT_SIZE
				, Integer.MAX_VALUE
			)
			.addComponent(this.jbtnSend)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jlblName)
			.addComponent(this.jtfdName)
		);

		vGrp.addComponent
		(
			this.jspMsg
			, GroupLayout.DEFAULT_SIZE
			, GroupLayout.DEFAULT_SIZE
			, Integer.MAX_VALUE
		);

		vGrp.addComponent(this.jbtnSend);

		glBase.setVerticalGroup(vGrp);
	}

	// E V E N T   H A N D L E R -------------------------------------------
	private void jbtnSend_ActionPerformed(ActionEvent evt)
	{
		EzimMsgSender jmsTmp = new EzimMsgSender
		(
			this.ec.getIp()
			, this.jtaMsg.getText()
		);
		jmsTmp.run();

		this.dispose();

		return;
	}
}
