/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2013  Chun-Kwong Wong
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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;

import org.ezim.ui.EzimLocalAddressListRenderer;
import org.ezim.ui.EzimLocalNIListRenderer;
import org.ezim.ui.EzimLocaleListRenderer;
import org.ezim.ui.EzimMain;

public class EzimPreferences
	extends JDialog
{
	private JPanel jpnlBase;

	private JTabbedPane jtpBase;

	private JPanel jpnlNetwork;

	private JLabel jlblMcGroup;
	private JTextField jtfdMcGroup;
	private JButton jbtnRestoreToDefault;

	private JLabel jlblMcPort;
	private JSpinner jspnMcPort;

	private JLabel jlblDtxPort;
	private JSpinner jspnDtxPort;

	private JLabel jlblLocalAddress;
	private JComboBox<NetworkInterface> jcbLocalNI;
	private JComboBox<InetAddress> jcbLocalAddress;

	private JPanel jpnlDesign;

	private JLabel jlblColorSelf;
	private JColorChooser jccColorSelf;

	private JPanel jpnlUI;

	private JCheckBox jcbAlwaysOnTop;
	private JCheckBox jcbAutoOpenMsgIn;
	private JLabel jlblLocale;
	private JComboBox<Locale> jcbUserLocale;
	private JLabel jlblStateiconSize;
	private JComboBox<Integer> jcbStateiconSize;
	private JCheckBox jcbEnableSound;

	private JButton jbtnSave;
	private JButton jbtnRestore;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the preferences window
	 */
	public EzimPreferences()
	{
		super(EzimMain.getInstance(), EzimLang.Prefs, true);

		this.init();

		this.setIconImage(EzimImage.icoButtons[4].getImage());
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * initialize class members and GUI
	 */
	private void init()
	{
		this.initGUI();
		this.jcbLocalNI_StateChanged();
		this.loadCurConf();
	}

	/**
	 * initialize GUI components
	 */
	@SuppressWarnings("unchecked")
	private void initGUI()
	{
		SpinnerNumberModel mdlMcPort = new SpinnerNumberModel
		(
			5555
			, 1024
			, 65535
			, 1
		);

		SpinnerNumberModel mdlDtxPort = new SpinnerNumberModel
		(
			6666
			, 1024
			, 65535
			, 1
		);

		// C O M P O N E N T S ---------------------------------------------
		this.jlblMcGroup = new JLabel(EzimLang.McGroup);
		this.jtfdMcGroup = new JTextField();
		this.jtfdMcGroup.addFocusListener
		(
			new FocusListener()
			{
				public void focusGained(FocusEvent evtTmp)
				{
				}

				public void focusLost(FocusEvent evtTmp)
				{
					EzimPreferences.this.jtfdMcGroup_FocusLost();
				}
			}
		);
		this.jbtnRestoreToDefault = new JButton
		(
			new ImageIcon
			(
				EzimImage.icoButtons[7].getImage().getScaledInstance
				(
					16, 16
					, Image.SCALE_SMOOTH
				)
			)
		);
		this.jbtnRestoreToDefault.setPreferredSize(new Dimension(16, 16));
		this.jbtnRestoreToDefault.setToolTipText(EzimLang.RestoreToDefault);
		this.jbtnRestoreToDefault.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evntTmp)
				{
					EzimPreferences.this
						.jbtnRestoreToDefault_ActionPerformed();
				}
			}
		);
		this.jlblMcPort = new JLabel(EzimLang.McPort);
		this.jspnMcPort = new JSpinner(mdlMcPort);
		this.jspnMcPort.setEditor
		(
			new JSpinner.NumberEditor(this.jspnMcPort, "#")
		);
		this.jlblMcPort.setLabelFor(this.jspnMcPort);
		this.jlblDtxPort = new JLabel(EzimLang.DtxPort);
		this.jspnDtxPort = new JSpinner(mdlDtxPort);
		this.jspnDtxPort.setEditor
		(
			new JSpinner.NumberEditor(this.jspnDtxPort, "#")
		);
		this.jlblDtxPort.setLabelFor(this.jspnDtxPort);
		this.jcbLocalNI = new JComboBox<NetworkInterface>
		(
			new Vector<NetworkInterface>(Ezim.getLocalNIs())
		);
		this.jcbLocalNI.setRenderer
		(
			new EzimLocalNIListRenderer()
		);
		this.jcbLocalNI.setEditable(false);
		this.jcbLocalNI.addItemListener
		(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent evtTmp)
				{
					if (ItemEvent.SELECTED == evtTmp.getStateChange())
						EzimPreferences.this.jcbLocalNI_StateChanged();
				}
			}
		);
		this.jcbLocalAddress = new JComboBox<InetAddress>
		(
			new Vector<InetAddress>()
		);
		this.jcbLocalAddress.setRenderer
		(
			new EzimLocalAddressListRenderer()
		);
		this.jcbLocalAddress.setEditable(false);
		this.jlblLocalAddress = new JLabel(EzimLang.LocalAddress);
		this.jlblLocalAddress.setLabelFor(this.jcbLocalAddress);

		this.jlblColorSelf = new JLabel(EzimLang.ColorSelf);
		this.jccColorSelf = new JColorChooser();
		this.jlblColorSelf.setLabelFor(this.jccColorSelf);

		this.jcbAlwaysOnTop = new JCheckBox(EzimLang.AlwaysOnTop);
		this.jcbAutoOpenMsgIn = new JCheckBox(EzimLang.AutoOpenMsgIn);
		this.jlblLocale = new JLabel(EzimLang.Locale);
		this.jcbUserLocale = new JComboBox<Locale>(Ezim.locales);
		this.jcbUserLocale.setRenderer(new EzimLocaleListRenderer());
		this.jcbUserLocale.setEditable(false);
		this.jlblLocale.setLabelFor(this.jcbUserLocale);
		this.jlblStateiconSize = new JLabel(EzimLang.StateiconSize);
		this.jcbStateiconSize = new JComboBox<Integer>(Ezim.stateiconSizes);
		this.jcbStateiconSize.setEditable(false);
		this.jcbEnableSound = new JCheckBox(EzimLang.EnableSound);

		this.jpnlNetwork = new JPanel();
		this.jpnlDesign = new JPanel();
		this.jpnlUI = new JPanel();

		this.jtpBase = new JTabbedPane();

		this.jbtnSave = new JButton(EzimLang.Save);
		this.jbtnSave.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimPreferences.this.jbtnSave_ActionPerformed();
				}
			}
		);
		this.jbtnRestore = new JButton(EzimLang.Restore);
		this.jbtnRestore.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimPreferences.this
						.jbtnRestore_ActionPerformed();
				}
			}
		);

		this.jpnlBase = new JPanel();

		// N E T W O R K   P A N E L   L A Y O U T -------------------------
		GroupLayout glNw = new GroupLayout(this.jpnlNetwork);
		this.jpnlNetwork.setLayout(glNw);

		glNw.setAutoCreateGaps(true);
		glNw.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hNwGrp = glNw.createSequentialGroup();

		hNwGrp.addGroup
		(
			glNw.createParallelGroup()
				.addComponent
				(
					this.jlblMcGroup
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jlblMcPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jlblDtxPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jlblLocalAddress
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		hNwGrp.addGroup
		(
			glNw.createParallelGroup()
				.addComponent
				(
					this.jtfdMcGroup
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
				.addComponent
				(
					this.jspnMcPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnDtxPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addGroup
				(
					glNw.createSequentialGroup()
						.addComponent
						(
							this.jcbLocalNI
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jcbLocalAddress
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
		);

		hNwGrp.addGroup
		(
			glNw.createParallelGroup()
				.addComponent
				(
					this.jbtnRestoreToDefault
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glNw.setHorizontalGroup(hNwGrp);

		GroupLayout.SequentialGroup vNwGrp = glNw.createSequentialGroup();

		vNwGrp.addGroup
		(
			glNw.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblMcGroup
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jtfdMcGroup
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnRestoreToDefault
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vNwGrp.addGroup
		(
			glNw.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblMcPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnMcPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vNwGrp.addGroup
		(
			glNw.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblDtxPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnDtxPort
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vNwGrp.addGroup
		(
			glNw.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblLocalAddress
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jcbLocalNI
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jcbLocalAddress
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glNw.setVerticalGroup(vNwGrp);

		// D E S I G N   P A N E L   L A Y O U T ---------------------------
		GroupLayout glDs = new GroupLayout(this.jpnlDesign);
		this.jpnlDesign.setLayout(glDs);

		glDs.setAutoCreateGaps(true);
		glDs.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hDsGrp = glDs.createSequentialGroup();

		hDsGrp.addGroup
		(
			glDs.createParallelGroup()
				.addComponent
				(
					this.jlblColorSelf
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jccColorSelf
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glDs.setHorizontalGroup(hDsGrp);

		GroupLayout.SequentialGroup vDsGrp = glDs.createSequentialGroup();

		vDsGrp.addComponent
		(
			this.jlblColorSelf
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);
		vDsGrp.addComponent
		(
			this.jccColorSelf
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		glDs.setVerticalGroup(vDsGrp);

		// I N T E R F A C E   P A N E   L A Y O U T -----------------------
		GroupLayout glUI = new GroupLayout(this.jpnlUI);
		this.jpnlUI.setLayout(glUI);

		glUI.setAutoCreateGaps(true);
		glUI.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hUIGrp = glUI.createSequentialGroup();

		hUIGrp.addGroup
		(
			glUI.createParallelGroup()
				.addComponent
				(
					this.jcbAlwaysOnTop
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jcbAutoOpenMsgIn
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addGroup
				(
					glUI.createSequentialGroup()
						.addComponent
						(
							this.jlblLocale
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jcbUserLocale
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
				.addGroup
				(
					glUI.createSequentialGroup()
						.addComponent
						(
							this.jlblStateiconSize
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jcbStateiconSize
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
				.addComponent
				(
					this.jcbEnableSound
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glUI.setHorizontalGroup(hUIGrp);

		GroupLayout.SequentialGroup vUIGrp = glUI.createSequentialGroup();

		vUIGrp.addGroup
		(
			glUI.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jcbAlwaysOnTop
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vUIGrp.addGroup
		(
			glUI.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jcbAutoOpenMsgIn
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vUIGrp.addGroup
		(
			glUI.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblLocale
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jcbUserLocale
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vUIGrp.addGroup
		(
			glUI.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblStateiconSize
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jcbStateiconSize
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vUIGrp.addGroup
		(
			glUI.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jcbEnableSound
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glUI.setVerticalGroup(vUIGrp);

		// T A B B E D   P A N E   L A Y O U T -----------------------------
		this.jtpBase.addTab(EzimLang.UI, jpnlUI);
		this.jtpBase.addTab(EzimLang.Design, jpnlDesign);
		this.jtpBase.addTab(EzimLang.Network, jpnlNetwork);

		// O V E R A L L   L A Y O U T -------------------------------------
		GroupLayout glBase = new GroupLayout(this.jpnlBase);
		this.jpnlBase.setLayout(glBase);

		GroupLayout.SequentialGroup hGrp = glBase.createSequentialGroup();

		hGrp.addGroup
		(
			glBase.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent
				(
					this.jtpBase
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
				.addGroup
				(
					glBase.createSequentialGroup()
						.addComponent
						(
							this.jbtnSave
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnRestore
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
				)
		);

		glBase.setHorizontalGroup(hGrp);

		GroupLayout.SequentialGroup vGrp = glBase.createSequentialGroup();

		vGrp.addGroup
		(
			glBase.createParallelGroup()
				.addComponent
				(
					this.jtpBase
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup()
				.addComponent
				(
					this.jbtnSave
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnRestore
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glBase.setVerticalGroup(vGrp);

		this.add(jpnlBase);
	}

	/**
	 * load settings from current configuration
	 */
	private void loadCurConf()
	{
		int iCnt = 0, iLen = 0;

		// C U R R E N T   S E T T I N G S ---------------------------------
		EzimConf ecTmp = EzimConf.getInstance();

		// multicast group IP
		String strMcGroup = ecTmp.settings.getProperty
		(
			EzimConf.ezimMcGroup
		);

		this.jtfdMcGroup.setText(strMcGroup);

		// multicast group port
		String strMcPort = ecTmp.settings.getProperty
		(
			EzimConf.ezimMcPort
		);

		this.jspnMcPort.setValue(Integer.valueOf(strMcPort));

		// direct transmission port
		String strDtxPort = ecTmp.settings.getProperty
		(
			EzimConf.ezimDtxPort
		);

		this.jspnDtxPort.setValue(Integer.valueOf(strDtxPort));

		// local network interface
		String strLocalNI = ecTmp.settings.getProperty
		(
			EzimConf.ezimLocalni
		);
		iLen = this.jcbLocalNI.getItemCount();
		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			if
			(
				this.jcbLocalNI.getItemAt(iCnt).getName().equals(strLocalNI)
			)
			{
				this.jcbLocalNI.setSelectedIndex(iCnt);
				break;
			}
		}

		// local address
		String strLocalAddress = ecTmp.settings.getProperty
		(
			EzimConf.ezimLocaladdress
		);

		iLen = this.jcbLocalAddress.getItemCount();
		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			if
			(
				this.jcbLocalAddress.getItemAt(iCnt).toString()
					.endsWith("/" + strLocalAddress)
				|| this.jcbLocalAddress.getItemAt(iCnt).toString()
					.indexOf("/" + strLocalAddress + "%") > -1
			)
			{
				this.jcbLocalAddress.setSelectedIndex(iCnt);
				break;
			}
		}

		// self color
		String strColorSelf = ecTmp.settings.getProperty
		(
			EzimConf.ezimColorSelf
		);

		this.jccColorSelf.setColor(Integer.parseInt(strColorSelf, 16));

		// always on top
		String strAlwaysOnTop = ecTmp.settings.getProperty
		(
			EzimConf.ezimmainAlwaysontop
		);

		this.jcbAlwaysOnTop.setSelected
		(
			Boolean.parseBoolean(strAlwaysOnTop)
		);

		// auto open incoming message
		String strAutoOpenMsgIn = ecTmp.settings.getProperty
		(
			EzimConf.ezimmsginAutoopen
		);

		this.jcbAutoOpenMsgIn.setSelected
		(
			Boolean.parseBoolean(strAutoOpenMsgIn)
		);

		// locale
		String strUserLocale = ecTmp.settings.getProperty
		(
			EzimConf.ezimUserLocale
		);

		iLen = this.jcbUserLocale.getItemCount();
		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			if
			(
				strUserLocale.equals
				(
					((Locale) this.jcbUserLocale.getItemAt(iCnt)).toString()
				)
			)
			{
				this.jcbUserLocale.setSelectedIndex(iCnt);
				break;
			}
		}

		// state icon size
		int iStateiconSize = Integer.parseInt
		(
			ecTmp.settings.getProperty
			(
				EzimConf.ezimStateiconSize
			)
		);
		iLen = this.jcbStateiconSize.getItemCount();
		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			if
			(
				iStateiconSize ==
				(
					(Integer) this.jcbStateiconSize.getItemAt(iCnt)
				).intValue()
			)
			{
				this.jcbStateiconSize.setSelectedIndex(iCnt);
				break;
			}
		}

		// enable sound
		String strEnableSound = ecTmp.settings.getProperty
		(
			EzimConf.ezimsoundEnabled
		);

		this.jcbEnableSound.setSelected
		(
			Boolean.parseBoolean(strEnableSound)
		);
	}

	/**
	 * verify settings
	 */
	private boolean verifyCurConf()
	{
		boolean blnOut = true;
		String strHostAddress
			= ((InetAddress) this.jcbLocalAddress.getSelectedItem())
				.getHostAddress();
		String strMcGroup = this.jtfdMcGroup.getText();

		if
		(
			(
				strHostAddress.matches(Ezim.regexpIPv4)
				&& ! strMcGroup.matches(Ezim.regexpIPv4)
			)
			// should we comment these out for dual stack networks?
			|| (
				strHostAddress.matches(Ezim.regexpIPv6)
				&& ! strMcGroup.matches(Ezim.regexpIPv6)
			)
		)
		{
			EzimMain.showError
			(
				this
				, EzimLang.McGroupError
				, EzimLang.Prefs
			);

			blnOut = false;
		}
		else
		{
			try
			{
				InetAddress iaTmp = InetAddress.getByName(strMcGroup);

				if (! iaTmp.isMulticastAddress())
				{
					EzimMain.showError
					(
						this
						, EzimLang.McGroupError
						, EzimLang.Prefs
					);

					blnOut = false;
				}
			}
			catch(Exception e)
			{
				// this should NEVER happen
				blnOut = false;
			}
		}

		return blnOut;
	}

	/**
	 * save settings to current configuration
	 */
	private void saveCurConf()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		// multicast group IP
		ecTmp.settings.setProperty
		(
			EzimConf.ezimMcGroup
			, this.jtfdMcGroup.getText()
		);

		// multicast group port
		ecTmp.settings.setProperty
		(
			EzimConf.ezimMcPort
			, this.jspnMcPort.getValue().toString()
		);

		// direct transmission port
		ecTmp.settings.setProperty
		(
			EzimConf.ezimDtxPort
			, this.jspnDtxPort.getValue().toString()
		);

		// self color
		ecTmp.settings.setProperty
		(
			EzimConf.ezimColorSelf
			, Integer.toHexString
			(
				this.jccColorSelf.getColor().getRGB() & 0xffffff
			)
		);

		// local network interface
		ecTmp.settings.setProperty
		(
			EzimConf.ezimLocalni
			, ((NetworkInterface) this.jcbLocalNI.getSelectedItem())
				.getName()
		);

		// local address
		ecTmp.settings.setProperty
		(
			EzimConf.ezimLocaladdress
			, ((InetAddress) this.jcbLocalAddress.getSelectedItem())
				.getHostAddress()
		);

		// always on top
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmainAlwaysontop
			, Boolean.toString(this.jcbAlwaysOnTop.isSelected())
		);

		// auto open incoming message
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmsginAutoopen
			, Boolean.toString(this.jcbAutoOpenMsgIn.isSelected())
		);

		// locale
		ecTmp.settings.setProperty
		(
			EzimConf.ezimUserLocale
			, ((Locale) this.jcbUserLocale.getSelectedItem()).toString()
		);

		// state icon size
		ecTmp.settings.setProperty
		(
			EzimConf.ezimStateiconSize
			, this.jcbStateiconSize.getSelectedItem().toString()
		);

		// enable sound
		ecTmp.settings.setProperty
		(
			EzimConf.ezimsoundEnabled
			, Boolean.toString(this.jcbEnableSound.isSelected())
		);

		ecTmp.write();
	}

	// E V E N T   H A N D L E R -------------------------------------------
	/**
	 * "Multicast Group IP" textfield event handler
	 */
	private void jtfdMcGroup_FocusLost()
	{
		this.jtfdMcGroup.setText(this.jtfdMcGroup.getText().trim());
	}

	/**
	 * "Default Multicast Group IP" button event handler
	 */
	private void jbtnRestoreToDefault_ActionPerformed()
	{
		String strHostAddress = ((InetAddress) this.jcbLocalAddress
			.getSelectedItem()).getHostAddress();

		if (strHostAddress.matches(Ezim.regexpIPv4))
		{
			this.jtfdMcGroup.setText(Ezim.mcGroupIPv4);
		}
		else if (strHostAddress.matches(Ezim.regexpIPv6))
		{
			this.jtfdMcGroup.setText(Ezim.mcGroupIPv6);
		}
	}

	/**
	 * network interface combobox event handler
	 */
	private void jcbLocalNI_StateChanged()
	{
		this.jcbLocalAddress.removeAllItems();

		List<InetAddress> lAddrs = Ezim.getNIAddresses
		(
			(NetworkInterface) this.jcbLocalNI.getSelectedItem()
		);

		for(InetAddress iaTmp: lAddrs)
			this.jcbLocalAddress.addItem(iaTmp);
	}

	/**
	 * "Save" button event handler
	 */
	private void jbtnSave_ActionPerformed()
	{
		if (this.verifyCurConf())
		{
			this.saveCurConf();

			JOptionPane.showMessageDialog
			(
				this
				, EzimLang.RestartNeeded
				, EzimLang.Prefs
				, JOptionPane.INFORMATION_MESSAGE
			);

			this.dispose();
		}
	}

	/**
	 * "Restore" button event handler
	 */
	private void jbtnRestore_ActionPerformed()
	{
		this.loadCurConf();
	}
}
