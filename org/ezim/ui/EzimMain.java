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
import java.awt.Point;
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
import java.util.Arrays;
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
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimPlainDocument;
import org.ezim.core.EzimSysTray;
import org.ezim.ui.EzimMsgOut;
import org.ezim.ui.EzimPlaza;

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
	private JButton jbtnPlz;

	public String localAddress;
	public String localName;
	public int localState;
	public String localStatus;

	public EzimPlaza epMain;
	private EzimSysTray ezimSysTray;

	
	// C O N S T R U C T O R
	public EzimMain(EzimSysTray ezimSysTray)
	{
		this.ezimSysTray = ezimSysTray;
		this.ezimSysTray.setMainWindow(this);
		this.initData();
		this.initGUI();

		this.setIconImage(EzimImage.icoMain.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(Ezim.appAbbrev);
		this.setMinimumSize(new Dimension(210, 300));
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

		this.localState = EzimContact.DEFAULT_STATE;
		this.localStatus = EzimContact.DEFAULT_STATUS;
		this.alContacts = new ArrayList<EzimContact>();

		this.epMain = new EzimPlaza(this);
		this.ezimSysTray.setContactList(this.alContacts);
		this.ezimSysTray.updateSysTrayMenue();
		this.ezimSysTray.setPlazaWindow(epMain);

		return;
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblStatus = new JLabel(EzimLang.Status);

		this.jtfdStatus = new JTextField
		(
			new EzimPlainDocument(Ezim.maxAckLength)
			, EzimContact.DEFAULT_STATUS
			, 0
		);
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
		this.jlstContacts.setCellRenderer(new EzimContactListRenderer(this));
		this.jlstContacts.setSelectionMode
		(
			ListSelectionModel.SINGLE_SELECTION
		);
		this.jlstContacts.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					if
					(
						evtTmp.getButton() == MouseEvent.BUTTON1
						&& evtTmp.getClickCount() > 1
					)
					{
						jlstContacts_MouseDblClicked(evtTmp);
					}
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

		this.jspContacts = new JScrollPane(this.jlstContacts);

		this.jbtnMsg = new JButton(EzimLang.Message);
		this.jbtnMsg.setToolTipText(EzimLang.SendMessageToContact);
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

		this.jbtnRfh = new JButton(EzimLang.Refresh);
		this.jbtnRfh.setToolTipText(EzimLang.RefreshContactList);
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

		this.jbtnPlz = new JButton(EzimLang.Plaza);
		this.jbtnPlz.setToolTipText(EzimLang.PlazaOfSpeech);
		this.jbtnPlz.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnPlz_ActionPerformed(evtTmp);
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
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Integer.MAX_VALUE
					)
					.addComponent(this.jlblAbout)
			)
			.addGroup
			(
				glBase.createSequentialGroup()
					.addComponent(this.jspContacts)
			)
			.addGroup
			(
				glBase.createSequentialGroup()
					.addComponent
					(
						this.jbtnMsg
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Integer.MAX_VALUE
					)
					.addComponent(this.jbtnRfh)
			)
			.addGroup
			(
				glBase.createSequentialGroup()
					.addComponent
					(
						this.jbtnPlz
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Integer.MAX_VALUE
					)
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

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent(this.jbtnPlz)
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
		EzimConf ecTmp = EzimConf.getInstance();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainLocalname
			, this.localName
		);

		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);
		ptTmp = this.epMain.getLocation();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimplazaLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimplazaLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		dmTmp = this.epMain.getSize();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimplazaSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimplazaSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);
		ecTmp.write();

		// acknowledge other peers we're going offline
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
	    setVisible(true);
		return;
	}

	public void windowIconified(WindowEvent e)
	{
	    setVisible(false);
		return;
	}

	public void windowOpened(WindowEvent e)
	{
		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
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

	private void openMsgOut()
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

	private void jlstContacts_MouseDblClicked(MouseEvent evt)
	{
		this.openMsgOut();
		return;
	}

	private void jbtnMsg_ActionPerformed(ActionEvent evt)
	{
		this.openMsgOut();
		return;
	}

	private void jbtnRfh_ActionPerformed(ActionEvent evt)
	{
		this.freshPoll();
		return;
	}

	private void jbtnPlz_ActionPerformed(ActionEvent evt)
	{
		if (! this.epMain.isVisible())
		{
			this.changeState(EzimContact.PLAZA_STATE);

			this.epMain.reset();
			this.epMain.setVisible(true);
		}
		return;
	}

	// A B O U T   D I A L O G ---------------------------------------------
	private void showAboutDlg()
	{
		JOptionPane.showMessageDialog
		(
			this
			, EzimLang.Notice
			, EzimLang.About
			, JOptionPane.PLAIN_MESSAGE
			, EzimImage.icoMain
		);
	}

	// E R R O R   H A N D L E R -------------------------------------------
	public void errAlert(String strIn)
	{
		JOptionPane.showMessageDialog
		(
			this
			, strIn
			, EzimLang.Error
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

		Arrays.sort(ecTmp);

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
				this.alContacts.add
				(
					new EzimContact(strIp, strName, strStatus)
				);
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

		ezimSysTray.updateSysTrayMenue();
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
		ezimSysTray.updateSysTrayMenue();
		return;
	}

	/**
	 * update an existing contact with the new state
	 * @param strIp IP address of the contact to be update
	 * @param iState new state of the contact
	 */
	public void updContactState(String strIp, int iState)
	{
		EzimContact ecTmp = this.getContact(strIp);

		if (ecTmp != null)
		{
			if (this.epMain.isVisible())
			{
				if
				(
					ecTmp.getState() == EzimContact.DEFAULT_STATE
					&& iState == EzimContact.PLAZA_STATE
				)
				{
					this.epMain.addNarration
					(
						strIp
						, EzimLang.HasJoinedPlazaOfSpeech
					);
				}
				else if
				(
					ecTmp.getState() == EzimContact.PLAZA_STATE
					&& iState == EzimContact.DEFAULT_STATE
				)
				{
					this.epMain.addNarration
					(
						strIp
						, EzimLang.HasLeftPlazaOfSpeech
					);
				}
			}

			ecTmp.setState(iState);
			this.refreshContactList();
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
	 * change state and notify all peers for status change
	 */
	public void changeState(int iState)
	{
		EzimAckSender easTmp = null;

		if (this.localState != iState)
		{
			this.localState = iState;

			easTmp = new EzimAckSender
			(
				this
				, EzimAckSemantics.state(iState)
			);
			easTmp.start();
		}

		return;
	}

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

		if (! this.localStatus.equals(strTmp))
		{
			this.localStatus = strTmp;

			easTmp = new EzimAckSender
			(
				this
				, EzimAckSemantics.status(this.localStatus)
			);
			easTmp.start();
		}

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
		this.ezimSysTray.setContactList(this.alContacts);
		this.ezimSysTray.updateSysTrayMenue();

		EzimAckSender easTmp = new EzimAckSender
		(
			this
			, EzimAckSemantics.poll(this.localName)
		);
		easTmp.start();
	}
}
