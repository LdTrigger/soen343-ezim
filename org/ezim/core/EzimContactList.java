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
package org.ezim.core;

import java.util.ArrayList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.ListModel;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimSound;

import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimPlaza;

public class EzimContactList implements ListModel
{
	private static EzimContactList contacts = null;

	private ArrayList<EzimContact> list = null;
	private ArrayList<ListDataListener> listeners = null;

	// C O N S T R U C T O R -----------------------------------------------
	private EzimContactList()
	{
		this.list = new ArrayList<EzimContact>();
		this.listeners = new ArrayList<ListDataListener>();
	}

	// I N T E R F A C E   M E T H O D   I M P L E M E N T A T I O N -------
	/**
	 * add list data listener to the call list
	 * @param l listener to be added
	 */
	public void addListDataListener(ListDataListener l)
	{
		this.listeners.add(l);
		return;
	}

	/**
	 * get size of the contact list
	 * @return size of the contact list
	 */
	public int getSize()
	{
		this.list.trimToSize();
		return this.list.size();
	}

	/**
	 * get element at the specified index
	 * @param iIdx the target element's index
	 */
	public EzimContact getElementAt(int iIdx)
	{
		return this.list.get(iIdx);
	}

	/**
	 * remove list data listener from the call list
	 * @param l listener to be added
	 */
	public void removeListDataListener(ListDataListener l)
	{
		this.listeners.remove(l);
		return;
	}

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * fire "contents changed" event to all listeners
	 * @param iIdx0 end of the changed interval (inclusive)
	 * @param iIdx1 the other end of the changed interval (inclusive)
	 */
	private void fireContentsChanged(int iIdx0, int iIdx1)
	{
		int iCnt = 0;
		int iLen = this.listeners.size();
		ListDataEvent ldeTmp = new ListDataEvent
		(
			this
			, ListDataEvent.CONTENTS_CHANGED
			, iIdx0
			, iIdx1
		);

		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			this.listeners.get(iCnt).contentsChanged(ldeTmp);
		}

