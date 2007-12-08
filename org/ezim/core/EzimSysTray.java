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
package org.ezim.core;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimMsgOut;
import org.ezim.ui.EzimPlaza;

public class EzimSysTray {
	//java version, needed for systray
	public final static String JAVA_VERSION = "1.6";
	
	public static final int NOTIFICATION_TIME	= 2000; //ms
	private TrayIcon trayIcon;

	private static SystemTray tray;

	private PopupMenu popup;

	private ArrayList<EzimContact> alContacts;
	
	private ArrayList<String> usersToNotificate;

	private EzimMain ezMainWindows;

	private EzimPlaza ezPlz;

	private static final String JVERSION_ID = "java.version";

	private ImageIcon imageIcon;
	private MenuItem defaultSep;
	private MenuItem defaultSep1;
	private MenuItem defaultSep2;
	private ActionListener ezezPoSListener;
	private ActionListener actionListener;
	private ActionListener ezImListener;
	private ActionListener exitListener;
	private ActionListener msgToAllListener;
	private ItemListener openMsgsImmListener;
	private MenuItem ezPoS;
	private MenuItem ezImMain;
	private MenuItem ezItemExit;
	private MenuItem ezMsgToAll;
	private CheckboxMenuItem ezPropOpenMsgsImmediatly;
	private Menu ezProperties;
	private Object ezimTraySync;
	private static EzimSysTray ezimSysTray = null;
	private EzimConf ezimCnf;
	public boolean openMsgsImmediatly = true;
	
	
	private EzimSysTray() {
		if(!isAvailable())
			return;

		ezimCnf = EzimConf.getInstance();
		ezimTraySync = new Object();
		tray = SystemTray.getSystemTray();
		startNotification();
		initGui();
		updateSysTrayMenue();
	}

	synchronized public static EzimSysTray getInstance(){
		if(ezimSysTray == null){
			ezimSysTray = new EzimSysTray();
		}
		return ezimSysTray;
	}
	
