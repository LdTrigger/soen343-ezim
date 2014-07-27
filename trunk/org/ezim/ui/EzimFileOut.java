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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;
import java.util.UUID;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimFileRequester;
import org.ezim.core.EzimFtxList;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimThreadPool;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMain;

public class EzimFileOut
	extends JFrame
{
	private EzimContact ec;
	private File file;
	private String id;
	private Socket socket;
	private long filesize;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JLabel jlblFName;
	private JTextField jtfdFName;
	private JLabel jlblSize;
	private JTextField jtfdSize;
	private JProgressBar jpbProgress;
	private JLabel jlblSysMsg;
	private JButton jbtnClose;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the outgoing file GUI
	 * @param ecIn contact of the file recipient
	 */
	public EzimFileOut(EzimContact ecIn)
	{
		this.init(ecIn, (File) null);
	}

	/**
	 * construct an instance of the outgoing file GUI
	 * @param ecIn contact of the file recipient
	 * @param fIn the associated physical file
	 */
	public EzimFileOut(EzimContact ecIn, File fIn)
	{
		this.init(ecIn, fIn);

		EzimFtxList.getInstance().put(this.id, this);

		EzimThreadPool.getInstance().execute
		(
			new EzimFileRequester
			(
				ecIn.getAddress()
				, ecIn.getPort()
				, this.id
				, this
			)
		);
	}

	/**
	 * initialize class members and GUI
	 * @param ecIn contact of the file recipient
	 * @param fIn the associated physical file
	 */
	private void init(EzimContact ecIn, File fIn)
	{
		this.id = UUID.randomUUID().toString();
		while(EzimFtxList.getInstance().get(this.id) != null)
			this.id = UUID.randomUUID().toString();
		this.ec = ecIn;
		this.file = fIn;
		this.socket = null;

		this.initGUI();

		this.setIconImage(EzimImage.icoButtons[1].getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingFile);
		this.pack();
		this.setMinimumSize(this.getSize());

		this.loadConf();

		this.setVisible(true);
	}

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * load window position and size from configuration settings
	 */
	private void loadConf()
	{
		this.setLocation
		(
			EzimConf.UI_FILEOUT_LOCATION_X
			, EzimConf.UI_FILEOUT_LOCATION_Y
		);
		this.setSize
		(
			EzimConf.UI_FILEOUT_SIZE_W
			, EzimConf.UI_FILEOUT_SIZE_H
		);
	}

	/**
	 * save window position and size to configuration settings
	 */
	private void saveConf()
	{
		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		EzimConf.UI_FILEOUT_LOCATION_X = (int) ptTmp.getX();
		EzimConf.UI_FILEOUT_LOCATION_Y = (int) ptTmp.getY();
		Dimension dmTmp = this.getSize();
		EzimConf.UI_FILEOUT_SIZE_W = (int) dmTmp.getWidth();
		EzimConf.UI_FILEOUT_SIZE_H = (int) dmTmp.getHeight();
	}

	/**
	 * initialize GUI components
	 */
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

		this.jlblSize = new JLabel(EzimLang.Size);

		this.jtfdSize = new JTextField();
		this.jtfdSize.setEditable(false);

		this.jpbProgress = new JProgressBar();
		this.jpbProgress.setStringPainted(true);

		this.jlblSysMsg = new JLabel(EzimLang.WaitingForResponse);

		this.jbtnClose = new JButton(EzimLang.Cancel);
		this.jbtnClose.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimFileOut.this.jbtnClose_ActionPerformed();
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

		vGrp.addComponent
		(
			this.jbtnClose
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		vGrp.addGap(10);

		glBase.setVerticalGroup(vGrp);
	}

	// E V E N T   H A N D L E R -------------------------------------------
	/**
	 * "Close" button event handler
	 */
	private void jbtnClose_ActionPerformed()
	{
		if (this.socket != null && ! this.socket.isClosed())
		{
			try
			{
				this.socket.close();
			}
			catch(Exception e)
			{
				EzimMain.showError(e.getMessage());
				EzimLogger.getInstance().warning(e.getMessage(), e);
			}
		}

		this.saveConf();
		this.unregDispose();
	}

	// O P E R A T I O N ---------------------------------------------------
	/**
	 * get ID of the outgoing file
	 * @return ID of the outgoing file
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
	 * set socket to associate with this user interface
	 * @param sckIn socket to associate with
	 */
	public void setSocket(Socket sckIn)
	{
		if (this.socket == null) this.socket = sckIn;
	}

	/**
	 * set text of the system message label
	 * @param strIn text to be set
	 */
	public void setSysMsg(String strIn)
	{
		this.jlblSysMsg.setText(strIn);
	}

	/**
	 * set value of the file size textfield
	 * @param lIn value to be set
	 */
	public void setSize(long lIn)
	{
		this.filesize = lIn;
		this.jtfdSize.setText(Long.toString(this.filesize));
		this.jpbProgress.setMaximum(100);
	}

	/**
	 * set value of the progress bar
	 * @param lIn value to be set
	 */
	public void setProgressed(long lIn)
	{
		if (0 == this.filesize)
			this.jpbProgress.setValue(100);
		else
			this.jpbProgress.setValue((int) ((lIn * 100) / this.filesize));
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
	}

	/**
	 * unregister file from the outgoing file queue then dispose itself
	 */
	public void unregDispose()
	{
		EzimFtxList.getInstance().remove(this.id);
		this.dispose();
	}
}
