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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
import org.ezim.core.EzimConf;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimSysTray;
import org.ezim.ui.EzimMsgOut;

public class EzimMsgIn extends JFrame implements WindowListener
{
	private EzimContact ec;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JTextArea jtaMsg;
	private JScrollPane jspMsg;
	private JLabel jlblOpen;
	private JButton jbtnReply;
	private EzimConf ezimCnf;
	
	// C O N S T R U C T O R -----------------------------------------------
	public EzimMsgIn(EzimContact ecIn, String strIn)
	{
		this.ec = ecIn;
		ezimCnf = EzimConf.getInstance();
		
		this.initGUI();

		if (strIn != null && strIn.length() > 0)
			this.jtaMsg.setText(strIn);

		this.setIconImage(EzimImage.icoMsg.getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.IncomingMessage);
		this.setMinimumSize(new Dimension(320, 200));
		this.setPositionFromConf();
	
		this.setVisible(true);
		this.toFront();
	}

	private void setPositionFromConf(){
		this.setLocation
		(
			Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgInLocationX
				)
			)
			, Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgInLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgInSizeW
				)
			)
			, Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgInSizeH
				)
			)
		);	
	}
	
	private void storePostionToConf(){
		Point ptTmp = this.getLocation();
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgInLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgInLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgInSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgInSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);
		ezimCnf.write();
	}
	
	private void initGUI()
	{
		this.addWindowListener(this);
		
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.From);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEnabled(false);

		this.jtaMsg = new JTextArea();
		this.jtaMsg.setLineWrap(true);
		this.jtaMsg.setWrapStyleWord(true);
		this.jtaMsg.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.jtaMsg.setEditable(false);

		this.jspMsg = new JScrollPane();

		this.jlblOpen = new JLabel(EzimLang.ClickHereToOpenMessage);
		this.jlblOpen.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					jlblOpen_MouseClicked(evtTmp);
					return;
				}

				public void mouseEntered(MouseEvent evtTmp)
				{
					return;
				}

				public void mouseExited(MouseEvent evtTmp)
				{
					return;
				}

				public void mousePressed(MouseEvent evtTmp)
				{
					return;
				}

				public void mouseReleased(MouseEvent evtTmp)
				{
					return;
				}
			}
		);

		this.jbtnReply = new JButton(EzimLang.Reply);
		this.jbtnReply.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnReply_ActionPerformed(evtTmp);
					return;
				}
			}
		);
		this.jbtnReply.setEnabled(false);

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
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Integer.MAX_VALUE
				)
			)
			.addComponent
			(
				this.jspMsg
				, GroupLayout.DEFAULT_SIZE
				, GroupLayout.PREFERRED_SIZE
				, Integer.MAX_VALUE
			)
			.addGroup
			(
				glBase.createSequentialGroup()
				.addComponent
				(
					this.jlblOpen
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Integer.MAX_VALUE
				)
				.addComponent(this.jbtnReply)
			)
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
			, GroupLayout.PREFERRED_SIZE
			, Integer.MAX_VALUE
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jlblOpen)
			.addComponent(this.jbtnReply)
		);

		glBase.setVerticalGroup(vGrp);
		
		
		if(EzimSysTray.getInstance().openMsgsImmediatly){
			jlblOpen_MouseClicked(null);
		}
	}

	// E V E N T   H A N D L E R -------------------------------------------
	private void jbtnReply_ActionPerformed(ActionEvent evt)
	{
		StringBuffer sbMsg = new StringBuffer();
		sbMsg.append("----- ");
		sbMsg.append(EzimLang.OriginalMessageFrom);
		sbMsg.append(" ");
		sbMsg.append(ec.getName());
		sbMsg.append(" -----\n");
		sbMsg.append(this.jtaMsg.getText());
		new EzimMsgOut(this.ec, sbMsg.toString());
		this.dispose();

		return;
	}

	private void jlblOpen_MouseClicked(MouseEvent evt)
	{
		this.jspMsg.setViewportView(this.jtaMsg);
		this.jbtnReply.setEnabled(true);
		this.jlblOpen.setVisible(false);

		return;
	}

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
		storePostionToConf();

	}

	public void windowClosing(WindowEvent arg0) {
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
}
