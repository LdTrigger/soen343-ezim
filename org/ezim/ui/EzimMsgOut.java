/*
    EZ Intranet Messenger
    Copyright (C) 2007 - 2008  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactList;
import org.ezim.core.EzimFtxList;
import org.ezim.core.EzimMsgSender;
import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimPlainDocument;
import org.ezim.ui.EzimTextArea;

public class EzimMsgOut
	extends JFrame
	implements WindowListener
{
	private EzimContact ec;

	private JPanel jpnlBase;
	private JLabel jlblName;
	private JTextField jtfdName;
	private JLabel jlblSbj;
	private JTextField jtfdSbj;
	private EzimTextArea etaMsg;
	private JScrollPane jspMsg;
	private JButton jbtnSend;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimMsgOut(EzimContact ecIn)
	{
		init(ecIn, (String) null, (String) null);
	}

	public EzimMsgOut(EzimContact ecIn, String strSbj, String strIn)
	{
		init(ecIn, strSbj, strIn);
		this.etaMsg.requestFocusInWindow();
	}

	private void init(EzimContact ecIn, String strSbj, String strIn)
	{
		this.ec = ecIn;

		this.loadConf();
		this.initGUI();

		if (strSbj != null && strSbj.length() > 0)
		{
			this.jtfdSbj.setText(strSbj);
		}

		if (strIn != null && strIn.length() > 0)
		{
			this.etaMsg.setText(strIn);
			this.etaMsg.setCaretPosition(0);
		}

		this.setIconImage(EzimImage.icoMsg.getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(EzimLang.OutgoingMessage);
		this.setMinimumSize(new Dimension(320, 200));
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
					EzimConf.ezimmsgoutLocationX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmsgoutLocationY
				)
			)
		);
		this.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmsgoutSizeW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.ezimmsgoutSizeH
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
			EzimConf.ezimmsgoutLocationX
			, String.valueOf((int) ptTmp.getX())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmsgoutLocationY
			, String.valueOf((int) ptTmp.getY())
		);
		Dimension dmTmp = this.getSize();
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmsgoutSizeW
			, String.valueOf((int) dmTmp.getWidth())
		);
		ecTmp.settings.setProperty
		(
			EzimConf.ezimmsgoutSizeH
			, String.valueOf((int) dmTmp.getHeight())
		);

		return;
	}

	private void initGUI()
	{
		// C O M P O N E N T S ---------------------------------------------
		this.jlblName = new JLabel(EzimLang.To);

		this.jtfdName = new JTextField(this.ec.getName());
		this.jtfdName.setEnabled(false);

		this.jlblSbj = new JLabel(EzimLang.Subject);

		this.jtfdSbj = new JTextField
		(
			new EzimPlainDocument(Ezim.dtxBufLen)
			, ""
			, 0
		);

		this.etaMsg = new EzimTextArea();
		this.etaMsg.setLineWrap(true);
		this.etaMsg.setWrapStyleWord(true);
		this.etaMsg.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		this.jspMsg = new JScrollPane(this.etaMsg);

		this.jbtnSend = new JButton(EzimLang.Send);
		this.jbtnSend.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent evtTmp)
				{
					EzimMsgOut.this.jbtnSend_ActionPerformed(evtTmp);
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
					glBase.createParallelGroup(Alignment.LEADING)
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
						this.jtfdName
						, GroupLayout.DEFAULT_SIZE
						, GroupLayout.PREFERRED_SIZE
						, Short.MAX_VALUE
					)
					.addComponent
					(
						this.jtfdSbj
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
			.addComponent
			(
				this.jbtnSend
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
				this.jlblSbj
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
				, GroupLayout.PREFERRED_SIZE
			)
			.addComponent
			(
				this.jtfdSbj
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

		vGrp.addComponent
		(
			this.jbtnSend
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
			, GroupLayout.PREFERRED_SIZE
		);

		glBase.setVerticalGroup(vGrp);

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
		return;
	}

	public void windowClosing(WindowEvent e)
	{
		this.saveConf();
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
	private void jbtnSend_ActionPerformed(ActionEvent evt)
	{
		if (EzimContactList.getInstance().get(this.ec.getIp()) == null)
		{
			JOptionPane.showMessageDialog
			(
				null
				, EzimLang.RecipientNotExists
				, EzimLang.Error
				, JOptionPane.ERROR_MESSAGE
			);
		}
		else if (this.etaMsg.getText().length() > 0)
		{
			EzimMsgSender jmsTmp = new EzimMsgSender
			(
				this
				, this.ec.getIp()
				, this.jtfdSbj.getText()
				, this.etaMsg.getText()
			);
			jmsTmp.start();
		}

		return;
	}
}