		return;
	}

	/**
	 * fire "interval added" event to all listeners
	 * @param iIdx0 end of the new interval (inclusive)
	 * @param iIdx1 the other end of the new interval (inclusive)
	 */
	private void fireIntervalAdded(int iIdx0, int iIdx1)
	{
		int iCnt = 0;
		int iLen = this.listeners.size();
		ListDataEvent ldeTmp = new ListDataEvent
		(
			this
			, ListDataEvent.INTERVAL_ADDED
			, iIdx0
			, iIdx1
		);

		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			this.listeners.get(iCnt).intervalAdded(ldeTmp);
		}

		return;
	}

	/**
	 * fire "interval removed" event to all listeners
	 * @param iIdx0 end of the removed interval (inclusive)
	 * @param iIdx1 the other end of the removed interval (inclusive)
	 */
	private void fireIntervalRemoved(int iIdx0, int iIdx1)
	{
		int iCnt = 0;
		int iLen = this.listeners.size();
		ListDataEvent ldeTmp = new ListDataEvent
		(
			this
			, ListDataEvent.INTERVAL_REMOVED
			, iIdx0
			, iIdx1
		);

		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			this.listeners.get(iCnt).intervalRemoved(ldeTmp);
		}

		return;
	}

	/**
	 * find and return index of the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return index of the contact, or -1 if not found
	 */
	private int idxContact(String strIp)
	{
		int iOut = -1;
		int iCnt = 0;
		int iLen = this.getSize();
		EzimContact ecTmp = null;

		try
		{
			for(iCnt = 0; iCnt < iLen; iCnt ++)
			{
				ecTmp = (EzimContact) this.list.get(iCnt);

				if (strIp.equals(ecTmp.getIp()))
				{
					iOut = iCnt;
					break;
				}
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);

			iOut = -1;
		}

		return iOut;
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimContactList getInstance()
	{
		return EzimContactList.getInstance(false);
	}

	/**
	 * reset and return the only instance of this class
	 * @param blnReset force reseting the instance if true
	 * @return instance of this class
	 */
	public static EzimContactList getInstance(boolean blnReset)
	{
		if (EzimContactList.contacts == null)
		{
			EzimContactList.contacts = new EzimContactList();
		}
		else if (blnReset)
		{
			EzimContactList.contacts.clear();
		}

		return EzimContactList.contacts;
	}

	/**
	 * remove all elements from this list
	 */
	public void clear()
	{
		int iSize = this.getSize();

		if (iSize > 0)
		{
			synchronized(this.list)
			{
				this.list.clear();
			}

			this.fireIntervalRemoved(0, iSize - 1);
		}

		return;
	}

	/**
	 * find and return the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return instance of the contact, or null if not found
	 */
	public EzimContact getContact(String strIp)
	{
		EzimContact ecOut = null;
		int iIdx = idxContact(strIp);

		if (iIdx != -1)
			ecOut = this.getElementAt(iIdx);

		return ecOut;
	}

	/**
	 * add a new contact to the list (ordered by name) if not yet exists
	 * @param strIp IP address of the new contact
	 * @param iPort DTX port of the new contact
	 * @param strName name of the new contact
	 * @param iSysState system state of the new contact
	 * @param iState (user) state of the new contact
	 * @param strStatus status of the new contact
	 */
	public void addContact
	(
		String strIp
		, int iPort
		, String strName
		, int iSysState
		, int iState
		, String strStatus
	)
	{
		synchronized(this.list)
		{
			if (this.idxContact(strIp) == -1)
			{
				try
				{
					EzimContact ecTmp = null;

					int iIdx = 0;
					int iLen = this.getSize();

					while(iIdx < iLen)
					{
						ecTmp = this.getElementAt(iIdx);

						if (strName.compareTo(ecTmp.getName()) < 0) break;

						iIdx ++;
					}

					this.list.add
					(
						iIdx
						, new EzimContact
						(
							strIp
							, iPort
							, strName
							, iSysState
							, iState
							, strStatus
						)
					);

					this.fireIntervalAdded(iIdx, iIdx);

					if (EzimSound.getInstance() != null)
						EzimSound.getInstance().playStateChg();
				}
				catch(EzimContactException eceTmp)
				{
					EzimLogger.getInstance().severe
					(
						eceTmp.getMessage()
						, eceTmp
					);
				}
			}
		}

		return;
	}

	/**
	 * remove contact from the list if exists
	 * @param strIp IP address of the contact to be removed
	 */
	public void rmContact(String strIp)
	{
		synchronized(this.list)
		{
			int iIdx = idxContact(strIp);

			if (iIdx != -1)
			{
				this.list.remove(iIdx);

				this.fireIntervalRemoved(iIdx, iIdx);

				if (EzimSound.getInstance() != null)
					EzimSound.getInstance().playStateChg();
			}
		}

		return;
	}

	/**
	 * update an existing contact with the new DTX port
	 * @param strIp IP address of the contact to be updated
	 * @param iPort new DTX port of the contact
	 */
	public void updContactPort(String strIp, int iPort)
	{
		int iIdx = this.idxContact(strIp);

		if (iIdx >= 0)
		{
			try
			{
				EzimContact ecTmp = this.getElementAt(iIdx);
				ecTmp.setPort(iPort);
				this.fireContentsChanged(iIdx, iIdx);
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}

		return;
	}

	/**
	 * update an existing contact with the new name
	 * @param strIp IP address of the contact to be updated
	 * @param strName new name of the contact
	 */
	public void updContactName(String strIp, String strName)
	{
		int iIdx = this.idxContact(strIp);

		if (iIdx >= 0)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);
			ecTmp.setName(strName);
			this.fireContentsChanged(iIdx, iIdx);
		}

		return;
	}

	/**
	 * update an existing contact with the new system state
	 * @param strIp IP address of the contact to be update
	 * @param iState new state of the contact
	 */
	public void updContactSysState(String strIp, int iState)
	{
		int iIdx = this.idxContact(strIp);

		if (iIdx >= 0)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);

			// post auto narration in the plaza
			EzimPlaza epTmp = EzimMain.getInstance().epMain;
			if (epTmp.isVisible())
			{
				if
				(
					ecTmp.getSysState() != EzimContact.SYSSTATE_PLAZA
					&& iState == EzimContact.SYSSTATE_PLAZA
				)
				{
					epTmp.addNarration
					(
						strIp
						, EzimLang.HasJoinedPlazaOfSpeech
					);
				}
				else if
				(
					ecTmp.getSysState() == EzimContact.SYSSTATE_PLAZA
					&& iState != EzimContact.SYSSTATE_PLAZA
				)
				{
					epTmp.addNarration
					(
						strIp
						, EzimLang.HasLeftPlazaOfSpeech
					);
				}
			}

			ecTmp.setSysState(iState);
			this.fireContentsChanged(iIdx, iIdx);


			if (EzimSound.getInstance() != null)
				EzimSound.getInstance().playStateChg();
		}

		return;
	}

	/**
	 * update an existing contact with the new state
	 * @param strIp IP address of the contact to be update
	 * @param iState new state of the contact
	 */
	public void updContactState(String strIp, int iState)
	{
		int iIdx = this.idxContact(strIp);

		if (iIdx >= 0)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);
			ecTmp.setState(iState);
			this.fireContentsChanged(iIdx, iIdx);

			if (EzimSound.getInstance() != null)
				EzimSound.getInstance().playStateChg();
		}

		return;
	}

	/**
	 * update an existing contact with the new status
	 * @param strIp IP address of the contact to be updated
	 * @param strStatus new status of the contact
	 */
	public void updContactStatus(String strIp, String strStatus)
	{
		int iIdx = this.idxContact(strIp);

		if (iIdx >= 0)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);
			ecTmp.setStatus(strStatus);
			this.fireContentsChanged(iIdx, iIdx);

			if (EzimSound.getInstance() != null)
				EzimSound.getInstance().playStatusChg();
		}

		return;
	}
}
