/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2011  Chun-Kwong Wong
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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimFileResponder;
import org.ezim.core.EzimFrxList;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimSound;
import org.ezim.core.EzimThreadPool;

public class EzimFileIn
	extends JFrame
{
	private EzimContact ec;
	private File file;
	private String id;
	private String remoteFName;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JLabel jlblFName;
	private JTextField jtfdFName;
	private JLabel jlblSize;
	private JTextField jtfdSize;
	private JProgressBar jpbProgress;
	private JLabel jlblSysMsg;
	private JButton jbtnYes;
	private JButton jbtnNo;
	private JButton jbtnClose;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the incoming file GUI
	 * @param ecIn contact of the file sender
	 * @param strId ID of the file in the incoming file queue
	 */
	public EzimFileIn(EzimContact ecIn, String strId)
	{
		this.init(ecIn, strId, (String) null);
	}

	/**
	 * construct an instance of the incoming file GUI
	 * @param ecIn contact of the file sender
	 * @param strId ID of the file in the incoming file queue
	 * @param strRemoteFName file name on the remote (sender's) machine
	 */
	public EzimFileIn
	(
		EzimContact ecIn
		, String strId
		, String strRemoteFName
	)
	{
		this.init(ecIn, strId, strRemoteFName);
	}

	/**
	 * initialize class members and GUI
	 * @param ecIn contact of the file sender
	 * @param strId ID of the file in the incoming file queue
	 * @param strRemoteFName file name on the remote (sender's) machine
	 */
	private void init
	(
		EzimContact ecIn
		, String strId
		, String strRemoteFName
	)
	{
		this.ec = ecIn;
		this.id = strId;
		this.remoteFName = strRemoteFName;

		this.initGUI();

		this.setIconImage(EzimImage.icoButtons[6].getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(EzimLang.IncomingFile);
		this.pack();
		this.setMinimumSize(this.getSize());

		this.loadConf();

		this.setVisible(true);

		if (EzimSound.getInstance() != null)
			EzimSound.getInstance().playFileIn();

		return;
	}

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * load window position and size from configuration settings
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
					EzimConf.ezimfileinLocationX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileinLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileinSizeW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimfileinSizeH
				)
			)
		);

		return;
	}

	/**
	 * save window position and size to configuration settings
	 */
	private void saveConf()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileinLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileinLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileinSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimfileinSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);

		return;
	}

	/**
	 * initialize GUI components
	 */
	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.From);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEditable(false);

		this.jlblFName = new JLabel(EzimLang.Filename);

		this.jtfdFName = new JTextField();
		if (this.remoteFName != null) this.jtfdFName.setText(this.remoteFName);
		this.jtfdFName.setEditable(false);

		this.jlblSize = new JLabel(EzimLang.Size);

		this.jtfdSize = new JTextField();
		this.jtfdSize.setEditable(false);

		this.jpbProgress = new JProgressBar();
		this.jpbProgress.setStringPainted(true);

		this.jlblSysMsg = new JLabel(EzimLang.ReceiveFile);

		this.jbtnYes = new JButton(EzimLang.Yes);
		this.jbtnYes.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimFileIn.this.jbtnYes_ActionPerformed();
					return;
				}
			}
		);

		this.jbtnNo = new JButton(EzimLang.No);
		this.jbtnNo.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimFileIn.this.jbtnNo_ActionPerformed();
					return;
				}
			}
		);

		this.jbtnClose = new JButton(EzimLang.Cancel);
		this.jbtnClose.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimFileIn.this.jbtnClose_ActionPerformed();
					return;
				}
			}
		);
		this.jbtnClose.setVisible(false);

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
								.addComponent
								(
									this.jlblSize
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
								.addComponent
								(
									this.jtfdSize
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
				.addGroup
				(
					glBase.createSequentialGroup()
						.addComponent
						(
							this.jbtnYes
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnNo
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
							, GroupLayout.PREFERRED_SIZE
						)
						.addComponent
						(
							this.jbtnClose
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

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblSize
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jtfdSize
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

		vGrp.addGap(10, 10, Short.MAX_VALUE);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jbtnYes
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnNo
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnClose
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vGrp.addGap(10);

		glBase.setVerticalGroup(vGrp);

		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
	/**
	 * handle (accept or reject) file transmission
	 * @param blnRes a true value indicates the transmission is accepted,
	 *   while a false value indicates the transmission is rejected
	 */
	private void handleTransmission(boolean blnRes)
	{
		this.setSysMsg(EzimLang.WaitingForResponse);
		this.jbtnYes.setVisible(false);
		this.jbtnNo.setVisible(false);
		this.jbtnClose.setVisible(true);

		EzimFrxList.getInstance().put(this.id, this);

		// let the user choose where to save the incoming file
		if (blnRes)
		{
			EzimConf econf = EzimConf.getInstance();
			boolean blnRetry = true;

			while(blnRetry)
			{
				blnRetry = false;

				JFileChooser jfcTmp = new JFileChooser
				(
					econf.settings.getProperty
					(
						EzimConf.ezimfileinDirectory
					)
				);

				jfcTmp.setSelectedFile
				(
					new File
					(
						jfcTmp.getCurrentDirectory().getAbsolutePath()
						, this.remoteFName
					)
				);

				int iJfcRes = jfcTmp.showSaveDialog(this);

				if (iJfcRes == JFileChooser.APPROVE_OPTION)
				{
					econf.settings.setProperty
					(
						EzimConf.ezimfileinDirectory
						, jfcTmp.getCurrentDirectory().getAbsolutePath()
					);

					this.file = jfcTmp.getSelectedFile();

					if (this.file.exists())
					{
						int iDlgRes = JOptionPane.showConfirmDialog
						(
							null
							, EzimLang.OverwriteExistingFile
						);

						switch(iDlgRes)
						{
						case JOptionPane.NO_OPTION:
						default:
							this.file = null;
							blnRetry = true;
							break;
						case JOptionPane.CANCEL_OPTION:
							this.file = null;
						case JOptionPane.YES_OPTION:
						}
					}
				}
			}
		}

		boolean blnFinalRes = (blnRes && this.file != null);

		// respond back to the request
		EzimThreadPool.getInstance().execute
		(
			new EzimFileResponder
			(
				this.ec.getAddress()
				, this.ec.getPort()
				, this.id
				, blnFinalRes
			)
		);

		// close this incoming file window if the user has declined the
		// transmission or has not chosen where to save the file
		if (! blnFinalRes)
		{
			this.saveConf();
			this.unregDispose();
		}

		return;
	}

	/**
	 * "Yes" button event handler
	 */
	private void jbtnYes_ActionPerformed()
	{
		this.handleTransmission(true);
		return;
	}

	/**
	 * "No" button event handler
	 */
	private void jbtnNo_ActionPerformed()
	{
		this.handleTransmission(false);
		return;
	}

	/**
	 * "Close" button event handler
	 */
	private void jbtnClose_ActionPerformed()
	{
		this.saveConf();
		this.unregDispose();
		return;
	}

	// O P E R A T I O N ---------------------------------------------------
	/**
	 * get ID of the incoming file
	 * @return ID of the incoming file
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * get file associated with this user interface
	 * @return the associated physical file
	 */
	public File getFile()
	{
		return this.file;
	}

	/**
	 * set text of the system message label
	 * @param strIn text to be set
	 */
	public void setSysMsg(String strIn)
	{
		this.jlblSysMsg.setText(strIn);
		return;
	}

	/**
	 * set value of the file size textfield
	 * @param iIn value to be set
	 */
	public void setSize(int iIn)
	{
		this.jtfdSize.setText(Integer.toString(iIn));
		this.jpbProgress.setMaximum(iIn);
		return;
	}

	/**
	 * set value of the progress bar
	 * @param iIn value to be set
	 */
	public void setProgressed(int iIn)
	{
		this.jpbProgress.setValue(iIn);
		return;
	}

	/**
	 * change text in the system message label and close button to indicate
	 * the progress has ended
	 * @param strIn text to be set in the system message label
	 */
	public void endProgress(String strIn)
	{
		this.setSysMsg(strIn);
		this.jbtnClose.setText(EzimLang.Close);

		return;
	}

	/**
	 * unregister file from the incoming file queue, then dispose itself
	 */
	public void unregDispose()
	{
		EzimFrxList.getInstance().remove(this.id);
		this.dispose();

		return;
	}
}
