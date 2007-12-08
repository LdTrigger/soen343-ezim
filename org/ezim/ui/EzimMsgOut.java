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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.ezim.core.EzimConf;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;
import org.ezim.core.EzimMsgSender;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;

public class EzimMsgOut extends JFrame implements WindowListener
{
	private EzimContact ec;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JTextArea jtaMsg;
	private JScrollPane jspMsg;
	private JButton jbtnSend;
	private EzimConf ezimCnf;
	private ArrayList<EzimContact> allContacts;
	/** needed to catch the tab when switching focus from text to send */
	private String jtaMsgBeforeTab = null; 
	
	// C O N S T R U C T O R -----------------------------------------------
	public EzimMsgOut(EzimContact ecIn)
	{
		this.allContacts = null;
		init(ecIn, (String) null);
	}

	public EzimMsgOut(EzimContact ecIn, String strIn)
	{
		this.allContacts = null;
		init(ecIn, strIn);
	}

	public EzimMsgOut(ArrayList<EzimContact> allContacts) {
		this.allContacts = allContacts;
		EzimContact ecIn;
		try {
			ecIn = new EzimContact("127.0.0.1", "All Users", "online");
			init(ecIn, (String) null);
		} catch (EzimContactException e) {
			JOptionPane.showMessageDialog(this, "ERROR: Can't send message to all");
		}
	}

	private void init(EzimContact ecIn, String strIn)
	{
		this.ec = ecIn;
		this.ezimCnf = EzimConf.getInstance();
		
		this.initGUI();

		if (strIn != null && strIn.length() > 0)
		{
			this.jtaMsg.setText(strIn);
			this.jtaMsg.setCaretPosition(0);
		}

		this.setIconImage(EzimImage.icoMsg.getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingMessage);
		this.setMinimumSize(new Dimension(320, 200));
		this.setPositionFromConf();
		this.setVisible(true);

		return;
	}

	private void initGUI()
	{
		this.addWindowListener(this);
		
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.To);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEnabled(false);

		this.jtaMsg = new JTextArea();
		this.jtaMsg.setLineWrap(true);
		this.jtaMsg.setWrapStyleWord(true);
		this.jtaMsg.addKeyListener(
			new KeyListener(){
				public void keyPressed(KeyEvent e) {
				}
				public void keyReleased(KeyEvent e) {
					if(e.getKeyChar() != '\t'){
						jtaMsgBeforeTab = jtaMsg.getText();
					}					
				}

				public void keyTyped(KeyEvent e) {
					if(e.getKeyChar() == '\t'){
						jtaMsg.setText(jtaMsgBeforeTab);
						jbtnSend.requestFocus();
					}
				}
				
			}
		);
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
		this.jbtnSend.addKeyListener(
			new KeyListener(){
				public void keyPressed(KeyEvent e) {
					if(e.getKeyChar() == '\n'){
						jbtnSend_ActionPerformed(null);
						return;
					}
					if(e.getKeyChar() == '\r'){
						jbtnSend_ActionPerformed(null);
						return;
					}
				}
				public void keyReleased(KeyEvent e) {}

				public void keyTyped(KeyEvent e) {
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
			, GroupLayout.PREFERRED_SIZE
			, Integer.MAX_VALUE
		);

		vGrp.addComponent(this.jbtnSend);

		glBase.setVerticalGroup(vGrp);
	}


	private void setPositionFromConf(){
		this.setLocation
		(
			Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgOutLocationX
				)
			)
			, Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgOutLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgOutSizeW
				)
			)
			, Integer.parseInt
			(
					ezimCnf.settings.getProperty
				(
					EzimConf.ezimMsgOutSizeH
				)
			)
		);	
	}
	
	private void storePostionToConf(){
		Point ptTmp = this.getLocation();
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgOutLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgOutLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgOutSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ezimCnf.settings.setProperty
		(
			EzimConf.ezimMsgOutSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);
		ezimCnf.write();
	}	

	
	// E V E N T   H A N D L E R -------------------------------------------
	private void jbtnSend_ActionPerformed(ActionEvent evt)
	{
		if (this.jtaMsg.getText().length() > 0)
		{
		if(allContacts == null){
			EzimMsgSender jmsTmp = new EzimMsgSender
			(
				this.ec.getIp(),
				jtaMsgBeforeTab
		//		, this.jtaMsg.getText()
			);
			jmsTmp.run();
		}else{
			for (int i = 0; i < allContacts.size(); i++) {
				EzimContact contact = allContacts.get(i);
				EzimMsgSender jmsTmp = new EzimMsgSender
				(
					contact.getIp(),
					jtaMsgBeforeTab
		//			this.jtaMsg.getText()
				);
				jmsTmp.run();
			}
		}
		this.dispose();
		}

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
