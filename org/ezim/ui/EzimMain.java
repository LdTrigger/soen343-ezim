/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2009  Chun-Kwong Wong
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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

public class EzimMain
	extends JFrame
	implements WindowListener
{
	private static EzimMain emSngtn;

	private JPanel jpnlBase;
	private JComboBox jcbState;
	private JTextField jtfdStatus;
	private JLabel jlblAbout;
	private JList jlstContacts;
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
	public int colorSelf;

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
		EzimConf ecTmp = EzimConf.getInstance();

		this.setLocation
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainLocationX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainSizeW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainSizeH
				)
			)
		);
		this.epMain.setLocation
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimplazaLocationX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimplazaLocationY
				)
			)
		);
		this.epMain.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimplazaSizeW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimplazaSizeH
				)
			)
		);

		this.setAlwaysOnTop
		(
			Boolean.parseBoolean
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainAlwaysontop
				)
			)
		);

		this.showHide
		(
			Boolean.parseBoolean
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmainVisible
				)
			)
		);

		this.autoOpenMsgIn = Boolean.parseBoolean
		(
			ecTmp.settings.getProperty
			(
				EzimConf.ezimmsginAutoopen
			)
		);

		this.colorSelf = Integer.parseInt
		(
			ecTmp.settings.getProperty(EzimConf.ezimColorSelf)
			, 16
		);

		return;
	}

	/**
	 * save configuration settings
	 */
	private void saveConf()
	{
		EzimConf ecTmp = EzimConf.getInstance();

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

		return;
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

		return;
	}

	/**
	 * initialize GUI components
	 */
	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jcbState = new JComboBox(EzimImage.icoStates);
		this.jcbState.setEditable(false);
		this.jcbState.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jcbState_ActionPerformed();
					return;
				}
			}
		);
		this.jcbState.setToolTipText(EzimLang.ClickToChangeState);

		this.jtfdStatus = new JTextField
		(
			new EzimPlainDocument(Ezim.maxAckLength)
			, EzimContact.STATUS_DEFAULT
			, 1
		);
		this.jtfdStatus.setEnabled(false);
		this.jtfdStatus.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMain.this.jtfdStatus_ActionPerformed();
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
					EzimMain.this.jtfdStatus_MouseClicked();
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
					EzimMain.this.jtfdStatus_FocusLost();
					return;
				}
			}
		);
		this.jtfdStatus.setToolTipText(EzimLang.ClickToChangeStatus);

		this.jlstContacts = new JList(EzimContactList.getInstance());
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
					return;
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
					return;
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
					return;
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
					return;
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
					return;
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
							this.jtfdStatus
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
			, this.jtfdStatus
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
					this.jtfdStatus
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

		return;
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
				EzimConf ecTmp = EzimConf.getInstance();

				// save whether window is visible
				ecTmp.settings.setProperty
				(
					EzimConf.ezimmainVisible
					, String.valueOf(EzimMain.this.isVisible())
				);

				EzimMain.this.showHide(true);
				EzimMain.this.saveConfAckOff();

				System.exit(0);
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
					EzimMain.this.showHide
					(
						! EzimMain.this.isVisible()
					);
				}

				public void mouseEntered(MouseEvent me)
				{
					return;
				}

				public void mouseExited(MouseEvent me)
				{
					return;
				}

				public void mousePressed(MouseEvent me)
				{
					return;
				}

				public void mouseReleased(MouseEvent me)
				{
					return;
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
			this.errAlert(awtE.getMessage());
			System.exit(1);
		}

		return;
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
		return;
	}

	public void windowClosed(WindowEvent e)
	{
		return;
	}

	public void windowClosing(WindowEvent e)
	{
		if (this.tiMain == null)
		{
			this.saveConfAckOff();
			System.exit(0);
		}

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
	/**
	 * state combobox event handler
	 */
	private void jcbState_ActionPerformed()
	{
		this.changeState(this.jcbState.getSelectedIndex());
		return;
	}

	/**
	 * status textfield event handler
	 */
	private void jtfdStatus_ActionPerformed()
	{
		this.changeStatus();
		return;
	}

	/**
	 * status textfield event handler on mouse click
	 */
	private void jtfdStatus_MouseClicked()
	{
		this.jtfdStatus.setEnabled(true);
		this.jtfdStatus.grabFocus();	// maybe inappropriate
		return;
	}

	/**
	 * status textfield event handler on focus lost
	 */
	private void jtfdStatus_FocusLost()
	{
		this.changeStatus();
		return;
	}

	/**
	 * about label event handler on mouse click
	 */
	private void jlblAbout_MouseClicked()
	{
		this.showAboutDlg();
		return;
	}

	/**
	 * open an outgoing message window
	 */
	private void openMsgOut()
	{
		Object obj[] = this.jlstContacts.getSelectedValues();

		if (obj != null && obj.length > 0)
		{
			ArrayList<EzimContact> alTmp = new ArrayList<EzimContact>();

			for(int iCnt = 0; iCnt < obj.length; iCnt ++)
			{
				alTmp.add((EzimContact) obj[iCnt]);
			}

			new EzimMsgOut(alTmp);
		}
		else
		{
			new EzimMsgOut();
		}

		return;
	}

	/**
	 * contact list event handler on mouse double click
	 */
	private void jlstContacts_MouseDblClicked()
	{
		this.openMsgOut();
		return;
	}

	/**
	 * message button event handler
	 */
	private void jbtnMsg_ActionPerformed()
	{
		this.openMsgOut();
		return;
	}

	/**
	 * file transfer button event handler
	 */
	private void jbtnFtx_ActionPerformed()
	{
		Object obj[] = this.jlstContacts.getSelectedValues();

		if (obj != null && obj.length == 1)
		{
			EzimConf econf = EzimConf.getInstance();
			EzimContact ecTmp
				= (EzimContact) this.jlstContacts.getSelectedValue();
			InetAddress iaTmp = ecTmp.getAddress();

			JFileChooser jfcTmp = new JFileChooser
			(
				econf.settings.getProperty
				(
					EzimConf.ezimfileoutDirectory
				)
			);
			int iJfcRes = jfcTmp.showOpenDialog(this);

			if (iJfcRes == JFileChooser.APPROVE_OPTION)
			{
				ecTmp = EzimContactList.getInstance().getContact(iaTmp);

				if (ecTmp == null)
				{
					JOptionPane.showMessageDialog
					(
						null
						, EzimLang.RecipientNotExists
						, EzimLang.Error
						, JOptionPane.ERROR_MESSAGE
					);
				}
				else
				{
					econf.settings.setProperty
					(
						EzimConf.ezimfileoutDirectory
						, jfcTmp.getCurrentDirectory().getAbsolutePath()
					);

					new EzimFileOut
					(
						ecTmp
						, jfcTmp.getSelectedFile()
					);
				}
			}
		}

		return;
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

					return;
				}
			}
		);

		return;
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
		return;
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

		return;
	}

	/**
	 * preferences button event handler
	 */
	private void jbtnPrefs_ActionPerformed()
	{
		new EzimPreferences();
		return;
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
	 * @param strIn the error message to be shown
	 */
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

	// O P E R A T I V E   M E T H O D -------------------------------------
	/**
	 * save configurations and acknowledge peers we're going offline
	 */
	private void saveConfAckOff()
	{
		// save configurations
		this.saveConf();

		EzimThreadPool etpTmp = EzimThreadPool.getInstance();

		// change our state back to default, if it was something else
		// (i.e. leave the plaza if we were there)
		if (this.localSysState != EzimContact.SYSSTATE_DEFAULT)
		{
			EzimAckSender easDS = new EzimAckSender
			(
				EzimAckSemantics.sysState(EzimContact.SYSSTATE_DEFAULT)
			);
			etpTmp.execute(easDS);
		}

		// acknowledge other peers we're going offline
		EzimAckSender easOff = new EzimAckSender
		(
			EzimAckSemantics.offline()
		);
		etpTmp.execute(easOff);

		return;
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

		return;
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
			strTmp = EzimContact.STATUS_DEFAULT;
			this.jtfdStatus.setText(strTmp);
		}

		if (! this.localStatus.equals(strTmp))
		{
			this.localStatus = strTmp;

			easTmp = new EzimAckSender
			(
				EzimAckSemantics.status(this.localStatus)
			);
			EzimThreadPool.getInstance().execute(easTmp);
		}

		this.jtfdStatus.setEnabled(false);

		return;
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

		return;
	}

	/**
	 * execute proper ending processes when JVM shuts down unexpectedly
	 */
	public void panic()
	{
		this.showHide(true);
		this.saveConfAckOff();

		return;
	}
}
