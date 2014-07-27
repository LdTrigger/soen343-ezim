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
package org.ezim.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactTransferHandler;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimMsgSender;
import org.ezim.core.EzimPlainDocument;
import org.ezim.core.EzimPlainDocument;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimTextArea;
import org.ezim.ui.EzimTextField;

public class EzimMsgOut
	extends JFrame
	implements WindowListener
{
	private List<EzimContact> contacts;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JButton jbtnDelLastRcpt;
	private JLabel jlblSbj;
	private EzimTextField etfSbj;
	private EzimTextArea etaMsg;
	private JScrollPane jspMsg;
	private JButton jbtnSend;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the outgoing message window
	 */
	public EzimMsgOut()
	{
		this.init
		(
			(List<EzimContact>) null
			, (String) null
			, (String) null
		);
	}

	/**
	 * construct an instance of the outgoing message window
	 * @param lIn contacts of the message recipients
	 */
	public EzimMsgOut(List<EzimContact> lIn)
	{
		this.init(lIn, (String) null, (String) null);
	}

	/**
	 * construct an instance of the outgoing message window
	 * @param lIn contacts of the message recipients
	 * @param strSbj preset subject line
	 * @param strIn preset message body
	 */
	public EzimMsgOut
	(
		List<EzimContact> lIn
		, String strSbj
		, String strIn
	)
	{
		this.init(lIn, strSbj, strIn);
	}

	/**
	 * initialize class members and GUI
	 * @param lIn contacts of the message recipients
	 * @param strSbj preset subject line
	 * @param strIn preset message body
	 */
	private void init
	(
		List<EzimContact> lIn
		, String strSbj
		, String strIn
	)
	{
		this.initGUI();

		this.setIconImage(EzimImage.icoButtons[0].getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingMessage);

		this.pack();
		this.setMinimumSize(this.getSize());
		this.loadConf();

		this.setVisible(true);

		if (lIn != null && lIn.size() > 0)
		{
			this.contacts = new ArrayList<EzimContact>(lIn);
			this.updateContactNames();
		}
		else
		{
			this.contacts = new ArrayList<EzimContact>();
		}

		if (strSbj != null && strSbj.length() > 0)
		{
			this.etfSbj.setText(strSbj);
		}

		if (strIn != null && strIn.length() > 0)
		{
			this.etaMsg.setText(strIn);
			this.etaMsg.setCaretPosition(0);
		}

		if (strSbj != null && strSbj.length() > 0)
		{
			this.etaMsg.requestFocusInWindow();
		}
		else
		{
			this.etfSbj.requestFocusInWindow();
		}
	}

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * load window position and size from configuration settings
	 */
	private void loadConf()
	{
		this.setLocation
		(
			EzimConf.UI_MSGOUT_LOCATION_X
			, EzimConf.UI_MSGOUT_LOCATION_Y
		);
		this.setSize
		(
			EzimConf.UI_MSGOUT_SIZE_W
			, EzimConf.UI_MSGOUT_SIZE_H
		);
	}

	/**
	 * save window position and size to configuration settings
	 */
	private void saveConf()
	{
		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		EzimConf.UI_MSGOUT_LOCATION_X = (int) ptTmp.getX();
		EzimConf.UI_MSGOUT_LOCATION_Y = (int) ptTmp.getY();
		Dimension dmTmp = this.getSize();
		EzimConf.UI_MSGOUT_SIZE_W = (int) dmTmp.getWidth();
		EzimConf.UI_MSGOUT_SIZE_H = (int) dmTmp.getHeight();
	}

	/**
	 * initialize GUI components
	 */
	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.To);

		this.jtfdName = new JTextField();
		this.jtfdName.setEnabled(false);
		this.jtfdName.setTransferHandler
		(
			EzimContactTransferHandler.getInstance()
		);

		this.jbtnDelLastRcpt = new JButton
		(
			new ImageIcon
			(
				EzimImage.icoButtons[5].getImage().getScaledInstance
				(
					16, 16
					, Image.SCALE_SMOOTH
				)
			)
		);
		this.jbtnDelLastRcpt.setPreferredSize(new Dimension(16, 16));
		this.jbtnDelLastRcpt.setToolTipText(EzimLang.DeleteLastRecipient);
		this.jbtnDelLastRcpt.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMsgOut.this.jbtnDelLastRcpt_ActionPerformed();
				}
			}
		);

		this.jlblSbj = new JLabel(EzimLang.Subject);

		this.etfSbj = new EzimTextField
		(
			new EzimPlainDocument(Ezim.dtxBufLen)
			, ""
			, 0
		);

		this.etaMsg = new EzimTextArea
		(
			new EzimPlainDocument(Ezim.maxMsgLength)
		);
		this.etaMsg.setLineWrap(true);
		this.etaMsg.setWrapStyleWord(true);
		this.etaMsg.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		this.jspMsg = new JScrollPane(this.etaMsg);
		this.jspMsg.setMinimumSize(new Dimension(240, 100));

		this.jbtnSend = new JButton(EzimLang.Send);
		this.jbtnSend.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMsgOut.this.jbtnSend_ActionPerformed();
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
						.addGroup
						(
							glBase.createParallelGroup(Alignment.LEADING)
								.addComponent
								(
									this.jlblName
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
								.addComponent
								(
									this.jlblSbj
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
						)
						.addGroup
						(
							glBase.createParallelGroup(Alignment.LEADING)
								.addComponent
								(
									this.jtfdName
									, GroupLayout.DEFAULT_SIZE
									, GroupLayout.PREFERRED_SIZE
									, Short.MAX_VALUE
								)
								.addComponent
								(
									this.etfSbj
									, GroupLayout.DEFAULT_SIZE
									, GroupLayout.PREFERRED_SIZE
									, Short.MAX_VALUE
								)
						)
						.addGroup
						(
							glBase.createParallelGroup(Alignment.LEADING)
								.addComponent
								(
									this.jbtnDelLastRcpt
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
						)
				)
				.addComponent
				(
					this.jspMsg
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
				.addComponent
				(
					this.jbtnSend
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblName
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jtfdName
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnDelLastRcpt
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblSbj
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.etfSbj
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vGrp.addComponent
		(
			this.jspMsg
/*
			, GroupLayout.DEFAULT_SIZE
			, GroupLayout.PREFERRED_SIZE
			, Short.MAX_VALUE
*/
			, GroupLayout.DEFAULT_SIZE
			, 100
			, Short.MAX_VALUE
		);

		vGrp.addComponent
		(
			this.jbtnSend
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		glBase.setVerticalGroup(vGrp);

		this.addWindowListener(this);
	}

	/**
	 * update contact names in the recipient textfield
	 */
	private void updateContactNames()
	{
		StringBuilder sbOut = new StringBuilder();

		for(EzimContact ec: this.contacts)
		{
			sbOut.append(ec.getName());
			sbOut.append(" ,");
		}

		if (sbOut.length() > 1)
		{
			this.jtfdName.setText(sbOut.substring(0, sbOut.length() - 2));
		}
		else
		{
			this.jtfdName.setText(sbOut.toString());
		}
	}

	// W I N D O W   L I S T E N E R ---------------------------------------
	public void windowActivated(WindowEvent e)
	{
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{
		this.saveConf();
	}

	public void windowDeactivated(WindowEvent e)
	{
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowIconified(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}

	// E V E N T   H A N D L E R -------------------------------------------
	/**
	 * "Send" button event handler
	 */
	private void jbtnSend_ActionPerformed()
	{
		if
		(
			this.contacts != null && this.contacts.size() > 0
			&& this.etaMsg.getText().length() > 0
		)
		{
			EzimMsgSender jmsTmp = new EzimMsgSender(this);
			EzimThreadPool.getInstance().execute(jmsTmp);
		}
	}

	/**
	 * "Delete last recipient" button event handler
	 */
	private void jbtnDelLastRcpt_ActionPerformed()
	{
		if (this.contacts != null && this.contacts.size() > 0)
		{
			synchronized(this.contacts)
			{
				this.contacts.remove(this.contacts.size() - 1);
			}

			this.updateContactNames();
		}
	}

	// P U B L I C   M E T H O D -------------------------------------------
	/**
	 * add contacts to the recipient list
	 * @param lIn contacts to be added
	 */
	public void addContacts(List<EzimContact> lIn)
	{
		if (this.contacts != null)
		{
			synchronized(this.contacts)
			{
				for(EzimContact ecTmp: lIn)
				{
					if (! this.contacts.contains(ecTmp))
					{
						this.contacts.add(ecTmp);
					}
				}
			}

			this.updateContactNames();
		}
	}

	/**
	 * get contacts of the recipients
	 * @return contacts of the recipients
	 */
	public List<EzimContact> getContacts()
	{
		return this.contacts;
	}

	/**
	 * get subject line
	 * @return subject line
	 */
	public String getSubject()
	{
		return this.etfSbj.getText();
	}

	/**
	 * get message body
	 * @return message body
	 */
	public String getBody()
	{
		return this.etaMsg.getText();
	}
}
