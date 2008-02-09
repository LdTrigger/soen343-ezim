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
import java.io.File;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimFtxList;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimFileRequester;

public class EzimFileOut
	extends JFrame
{
	private EzimContact ec;
	private File file;
	private String id;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JLabel jlblFName;
	private JTextField jtfdFName;
	private JProgressBar jpbProgress;
	private JLabel jlblSysMsg;
	private JButton jbtnClose;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimFileOut(EzimContact ecIn)
	{
		init(ecIn, (File) null);
	}

	public EzimFileOut(EzimContact ecIn, File fIn)
	{
		init(ecIn, fIn);

		EzimFtxList.getInstance().put(this.id, this);
		new EzimFileRequester(ecIn.getIp(), this.id, this.file).run();
	}

	private void init(EzimContact ecIn, File fIn)
	{
		this.id = Long.toString(new Date().getTime());
		this.ec = ecIn;
		this.file = fIn;

		this.loadConf();
		this.initGUI();

		this.setIconImage(EzimImage.icoBtnFtx.getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingFile);
		this.setMinimumSize(new Dimension(320, 180));
		this.setVisible(true);

		return;
	}

	// P R I V A T E   M E T H O D S ---------------------------------------
	private void loadConf()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		this.setLocation
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileoutLocationX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileoutLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileoutSizeW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileoutSizeH
				)
			)
		);

		return;
	}

	private void saveConf()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileoutLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileoutLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileoutSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileoutSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);

		return;
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.To);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEditable(false);

		this.jlblFName = new JLabel(EzimLang.Filename);

		this.jtfdFName = new JTextField();
		if (this.file != null) this.jtfdFName.setText(this.file.getName());
		this.jtfdFName.setEditable(false);

		this.jpbProgress = new JProgressBar();

		this.jlblSysMsg = new JLabel(EzimLang.WaitingForResponse);

		this.jbtnClose = new JButton(EzimLang.Close);
		this.jbtnClose.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					jbtnClose_ActionPerformed(evtTmp);
					return;
				}
			}
		);
		this.jbtnClose.setEnabled(false);

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
					glBase.createParallelGroup()
					.addComponent
					(
						this.jlblName
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
					)
					.addComponent
					(
						this.jlblFName
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
						, GroupLayout.PREFERRED_SIZE
					)
				)
				.addGroup
				(
					glBase.createParallelGroup()
					.addComponent
					(
						this.jtfdName
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Short.MAX_VALUE
					)
					.addComponent
					(
						this.jtfdFName
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Short.MAX_VALUE
					)
				)
			)
			.addComponent
			(
				this.jpbProgress
				, GroupLayout.DEFAULT_SIZE
				, GroupLayout.PREFERRED_SIZE
				, Short.MAX_VALUE
			)
			.addComponent
			(
				this.jlblSysMsg
				, GroupLayout.DEFAULT_SIZE
				, GroupLayout.PREFERRED_SIZE
				, Short.MAX_VALUE
			)
			.addComponent
			(
				this.jbtnClose
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
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
			.addComponent
			(
				this.jlblFName
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
			)
			.addComponent
			(
				this.jtfdFName
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
			)
		);

		vGrp.addComponent
		(
			this.jpbProgress
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		vGrp.addComponent
		(
			this.jlblSysMsg
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		vGrp.addContainerGap
		(
			GroupLayout.DEFAULT_SIZE
			, Short.MAX_VALUE
		);

		vGrp.addComponent
		(
			this.jbtnClose
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		glBase.setVerticalGroup(vGrp);

		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
	private void unregSaveDispose()
	{
		EzimFtxList.getInstance().remove(this.id);
		this.saveConf();
		this.dispose();

		return;
	}

	private void jbtnClose_ActionPerformed(ActionEvent evt)
	{
		this.unregSaveDispose();
		return;
	}

	// O P E R A T I O N ---------------------------------------------------
	public String getId()
	{
		return this.id;
	}

	public File getFile()
	{
		return this.file;
	}

	public void setSysMsg(String strIn)
	{
		this.jlblSysMsg.setText(strIn);
		return;
	}

	public void setSize(int iIn)
	{
		this.jpbProgress.setMaximum(iIn);
		return;
	}

	public void setProgressed(int iIn)
	{
		this.jpbProgress.setValue(iIn);
		return;
	}

	public void finishProgress()
	{
		this.setSysMsg(EzimLang.Done);
		this.jbtnClose.setEnabled(true);

		return;
	}

	public void setCloseButtonEnabled(boolean blnIn)
	{
		this.jbtnClose.setEnabled(blnIn);
		return;
	}
}