	public boolean isAvailable()
	{
		if (!System.getProperty(JVERSION_ID).startsWith(JAVA_VERSION))
			return false;
		if (!SystemTray.isSupported())
			return false;
		
		return true;
	}
	
	
	private void initGui(){
		
		URL iconUrlTmp = null;

		iconUrlTmp = ClassLoader
				.getSystemResource("org/ezim/image/icon/ezim.png");
		imageIcon = new ImageIcon(iconUrlTmp);
		popup = new PopupMenu();

		// context menue ---------------------------------------
		defaultSep = new MenuItem("-");
		defaultSep1 = new MenuItem("-");
		defaultSep2 = new MenuItem("-");
		
		ezezPoSListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPlazaWindow();
			}
		};	
		ezImListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMainWindow();
			}
		};	
		exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		msgToAllListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				sendMessageToAll();
			}			
		};
		openMsgsImmListener = new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					openMsgsImmediatly = true;
				}else{
					openMsgsImmediatly = false;
				}
				ezPropOpenMsgsImmediatly.setState(openMsgsImmediatly);
				
				ezimCnf.settings.setProperty(
					EzimConf.ezimOpenMsgsImmediatly
					, String.valueOf(openMsgsImmediatly)
				);
				ezimCnf.write();
			}
		};
		ezPoS = new MenuItem(EzimLang.PlazaOfSpeech);
		ezPoS.addActionListener(ezezPoSListener);		

		ezImMain = new MenuItem(Ezim.appName);
		ezImMain.addActionListener(ezImListener);
		
		ezItemExit = new MenuItem(EzimLang.SystrayExit);
		ezItemExit.addActionListener(exitListener);
		
		ezProperties = new Menu(EzimLang.SystrayProperties);
		ezPropOpenMsgsImmediatly = new CheckboxMenuItem(EzimLang.SystrayOpenMessageImmediatly);
		ezPropOpenMsgsImmediatly.addItemListener(openMsgsImmListener);
		ezProperties.add(ezPropOpenMsgsImmediatly);
		if(ezimCnf.settings.getProperty(
				EzimConf.ezimOpenMsgsImmediatly).indexOf("true")==0){
			openMsgsImmediatly = true;
		}else{
			openMsgsImmediatly = false;
		}
		ezPropOpenMsgsImmediatly.setState(openMsgsImmediatly);
		ezMsgToAll = new MenuItem(EzimLang.SystrayMsgToAll);
		ezMsgToAll.addActionListener(msgToAllListener);
		
		//systray icon ----------------------------------------
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMainWindow();
			}
		};
		trayIcon = new TrayIcon(imageIcon.getImage(), Ezim.appName, popup);
		trayIcon.addActionListener(actionListener);
		trayIcon.setImageAutoSize(true);
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {}		
	}
	
	public void showNotification(String user){
		if(!isAvailable())
			return;

		usersToNotificate.add(user);
	}
	
	//note currently not used
	private void startNotification(){
		usersToNotificate = new ArrayList<String>();
		new Thread(){
			public void run(){
				while(true){
					if(usersToNotificate.size()<= 0){
						try{
							Thread.sleep(500);
						} catch (InterruptedException e) {}
						continue;
					}
					synchronized (ezimTraySync) {
						trayIcon.displayMessage(null, usersToNotificate.get(0) 
								+ " "+ EzimLang.HasReadMessage, 
								TrayIcon.MessageType.NONE);
					    usersToNotificate.remove(0);
						try {
							Thread.sleep(NOTIFICATION_TIME);
						} catch (InterruptedException e) {}
						
							//workaround to disable the notification after a 
							//given time, unfortunately this will lead to a
							//blinking tray icon
							tray.remove(trayIcon);
							trayIcon.displayMessage("null", "",
									TrayIcon.MessageType.NONE);
							try {
								tray.add(trayIcon);
						} catch (AWTException e) {}		
					}
				}//while
			}
		}.start();
	}
	
	public void updateSysTrayMenue() {
		if(!isAvailable())
			return;

		synchronized (ezimTraySync) {
			popup.removeAll();
	
			if (alContacts != null) {
				for (int i = 0; i < alContacts.size(); i++) {
					EzimContact contact = alContacts.get(i);
					EzimSysTrayActListener listener = new EzimSysTrayActListener(
							contact);
					MenuItem it = new MenuItem(contact.getName());
					it.addActionListener(listener);
					popup.add(it);
	
				}
			}
			if (alContacts != null) {
				trayIcon.setToolTip(Ezim.appName + "\n" + alContacts.size() + " "
						+ EzimLang.SystrayUsersOnline);
			}
	
			popup.add(defaultSep);
			popup.add(ezPoS);
			popup.add(ezMsgToAll);
			popup.add(defaultSep1);
			popup.add(ezProperties);
			popup.add(ezImMain);
			popup.add(defaultSep2);
			popup.add(ezItemExit);
		};
	}
	
	
	private void sendMessageToAll(){

		if(alContacts != null && alContacts.size()>0){
			new EzimMsgOut(
					alContacts	
			);
		}
	}
	
	
	public void setContactList(ArrayList<EzimContact> alContacts) {
		if(!isAvailable())
			return;

		this.alContacts = alContacts;
	}


	private void showMainWindow() {
		//TODO sometimes it does not work properly on doubleclick 
		//on systray icon 

		ezMainWindows.setState(Frame.ICONIFIED);
		ezMainWindows.setState(Frame.NORMAL);
		//ezMainWindows.setExtendedState(Frame.NORMAL);
		ezMainWindows.setVisible(true);
	}

	private void showPlazaWindow() {
		if (!this.ezPlz.isVisible()) {
			this.ezMainWindows.changeState(1);

			this.ezPlz.reset();
			this.ezPlz.setState(Frame.NORMAL);
			this.ezPlz.setVisible(true);
		}
		return;
	}

	public void setMainWindow(EzimMain main) {
		this.ezMainWindows = main;
	};

	public void setPlazaWindow(EzimPlaza ezPlaza) {
		this.ezPlz = ezPlaza;
	}
}
