/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2008  Chun-Kwong Wong
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import org.ezim.core.EzimConf;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;

public class EzimPreferences
	extends JDialog
{
	private JPanel jpnlBase;

	private JTabbedPane jtpBase;

	private JPanel jpnlNetwork;

	private JLabel jlblMcGroup;
	private JSpinner jspnMcGroup1;
	private JSpinner jspnMcGroup2;
	private JSpinner jspnMcGroup3;
	private JSpinner jspnMcGroup4;

	private JLabel jlblMcPort;
	private JSpinner jspnMcPort;

	private JLabel jlblDtxPort;
	private JSpinner jspnDtxPort;

	private JPanel jpnlDesign;

	private JLabel jlblColorSelf;
	private JColorChooser jccColorSelf;

	private JPanel jpnlUI;

	private JCheckBox jcbAlwaysOnTop;
	private JCheckBox jcbAutoOpenMsgIn;

	private JButton jbtnSave;
	private JButton jbtnRestore;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimPreferences(EzimMain emIn)
	{
		super(emIn, EzimLang.Prefs, true);

		this.init();

		this.setIconImage(EzimImage.icoBtnPrefs.getImage());
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void init()
	{
		this.initGUI();
		this.loadCurConf();

		return;
	}

	/**
	 * initialize all GUI components
	 */
	public void initGUI()
	{
		SpinnerNumberModel mdlMcGrp1 = new SpinnerNumberModel
		(
			229
			, 224
			, 239
			, 1
		);
		SpinnerNumberModel mdlMcGrp2 = new SpinnerNumberModel
		(
			0
			, 0
			, 255
			, 1
		);
		SpinnerNumberModel mdlMcGrp3 = new SpinnerNumberModel
		(
			0
			, 0
			, 255
			, 1
		);
		SpinnerNumberModel mdlMcGrp4 = new SpinnerNumberModel
		(
			1
			, 0
			, 255
			, 1
		);

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
		this.jspnMcGroup1 = new JSpinner(mdlMcGrp1);
		this.jspnMcGroup1.setEditor
		(
			new JSpinner.NumberEditor(this.jspnMcGroup1, "#")
		);
		this.jspnMcGroup2 = new JSpinner(mdlMcGrp2);
		this.jspnMcGroup2.setEditor
		(
			new JSpinner.NumberEditor(this.jspnMcGroup2, "#")
		);
		this.jspnMcGroup3 = new JSpinner(mdlMcGrp3);
		this.jspnMcGroup3.setEditor
		(
			new JSpinner.NumberEditor(this.jspnMcGroup3, "#")
		);
		this.jspnMcGroup4 = new JSpinner(mdlMcGrp4);
		this.jspnMcGroup4.setEditor
		(
			new JSpinner.NumberEditor(this.jspnMcGroup4, "#")
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

		this.jlblColorSelf = new JLabel(EzimLang.ColorSelf);
		this.jccColorSelf = new JColorChooser();

		this.jcbAlwaysOnTop = new JCheckBox(EzimLang.AlwaysOnTop);
		this.jcbAutoOpenMsgIn = new JCheckBox(EzimLang.AutoOpenMsgIn);

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
					return;
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

					return;
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
		);

		hNwGrp.addGroup
		(
			glNw.createParallelGroup()
				.addGroup
				(
					glNw.createSequentialGroup()
						.addComponent
						(
							this.jspnMcGroup1
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jspnMcGroup2
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jspnMcGroup3
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jspnMcGroup4
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
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
					this.jspnMcGroup1
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnMcGroup2
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnMcGroup3
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jspnMcGroup4
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
				.addGroup
				(
					glDs.createSequentialGroup()
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
				)
		);

		glDs.setHorizontalGroup(hDsGrp);

		GroupLayout.SequentialGroup vDsGrp = glDs.createSequentialGroup();

		vDsGrp.addGroup
		(
			glDs.createParallelGroup(Alignment.BASELINE)
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

		return;
	}

	/**
	 * load settings from current configuration
	 */
	private void loadCurConf()
	{
		// C U R R E N T   S E T T I N G S ---------------------------------
		EzimConf ecTmp = EzimConf.getInstance();

		// multicast group IP
		String strMcGroup = ecTmp.settings.getProperty
		(
			EzimConf.ezimMcGroup
		);

		String[] arrMcGroup = strMcGroup.split("\\.");

		this.jspnMcGroup1.setValue(Integer.valueOf(arrMcGroup[0]));
		this.jspnMcGroup2.setValue(Integer.valueOf(arrMcGroup[1]));
		this.jspnMcGroup3.setValue(Integer.valueOf(arrMcGroup[2]));
		this.jspnMcGroup4.setValue(Integer.valueOf(arrMcGroup[3]));

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

		return;
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
			, this.jspnMcGroup1.getValue().toString()
				+ "." + this.jspnMcGroup2.getValue().toString()
				+ "." + this.jspnMcGroup3.getValue().toString()
				+ "." + this.jspnMcGroup4.getValue().toString()
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

		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
	private void jbtnSave_ActionPerformed()
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

		return;
	}

	private void jbtnRestore_ActionPerformed()
	{
		this.loadCurConf();
		return;
	}
}
