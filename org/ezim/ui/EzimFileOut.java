/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2010  Chun-Kwong Wong
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
import java.util.Date;
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
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;

public class EzimFileOut
	extends JFrame
{
	private EzimContact ec;
	private Socket sck;
	private File file;
	private String id;

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
		this.id = Long.toString(new Date().getTime());
		this.ec = ecIn;
		this.file = fIn;

		this.initGUI();

		this.setIconImage(EzimImage.icoButtons[1].getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingFile);
		this.pack();
		this.setMinimumSize(this.getSize());

		this.loadConf();

		this.setVisible(true);

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

		return;
	}

	// E V E N T   H A N D L E R -------------------------------------------
	/**
	 * unregister file from the outgoing file queue, save window position
	 * and size, then dispose itself
	 */
	private void unregSaveDispose()
	{
		try
		{
			if (this.sck != null && ! this.sck.isClosed())
				this.sck.close();
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		EzimFtxList.getInstance().remove(this.id);
		this.saveConf();
		this.dispose();

		return;
	}

	/**
	 * "Close" button event handler
	 */
	private void jbtnClose_ActionPerformed()
	{
		this.unregSaveDispose();
		return;
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
	 * set socket associated with this user interface
	 * @param sckIn socket to be applied
	 */
	public void setSocket(Socket sckIn)
	{
		this.sck = sckIn;
		return;
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
}
