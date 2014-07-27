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
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimPlainDocument;
import org.ezim.core.EzimSound;
import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimMsgOut;
import org.ezim.ui.EzimTextArea;
import org.ezim.ui.EzimTextField;

public class EzimMsgIn
	extends JFrame
	implements WindowListener
{
	private EzimContact ec;

	private JPanel jpnlBase;
	private JLabel jlblRcvAt;
	private JTextField jtfdRcvAt;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JLabel jlblSbj;
	private EzimTextField etfSbj;
	private EzimTextArea etaMsg;
	private JScrollPane jspMsg;
	private JLabel jlblOpen;
	private JButton jbtnReply;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the incoming message window
	 * @param ecIn contact of the message sender
	 * @param strSbj subject line
	 * @param strIn message body
	 */
	public EzimMsgIn(EzimContact ecIn, String strSbj, String strIn)
	{
		this.ec = ecIn;

		this.initGUI();

		this.setIconImage(EzimImage.icoButtons[0].getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.IncomingMessage);

		this.pack();
		this.setMinimumSize(this.getSize());
		this.loadConf();

		if (strSbj != null && strSbj.length() > 0)
			this.etfSbj.setText(strSbj);

		this.jtfdRcvAt.setText
		(
			DateFormat.getDateTimeInstance().format(new Date())
		);

		if (strIn != null && strIn.length() > 0)
			this.etaMsg.setText(strIn);

		this.setVisible(true);
		this.toFront();

		if
		(
			EzimMain.getInstance().autoOpenMsgIn
		)
		{
			jlblOpen_MouseClicked();
		}

		if (EzimSound.getInstance() != null)
			EzimSound.getInstance().playMsgIn();
	}

	/**
	 * load window position and size from configuration settings
	 */
	private void loadConf()
	{
		this.setLocation
		(
			EzimConf.UI_MSGIN_LOCATION_X
			, EzimConf.UI_MSGIN_LOCATION_Y
		);
		this.setSize
		(
			EzimConf.UI_MSGIN_SIZE_W
			, EzimConf.UI_MSGIN_SIZE_H
		);
	}

	/**
	 * save window position and size to configuration settings
	 */
	private void saveConf()
	{
		// save window location and size
		Point ptTmp = this.getLocationOnScreen();
		EzimConf.UI_MSGIN_LOCATION_X = (int) ptTmp.getX();
		EzimConf.UI_MSGIN_LOCATION_Y = (int) ptTmp.getY();
		Dimension dmTmp = this.getSize();
		EzimConf.UI_MSGIN_SIZE_W = (int) dmTmp.getWidth();
		EzimConf.UI_MSGIN_SIZE_H = (int) dmTmp.getHeight();
	}

	/**
	 * initialize GUI components
	 */
	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblRcvAt = new JLabel(EzimLang.ReceivedAt);

		this.jtfdRcvAt = new JTextField();
		this.jtfdRcvAt.setEditable(false);

		this.jlblName = new JLabel(EzimLang.From);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEnabled(false);

		this.jlblSbj = new JLabel(EzimLang.Subject);

		this.etfSbj = new EzimTextField();
		this.etfSbj.setEditable(false);

		this.etaMsg = new EzimTextArea
		(
			new EzimPlainDocument(Ezim.maxMsgLength)
		);
		this.etaMsg.setLineWrap(true);
		this.etaMsg.setWrapStyleWord(true);
		this.etaMsg.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.etaMsg.setEditable(false);

		this.jspMsg = new JScrollPane();
		this.jspMsg.setMinimumSize(new Dimension(240, 100));

		this.jlblOpen = new JLabel(EzimLang.ClickHereToOpenMessage);
		this.jlblOpen.addMouseListener
		(
			new MouseListener()
			{
				public void mouseClicked(MouseEvent evtTmp)
				{
					jlblOpen_MouseClicked();
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

		this.jbtnReply = new JButton(EzimLang.Reply);
		this.jbtnReply.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMsgIn.this.jbtnReply_ActionPerformed();
				}
			}
		);
		this.jbtnReply.setEnabled(false);

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
							glBase.createParallelGroup(Alignment.LEADING)
								.addComponent
								(
									this.jlblRcvAt
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
								.addComponent
								(
									this.jlblName
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
								.addComponent
								(
									this.jlblSbj
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
									, GroupLayout.PREFERRED_SIZE
								)
						)
						.addGroup
						(
							glBase.createParallelGroup(Alignment.LEADING)
								.addComponent
								(
									this.jtfdRcvAt
									, GroupLayout.DEFAULT_SIZE
									, GroupLayout.PREFERRED_SIZE
									, Short.MAX_VALUE
								)
								.addComponent
								(
									this.jtfdName
									, GroupLayout.DEFAULT_SIZE
									, GroupLayout.PREFERRED_SIZE
									, Short.MAX_VALUE
								)
								.addComponent
								(
									this.etfSbj
									, GroupLayout.DEFAULT_SIZE
									, GroupLayout.PREFERRED_SIZE
									, Short.MAX_VALUE
								)
						)
				)
				.addComponent
				(
					this.jspMsg
					, GroupLayout.DEFAULT_SIZE
					, GroupLayout.PREFERRED_SIZE
					, Short.MAX_VALUE
				)
				.addGroup
				(
					glBase.createSequentialGroup()
						.addComponent
						(
							this.jlblOpen
							, GroupLayout.DEFAULT_SIZE
							, GroupLayout.PREFERRED_SIZE
							, Short.MAX_VALUE
						)
						.addComponent
						(
							this.jbtnReply
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
					this.jlblRcvAt
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jtfdRcvAt
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
					this.jlblSbj
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.etfSbj
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		vGrp.addComponent
		(
			this.jspMsg
/*
			, GroupLayout.DEFAULT_SIZE
			, GroupLayout.PREFERRED_SIZE
			, Short.MAX_VALUE
*/
			, GroupLayout.DEFAULT_SIZE
			, 100
			, Short.MAX_VALUE
		);

		vGrp.addGroup
		(
			glBase.createParallelGroup(Alignment.BASELINE)
				.addComponent
				(
					this.jlblOpen
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
				.addComponent
				(
					this.jbtnReply
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
					, GroupLayout.PREFERRED_SIZE
				)
		);

		glBase.setVerticalGroup(vGrp);

		this.addWindowListener(this);
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
		this.saveConf();
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
	 * "Reply" button event handler
	 */
	private void jbtnReply_ActionPerformed()
	{
		ArrayList<EzimContact> alEc = new ArrayList<EzimContact>();
		alEc.add(this.ec);

		StringBuilder sbSbj = new StringBuilder();
		if (! this.etfSbj.getText().startsWith("Re: "))
			sbSbj.append("Re: ");
		sbSbj.append(this.etfSbj.getText());

		StringBuilder sbMsg = new StringBuilder();
		sbMsg.append("\n\n----- ");
		sbMsg.append(EzimLang.OriginalMessageFrom);
		sbMsg.append(" ");
		sbMsg.append(ec.getName());
		sbMsg.append(" -----\n");
		sbMsg.append(this.etaMsg.getText());

		new EzimMsgOut(alEc, sbSbj.toString(), sbMsg.toString());

		this.saveConf();
		this.dispose();
	}

	/**
	 * "Open" label event handler on mouse click
	 */
	private void jlblOpen_MouseClicked()
	{
		this.jspMsg.setViewportView(this.etaMsg);
		this.jbtnReply.setEnabled(true);
		this.jlblOpen.setVisible(false);
	}
}
