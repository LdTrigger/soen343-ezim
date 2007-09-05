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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;
import org.ezim.ui.EzimMsgOut;

public class EzimMain
	extends JFrame
	implements WindowListener
{
	private ArrayList<EzimContact> alContacts;
	private JPanel jpnlBase;
	private JLabel jlblStatus;
	private JTextField jtfdStatus;
	private JLabel jlblAbout;
	private JList jlstContacts;
	private JScrollPane jspContacts;
	private JButton jbtnMsg;
	private JButton jbtnRfh;

	public String localAddress;
	public String localName;
	public String localStatus;

	// resource bundle properties
	public static String rbpNotice;
	public static String rbpAbout;
	public static String rbpStatus;
	public static String rbpMessage;
	public static String rbpSendMessageToContact;
	public static String rbpRefresh;
	public static String rbpRefreshContactList;
	public static String rbpError;
	public static String rbpIncomingMessage;
	public static String rbpFrom;
	public static String rbpClickHereToOpenMessage;
	public static String rbpReply;
	public static String rbpOriginalMessageFrom;
	public static String rbpOutgoingMessage;
	public static String rbpTo;
	public static String rbpSend;

	// C O N S T R U C T O R
	public EzimMain()
	{
		this.initData();
		this.initGUI();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(Ezim.appAbbrev);
		this.setMinimumSize(new Dimension(210, 300));
		this.setVisible(true);
	}

	private void initData()
	{
		try
		{
			this.localAddress = InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception e)
		{
			this.localAddress = "127.0.0.1";
		}

		this.localStatus = EzimContact.DEFAULT_STATUS;
		this.alContacts = new ArrayList<EzimContact>();

		try
		{
			ResourceBundle rbTmp = ResourceBundle.getBundle
			(
				"org/ezim/properties/EzimUI"
			);

			rbpNotice = Ezim.appName + "\n"
				+ "version " + Ezim.appVer + "\n\n"
				+ rbTmp.getString("NOTICE");

			rbpAbout = rbTmp.getString("ABOUT");
			rbpStatus = rbTmp.getString("STATUS");
			rbpMessage = rbTmp.getString("MESSAGE");
			rbpSendMessageToContact = rbTmp.getString("SEND_MESSAGE_TO_CONTACT");
			rbpRefresh = rbTmp.getString("REFRESH");
			rbpRefreshContactList = rbTmp.getString("REFRESH_CONTACT_LIST");
			rbpError = rbTmp.getString("ERROR");
			rbpIncomingMessage = rbTmp.getString("INCOMING_MESSAGE");
			rbpFrom = rbTmp.getString("FROM");
			rbpClickHereToOpenMessage = rbTmp.getString("CLICK_HERE_TO_OPEN_MESSAGE");
			rbpReply = rbTmp.getString("REPLY");
			rbpOriginalMessageFrom = rbTmp.getString("ORIGINAL_MESSAGE_FROM");
			rbpOutgoingMessage = rbTmp.getString("OUTGOING_MESSAGE");
			rbpTo = rbTmp.getString("TO");
			rbpSend = rbTmp.getString("SEND");
		}
		catch(Exception e)
		{
			// properties file is obligatory
			System.out.println(e.getMessage());
			System.exit(1);
		}

		return;
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblStatus = new JLabel(EzimMain.rbpStatus);

		this.jtfdStatus = new JTextField(EzimContact.DEFAULT_STATUS);
		this.jtfdStatus.setEnabled(false);
		this.jtfdStatus.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jtfdStatus_ActionPerformed(evtTmp);
					return;
				}
			}
		);
		this.jtfdStatus.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					jtfdStatus_MouseClicked(evtTmp);
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
		this.jtfdStatus.addFocusListener
		(
			new FocusListener()
			{
				public void focusGained(FocusEvent evtTmp)
				{
					return;
				}

				public void focusLost(FocusEvent evtTmp)
				{
					jtfdStatus_FocusLost(evtTmp);
					return;
				}
			}
		);

		this.jlblAbout = new JLabel("?");
		this.jlblAbout.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					jlblAbout_MouseClicked(evtTmp);
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

		this.jlstContacts = new JList();
		this.jlstContacts.setCellRenderer(new EzimContactListRenderer());
		this.jlstContacts.setSelectionMode
		(
			ListSelectionModel.SINGLE_SELECTION
		);

		this.jspContacts = new JScrollPane(this.jlstContacts);

		this.jbtnMsg = new JButton(EzimMain.rbpMessage);
		this.jbtnMsg.setToolTipText(EzimMain.rbpSendMessageToContact);
		this.jbtnMsg.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnMsg_ActionPerformed(evtTmp);
					return;
				}
			}
		);

		this.jbtnRfh = new JButton(EzimMain.rbpRefresh);
		this.jbtnRfh.setToolTipText(EzimMain.rbpRefreshContactList);
		this.jbtnRfh.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnRfh_ActionPerformed(evtTmp);
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
					.addComponent(this.jlblStatus)
					.addComponent
					(
						this.jtfdStatus
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Integer.MAX_VALUE
					)
					.addComponent(this.jlblAbout)
			)
			.addComponent(this.jspContacts)
			.addGroup
			(
				glBase.createSequentialGroup()
					.addComponent
					(
						this.jbtnMsg
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Integer.MAX_VALUE
					)
					.addComponent(this.jbtnRfh)
			)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jlblStatus)
			.addComponent(this.jtfdStatus)
			.addComponent(this.jlblAbout)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jspContacts)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jbtnMsg)
			.addComponent(this.jbtnRfh)
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
		EzimAckSender easTmp = new EzimAckSender
		(
			this
			, EzimAckSemantics.offline()
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
	private void jbtnMsg_ActionPerformed(ActionEvent evt)
	{
		if (this.jlstContacts.getSelectedValue() != null)
		{
			EzimMsgOut jmoTmp = new EzimMsgOut
			(
				(EzimContact) this.jlstContacts.getSelectedValue()
			);
		}

		return;
	}

	private void jbtnRfh_ActionPerformed(ActionEvent evt)
	{
		this.freshPoll();
		return;
	}

	private void jtfdStatus_ActionPerformed(ActionEvent evt)
	{
		this.changeStatus();
		return;
	}

	private void jtfdStatus_MouseClicked(MouseEvent evt)
	{
		this.jtfdStatus.setEnabled(true);
		this.jtfdStatus.grabFocus();	// maybe inappropriate
		return;
	}

	private void jtfdStatus_FocusLost(FocusEvent evt)
	{
		this.changeStatus();
		return;
	}

	private void jlblAbout_MouseClicked(MouseEvent evt)
	{
		this.showAboutDlg();
		return;
	}

	// A B O U T   D I A L O G ---------------------------------------------
	private void showAboutDlg()
	{
		JOptionPane.showMessageDialog
		(
			this
			, this.rbpNotice
			, EzimMain.rbpAbout
			, JOptionPane.PLAIN_MESSAGE
		);
	}

	// E R R O R   H A N D L E R -------------------------------------------
	public void errAlert(String strIn)
	{
		JOptionPane.showMessageDialog
		(
			this
			, strIn
			, EzimMain.rbpError
			, JOptionPane.ERROR_MESSAGE
		);

		return;
	}

	// C O N T A C T   M A N I P U L A T I O N -----------------------------
	/**
	 * refresh contact list contents
	 */
	private void refreshContactList()
	{
		EzimContact[] ecTmp = (EzimContact[]) this.alContacts.toArray
		(
			new EzimContact[0]
		);

		this.jlstContacts.setListData(ecTmp);
		this.jlstContacts.repaint();

		return;
	}

	/**
	 * find and return index of the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return index of the contact, or -1 if not found
	 */
	private int idxContact(String strIp)
	{
		int iOut = -1;
		int iCnt = 0;
		int iLen = alContacts.size();
		EzimContact ecTmp = null;

		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			ecTmp = (EzimContact) this.alContacts.get(iCnt);

			if (strIp.equals(ecTmp.getIp()))
			{
				iOut = iCnt;
				break;
			}
		}

		return iOut;
	}

	/**
	 * find and return the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return instance of the contact, or null if not found
	 */
	public EzimContact getContact(String strIp)
	{
		EzimContact ecOut = null;
		int iIdx = idxContact(strIp);

		if (iIdx != -1)
			ecOut = (EzimContact) this.alContacts.get(iIdx);

		return ecOut;
	}

	/**
	 * add a new contact to the list if not yet exists
	 * @param strIp IP address of the new contact
	 * @param strname name of the new contact
	 * @param strStatus status of the new contact
	 */
	public void addContact(String strIp, String strName, String strStatus)
	{
		if (this.idxContact(strIp) == -1)
		{
			try
			{
				// place the user him/herself as the first entry
				if (strIp.equals(this.localAddress))
				{
					this.alContacts.add
					(
						0
						, new EzimContact(strIp, strName, strStatus)
					);
				}
				else
				{
					this.alContacts.add
					(
						new EzimContact(strIp, strName, strStatus)
					);
				}
			}
			catch(EzimContactException eceTmp)
			{
				this.errAlert(eceTmp.getMessage());
			}
			finally
			{
				this.refreshContactList();
			}
		}

		return;
	}

	/**
	 * remove contact from the list if exists
	 * @param strIp IP address of the contact to be removed
	 */
	public void rmContact(String strIp)
	{
		int iIdx = idxContact(strIp);

		if (iIdx != -1)
		{
			try
			{
				this.alContacts.remove(iIdx);
			}
			catch(Exception e)
			{
				this.errAlert(e.getMessage());
			}
			finally
			{
				this.refreshContactList();
			}
		}

		return;
	}

	/**
	 * update an existing contact with the new status
	 * @param strIp IP address of the contact to be updated
	 * @param strStatus new status of the contact
	 */
	public void updContactStatus(String strIp, String strStatus)
	{
		EzimContact ecTmp = this.getContact(strIp);

		if (ecTmp != null)
		{
			ecTmp.setStatus(strStatus);
			this.refreshContactList();
		}

		return;
	}

	// O P E R A T I V E   M E T H O D -------------------------------------
	/**
	 * change status and notify all peers for status change
	 */
	private void changeStatus()
	{
		String strTmp = this.jtfdStatus.getText().trim();
		EzimAckSender easTmp = null;

		if (strTmp.length() == 0)
		{
			strTmp = EzimContact.DEFAULT_STATUS;
			this.jtfdStatus.setText(strTmp);
		}

		this.localStatus = strTmp;

		easTmp = new EzimAckSender
		(
			this
			, EzimAckSemantics.status(this.localStatus)
		);
		easTmp.start();

		this.jtfdStatus.setEnabled(false);

		return;
	}

	/**
	 * clear contact list and poll from all peers
	 */
	public void freshPoll()
	{
		this.alContacts = new ArrayList<EzimContact>();
		this.refreshContactList();

		EzimAckSender easTmp = new EzimAckSender
		(
			this
			, EzimAckSemantics.poll(this.localName)
		);
		easTmp.start();
	}
}
