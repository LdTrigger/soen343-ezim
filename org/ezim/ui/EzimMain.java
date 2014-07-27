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

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.util.List;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactList;
import org.ezim.core.EzimContactTransferHandler;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimPlainDocument;
import org.ezim.core.EzimThreadPool;

import org.ezim.ui.EzimFileOut;
import org.ezim.ui.EzimMsgOut;
import org.ezim.ui.EzimPlaza;
import org.ezim.ui.EzimPreferences;
import org.ezim.ui.EzimTextField;

public class EzimMain
	extends JFrame
	implements WindowListener
{
	private static EzimMain emSngtn;

	private JPanel jpnlBase;
	private JComboBox jcbState;
	private EzimTextField etfStatus;
	private JLabel jlblAbout;
	private JList<EzimContact> jlstContacts;
	private JScrollPane jspContacts;
	private JButton jbtnMsg;
	private JButton jbtnFtx;
	private JButton jbtnRfh;
	private JButton jbtnPlz;
	private JButton jbtnPrefs;

	private TrayIcon tiMain;
	public int localSysState;
	public int localState;
	public String localStatus;

	public boolean autoOpenMsgIn;

	public EzimPlaza epMain;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the main window GUI
	 */
	private EzimMain()
	{
		this.initData();
		this.initGUI();

		this.setIconImage(EzimImage.icoMain.getImage());
		this.setTitle(Ezim.appAbbrev);
		this.pack();
		this.setMinimumSize(this.getSize());

		this.loadConf();
	}

	/**
	 * load configuration settings
	 */
	private void loadConf()
	{
		this.setLocation
		(
			EzimConf.UI_MAIN_LOCATION_X
			, EzimConf.UI_MAIN_LOCATION_Y
		);
		this.setSize
		(
			EzimConf.UI_MAIN_SIZE_W
			, EzimConf.UI_MAIN_SIZE_H
		);
		this.epMain.setLocation
		(
			EzimConf.UI_PLAZA_LOCATION_X
			, EzimConf.UI_PLAZA_LOCATION_Y
		);
		this.epMain.setSize
		(
			EzimConf.UI_PLAZA_SIZE_W
			, EzimConf.UI_PLAZA_SIZE_H
		);

		this.setAlwaysOnTop
		(
			EzimConf.UI_MAIN_ALWAYSONTOP
		);

		this.showHide
		(
			EzimConf.UI_MAIN_VISIBLE
		);

		this.autoOpenMsgIn = EzimConf.UI_MSGIN_AUTOOPEN;
	}

	/**
	 * save configuration settings
	 */
	private void saveConf()
	{
		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		EzimConf.UI_MAIN_LOCATION_X = (int) ptTmp.getX();
		EzimConf.UI_MAIN_LOCATION_Y = (int) ptTmp.getY();

		Dimension dmTmp = this.getSize();
		EzimConf.UI_MAIN_SIZE_W = (int) dmTmp.getWidth();
		EzimConf.UI_MAIN_SIZE_H = (int) dmTmp.getHeight();

		ptTmp = this.epMain.getLocation();
		EzimConf.UI_PLAZA_LOCATION_X = (int) ptTmp.getX();
		EzimConf.UI_PLAZA_LOCATION_Y = (int) ptTmp.getY();

		dmTmp = this.epMain.getSize();
		EzimConf.UI_PLAZA_SIZE_W = (int) dmTmp.getWidth();
		EzimConf.UI_PLAZA_SIZE_H = (int) dmTmp.getHeight();

		EzimConf.write();
	}

	/**
	 * initialize class members
	 */
	private void initData()
	{
		this.localSysState = EzimContact.SYSSTATE_DEFAULT;
		this.localState = EzimContact.STATE_DEFAULT;
		this.localStatus = EzimContact.STATUS_DEFAULT;

		this.epMain = new EzimPlaza();
	}

	/**
	 * initialize GUI components
	 */
	@SuppressWarnings("unchecked")
	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jcbState = new JComboBox<ImageIcon>(EzimImage.icoStates);
		this.jcbState.setEditable(false);
		this.jcbState.addItemListener
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent evtTmp)
				{
					if (ItemEvent.SELECTED == evtTmp.getStateChange())
						EzimMain.this.jcbState_StateChanged();
				}
			}
		);
		this.jcbState.setToolTipText(EzimLang.ClickToChangeState);

		this.etfStatus = new EzimTextField
		(
			new EzimPlainDocument(Ezim.maxAckLength)
			, EzimContact.STATUS_DEFAULT
			, 1
			, false
		);
		this.etfStatus.setEnabled(false);
		this.etfStatus.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.etfStatus_ActionPerformed();
				}
			}
		);
		this.etfStatus.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					EzimMain.this.etfStatus_MouseClicked();
				}

				public void mouseEntered(MouseEvent evtTmp)
				{
				}

				public void mouseExited(MouseEvent evtTmp)
				{
				}

				public void mousePressed(MouseEvent evtTmp)
				{
				}

				public void mouseReleased(MouseEvent evtTmp)
				{
				}
			}
		);
		this.etfStatus.addFocusListener
		(
			new FocusListener()
			{
				public void focusGained(FocusEvent evtTmp)
				{
				}

				public void focusLost(FocusEvent evtTmp)
				{
					EzimMain.this.etfStatus_FocusLost();
				}
			}
		);
		this.etfStatus.setToolTipText(EzimLang.ClickToChangeStatus);

		this.jlstContacts = new JList<EzimContact>
		(
			EzimContactList.getInstance()
		);
		this.jlstContacts.setCellRenderer(new EzimContactListRenderer());
		this.jlstContacts.setSelectionMode
		(
			ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
		);
		this.jlstContacts.setTransferHandler
		(
			EzimContactTransferHandler.getInstance()
		);
		this.jlstContacts.setDragEnabled(true);
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
						EzimMain.this.jlstContacts_MouseDblClicked();
					}
				}

				public void mouseEntered(MouseEvent evtTmp)
				{
				}

				public void mouseExited(MouseEvent evtTmp)
				{
				}

				public void mousePressed(MouseEvent evtTmp)
				{
				}

				public void mouseReleased(MouseEvent evtTmp)
				{
				}
			}
		);

		this.jspContacts = new JScrollPane(this.jlstContacts);
		this.jspContacts.setPreferredSize(new Dimension(1, 100));

		this.jbtnMsg = new JButton(EzimImage.icoButtons[0]);
		this.jbtnMsg.setPreferredSize(new Dimension(32, 32));
		this.jbtnMsg.setToolTipText(EzimLang.SendMessageToContact);
		this.jbtnMsg.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jbtnMsg_ActionPerformed();
				}
			}
		);

		this.jbtnFtx = new JButton(EzimImage.icoButtons[1]);
		this.jbtnFtx.setPreferredSize(new Dimension(32, 32));
		this.jbtnFtx.setToolTipText(EzimLang.SendFileToContact);
		this.jbtnFtx.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jbtnFtx_ActionPerformed();
				}
			}
		);

		this.jbtnRfh = new JButton(EzimImage.icoButtons[2]);
		this.jbtnRfh.setPreferredSize(new Dimension(32, 32));
		this.jbtnRfh.setToolTipText(EzimLang.RefreshContactList);
		this.jbtnRfh.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jbtnRfh_ActionPerformed();
				}
			}
		);

		this.jbtnPlz = new JButton(EzimImage.icoButtons[3]);
		this.jbtnPlz.setPreferredSize(new Dimension(32, 32));
		this.jbtnPlz.setToolTipText(EzimLang.PlazaOfSpeech);
		this.jbtnPlz.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jbtnPlz_ActionPerformed();
				}
			}
		);

		this.jbtnPrefs = new JButton(EzimImage.icoButtons[4]);
		this.jbtnPrefs.setPreferredSize(new Dimension(32, 32));
		this.jbtnPrefs.setToolTipText(EzimLang.Prefs);
		this.jbtnPrefs.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jbtnPrefs_ActionPerformed();
				}
			}
		);

		this.jlblAbout = new JLabel(EzimImage.icoLblAbout);
		this.jlblAbout.setPreferredSize(new Dimension(16, 16));
		this.jlblAbout.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					EzimMain.this.jlblAbout_MouseClicked();
				}

				public void mouseEntered(MouseEvent evtTmp)
				{
				}

				public void mouseExited(MouseEvent evtTmp)
				{
				}

				public void mousePressed(MouseEvent evtTmp)
				{
				}

				public void mouseReleased(MouseEvent evtTmp)
				{
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
						.addComponent
						(
							this.jcbState
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.etfStatus
							, GroupLayout.DEFAULT_SIZE
							, GroupLayout.PREFERRED_SIZE
							, Short.MAX_VALUE
						)
				)
				.addGroup
				(
					glBase.createSequentialGroup()
						.addComponent
						(
							this.jspContacts
							, GroupLayout.DEFAULT_SIZE
							, GroupLayout.PREFERRED_SIZE
							, Short.MAX_VALUE
						)
				)
				.addGroup
				(
					glBase.createSequentialGroup()
						.addComponent
						(
							this.jbtnMsg
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnFtx
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnRfh
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnPlz
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnPrefs
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
				.addGroup
				(
					glBase.createSequentialGroup()
						.addContainerGap
						(
							0
							, Short.MAX_VALUE
						)
						.addComponent
						(
							this.jlblAbout
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		glBase.linkSize
		(
			SwingUtilities.VERTICAL
			, this.jcbState
			, this.etfStatus
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jcbState
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.etfStatus
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
					this.jspContacts
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jbtnMsg
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnFtx
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnRfh
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnPlz
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnPrefs
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
					this.jlblAbout
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glBase.setVerticalGroup(vGrp);

		// W I N D O W   L I S T E N E R -----------------------------------
		this.addWindowListener(this);

		// T R A Y   I C O N -----------------------------------------------
		if (SystemTray.isSupported()) this.initTrayIcon();
		else this.tiMain = null;
	}

	/**
	 * initialize system tray icon
	 */
	private void initTrayIcon()
	{
		// P O P - U P   M E N U -------------------------------------------
		PopupMenu pmTmp = new PopupMenu();

		MenuItem miRestore = new MenuItem(EzimLang.ShowHide);
		ActionListener alRestore = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EzimMain.this.showHide
				(
					! EzimMain.this.isVisible()
				);
			}
		};
		miRestore.addActionListener(alRestore);
		pmTmp.add(miRestore);

		MenuItem miAbout = new MenuItem(EzimLang.About);
		ActionListener alAbout = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EzimMain.this.showAboutDlg();
			}
		};
		miAbout.addActionListener(alAbout);
		pmTmp.add(miAbout);

		MenuItem miSeparator = new MenuItem("-");
		pmTmp.add(miSeparator);

		MenuItem miExit = new MenuItem(EzimLang.Exit);
		ActionListener alExit = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// save whether window is visible
				EzimConf.UI_MAIN_VISIBLE = EzimMain.this.isVisible();
				EzimMain.this.panic(0);
			}
		};
		miExit.addActionListener(alExit);
		pmTmp.add(miExit);

		// S Y S T E M   T R A Y   I C O N ---------------------------------
		SystemTray stTmp = SystemTray.getSystemTray();

		this.tiMain = new TrayIcon
		(
			EzimImage.icoMain.getImage()
			, Ezim.appName
			, pmTmp
		);
		this.tiMain.setImageAutoSize(true);
		this.tiMain.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent me)
				{
				}

				public void mouseEntered(MouseEvent me)
				{
				}

				public void mouseExited(MouseEvent me)
				{
				}

				public void mousePressed(MouseEvent me)
				{
				}

				public void mouseReleased(MouseEvent me)
				{
					if (! me.isPopupTrigger())
					{
						EzimMain.this.showHide
						(
							! EzimMain.this.isVisible()
						);
					}
				}
			}
		);

		try
		{
			stTmp.add(this.tiMain);
		}
		catch(AWTException awtE)
		{
			EzimLogger.getInstance().severe(awtE.getMessage(), awtE);
			EzimMain.showError(awtE.getMessage());
			Ezim.exit(1);
		}
	}

	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimMain getInstance()
	{
		if (EzimMain.emSngtn == null)
			EzimMain.emSngtn = new EzimMain();

		return EzimMain.emSngtn;
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
		if (this.tiMain == null) this.panic(0);
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
	 * state combobox event handler
	 */
	private void jcbState_StateChanged()
	{
		this.changeState(this.jcbState.getSelectedIndex());
	}

	/**
	 * status textfield event handler
	 */
	private void etfStatus_ActionPerformed()
	{
		this.changeStatus();
	}

	/**
	 * status textfield event handler on mouse click
	 */
	private void etfStatus_MouseClicked()
	{
		this.etfStatus.setEnabled(true);
		this.etfStatus.grabFocus();	// maybe inappropriate
	}

	/**
	 * status textfield event handler on focus lost
	 */
	private void etfStatus_FocusLost()
	{
		this.changeStatus();
	}

	/**
	 * about label event handler on mouse click
	 */
	private void jlblAbout_MouseClicked()
	{
		this.showAboutDlg();
	}

	/**
	 * open an outgoing message window
	 */
	private void openMsgOut()
	{
		List<EzimContact> lTmp = this.jlstContacts.getSelectedValuesList();

		if (lTmp.isEmpty())
			new EzimMsgOut();
		else
			new EzimMsgOut(lTmp);
	}

	/**
	 * contact list event handler on mouse double click
	 */
	private void jlstContacts_MouseDblClicked()
	{
		this.openMsgOut();
	}

	/**
	 * message button event handler
	 */
	private void jbtnMsg_ActionPerformed()
	{
		this.openMsgOut();
	}

	/**
	 * file transfer button event handler
	 */
	private void jbtnFtx_ActionPerformed()
	{
		List<EzimContact> lTmp = this.jlstContacts.getSelectedValuesList();

		if (lTmp.size() == 1)
		{
			EzimContact ecTmp
				= (EzimContact) this.jlstContacts.getSelectedValue();
			InetAddress iaTmp = ecTmp.getAddress();

			JFileChooser jfcTmp = new JFileChooser
			(
				EzimConf.UI_FILEOUT_DIRECTORY
			);
			int iJfcRes = jfcTmp.showOpenDialog(this);

			if (iJfcRes == JFileChooser.APPROVE_OPTION)
			{
				ecTmp = EzimContactList.getInstance().getContact(iaTmp);

				if (ecTmp == null)
				{
					EzimMain.showError(null, EzimLang.RecipientNotExists);
				}
				else
				{
					EzimConf.UI_FILEOUT_DIRECTORY
						= jfcTmp.getCurrentDirectory().getAbsolutePath();

					new EzimFileOut
					(
						ecTmp
						, jfcTmp.getSelectedFile()
					);
				}
			}
		}
	}

	/**
	 * refresh button event handler
	 */
	private void jbtnRfh_ActionPerformed()
	{
		this.jbtnRfh.setEnabled(false);
		this.freshPoll();

		EzimThreadPool.getInstance().execute
		(
			new Thread()
			{
				public void run()
				{
					try
					{
						Thread.sleep(Ezim.rfhBtnTI);
					}
					catch(Exception e)
					{
						EzimLogger.getInstance().severe
						(
							e.getMessage()
							, e
						);
					}

					EzimMain.this.jbtnRfh.setEnabled(true);
				}
			}
		);
	}

	/**
	 * plaza (chat) button event handler
	 */
	private void jbtnPlz_ActionPerformed()
	{
		if (! this.epMain.isVisible())
		{
			this.epMain.reset();
			this.changeSysState(EzimContact.SYSSTATE_PLAZA);
		}
	}

	/**
	 * show or hide this main window instance
	 * @param blnIn show if true, hide otherwise
	 */
	private void showHide(boolean blnIn)
	{
		boolean blnSysTray = SystemTray.isSupported();

		if (blnSysTray) this.setState(JFrame.NORMAL);

		this.setVisible(blnIn || ! blnSysTray);
	}

	/**
	 * preferences button event handler
	 */
	private void jbtnPrefs_ActionPerformed()
	{
		new EzimPreferences();
	}

	// A B O U T   D I A L O G ---------------------------------------------
	/**
	 * show the about dialog
	 */
	private void showAboutDlg()
	{
		JOptionPane.showMessageDialog
		(
			null
			, EzimLang.Notice
			, EzimLang.About
			, JOptionPane.PLAIN_MESSAGE
			, EzimImage.icoMain
		);
	}

	// E R R O R   H A N D L E R -------------------------------------------
	/**
	 * show an error message dialog
	 * @param cpntParent the parent component of the dialog
	 * @param strMsg the error message to be shown
	 * @param strTitle the title string of the dialog
	 */
	public static void showError
	(
		Component cpntParent
		, String strMsg
		, String strTitle
	)
	{
		JOptionPane.showMessageDialog
		(
			cpntParent
			, strMsg
			, strTitle
			, JOptionPane.ERROR_MESSAGE
		);
	}

	/**
	 * show an error message dialog
	 * @param cpntParent the parent component of the dialog
	 * @param strMsg the error message to be shown
	 */
	public static void showError(Component cpntParent, String strMsg)
	{
		EzimMain.showError
		(
			cpntParent
			, strMsg
			, EzimLang.Error
		);
	}

	/**
	 * show an error message dialog
	 * @param strMsg the error message to be shown
	 */
	public static void showError(String strMsg)
	{
		EzimMain.showError(EzimMain.emSngtn, strMsg);
	}

	// O P E R A T I V E   M E T H O D -------------------------------------
	/**
	 * save configurations and acknowledge peers we're going offline
	 */
	private void saveConfAckOff()
	{
		// save configurations
		this.saveConf();

		// change our state back to default, if it was something else
		// (i.e. leave the plaza if we were there)
		if (this.localSysState != EzimContact.SYSSTATE_DEFAULT)
		{
			EzimAckSender easDS = new EzimAckSender
			(
				EzimAckSemantics.sysState(EzimContact.SYSSTATE_DEFAULT)
			);
			EzimThreadPool.getInstance().execute(easDS);
		}
	}

	/**
	 * change system state and notify all peers for status change
	 */
	private void changeSysState(int iState)
	{
		EzimAckSender easTmp = null;

		if (this.localSysState != iState)
		{
			this.localSysState = iState;

			easTmp = new EzimAckSender
			(
				EzimAckSemantics.sysState(iState)
			);
			EzimThreadPool.getInstance().execute(easTmp);
		}
	}

	/**
	 * change state and notify all peers for status change
	 */
	private void changeState(int iState)
	{
		EzimAckSender easTmp = null;

		if (this.localState != iState)
		{
			this.localState = iState;

			easTmp = new EzimAckSender
			(
				EzimAckSemantics.state(iState)
			);
			EzimThreadPool.getInstance().execute(easTmp);
		}
	}

	/**
	 * change status and notify all peers for status change
	 */
	private void changeStatus()
	{
		String strTmp = this.etfStatus.getText();
		EzimAckSender easTmp = null;

		if (strTmp.length() == 0)
			strTmp = EzimContact.STATUS_DEFAULT;

		this.etfStatus.setText(strTmp);

		if (! this.localStatus.equals(strTmp))
		{
			this.localStatus = strTmp;

			easTmp = new EzimAckSender
			(
				EzimAckSemantics.status(this.localStatus)
			);
			EzimThreadPool.getInstance().execute(easTmp);
		}

		this.etfStatus.setEnabled(false);
	}

	/**
	 * clear contact list and poll from all peers
	 */
	public void freshPoll()
	{
		EzimContactList eclTmp = EzimContactList.getInstance(true);

		eclTmp.addContact
		(
			Ezim.localAddress
			, Ezim.localDtxPort
			, Ezim.localName
			, this.localSysState
			, this.localState
			, this.localStatus
		);

		EzimAckSender easTmp = new EzimAckSender
		(
			EzimAckSemantics.poll()
		);

		EzimThreadPool.getInstance().execute(easTmp);
	}

	/**
	 * execute proper ending processes when JVM shuts down unexpectedly
	 * @param iIn exit code
	 */
	public void panic(int iIn)
	{
		this.showHide(true);
		this.saveConfAckOff();
		Ezim.exit(iIn);
	}
}
