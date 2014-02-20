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
package org.ezim.core;

import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

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
	/**
	 * construct an instance of the contact list
	 */
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
	public synchronized void addListDataListener(ListDataListener l)
	{
		this.listeners.add(l);
	}

	/**
	 * get size of the contact list
	 * @return size of the contact list
	 */
	public int getSize()
	{
		return this.list.size();
	}

	/**
	 * get element at the specified index
	 * @param iIdx the target element's index
	 */
	public EzimContact getElementAt(int iIdx)
	{
		EzimContact ecOut = null;

		try
		{
			ecOut = this.list.get(iIdx);
		}
		catch(Exception e)
		{
			ecOut = null;
		}

		return ecOut;
	}

	/**
	 * remove list data listener from the call list
	 * @param l listener to be added
	 */
	public synchronized void removeListDataListener(ListDataListener l)
	{
		this.listeners.remove(l);
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
	}

	/**
	 * normalize the given address if it is local
	 * @param iaIn address to be normalized
	 * @return untouched remote address / normalized local address
	 */
	private InetAddress normalizeAddressIfLocal(InetAddress iaIn)
	{
		InetAddress iaOut = iaIn;

		if (Ezim.isLocalAddress(iaIn))
			iaOut = Ezim.localAddress;

		return iaOut;
	}

	/**
	 * find and return index of the contact specified by the IP address
	 * @param iaIn address to look for
	 * @return index of the contact, or -1 if not found
	 */
	private int idxContact(InetAddress iaIn)
	{
		int iOut = -1;
		int iCnt = 0;
		int iLen = this.getSize();
		EzimContact ecTmp = null;

		if (iaIn != null)
		{
			try
			{
				InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);

				synchronized(this.list)
				{
					for(iCnt = 0; iCnt < iLen; iCnt ++)
					{
						ecTmp = (EzimContact) this.list.get(iCnt);

						if
						(
							ecTmp != null
							&& iaTmp.equals(ecTmp.getAddress())
						)
						{
							iOut = iCnt;
							break;
						}
					}
				}
			}
			catch(Exception e)
			{
				iOut = -1;
			}
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
		synchronized(this.list)
		{
			int iSize = this.getSize();

			if (iSize > 0)
			{
				this.list.clear();
				this.fireIntervalRemoved(0, iSize - 1);
			}
		}
	}

	/**
	 * find and return the contact specified by the IP address
	 * @param iaIn address to look for
	 * @return instance of the contact, or null if not found
	 */
	public EzimContact getContact(InetAddress iaIn)
	{
		EzimContact ecOut = null;
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = idxContact(iaTmp);

		if (iIdx > -1)
		{
			ecOut = this.getElementAt(iIdx);

			if
			(
				ecOut != null
				&& iaTmp.equals(iaIn)
				&& ! ecOut.getAddress().equals(iaTmp)
			)
			{
				ecOut = null;
			}
		}

		return ecOut;
	}

	/**
	 * add a new contact to the list (ordered by name) if not yet exists
	 * @param iaIn address of the new contact
	 * @param iPort DTX port of the new contact
	 * @param strName name of the new contact
	 * @param iSysState system state of the new contact
	 * @param iState (user) state of the new contact
	 * @param strStatus status of the new contact
	 */
	public void addContact
	(
		InetAddress iaIn
		, int iPort
		, String strName
		, int iSysState
		, int iState
		, String strStatus
	)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);

		if (this.idxContact(iaTmp) == -1)
		{
			synchronized(this.list)
			{
				try
				{
					EzimContact ecTmp = null;

					int iIdx = 0;
					int iLen = this.getSize();

					while(iIdx < iLen)
					{
						ecTmp = this.getElementAt(iIdx);

						if
						(
							strName.compareToIgnoreCase
							(
								ecTmp.getName()
							) < 0
						)
						{
							break;
						}

						iIdx ++;
					}

					this.list.add
					(
						iIdx
						, new EzimContact
						(
							iaTmp
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
	}

	/**
	 * remove contact from the list if exists
	 * @param iaIn address of the contact to be removed
	 */
	public void rmContact(InetAddress iaIn)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = idxContact(iaTmp);

		if (iIdx > -1)
		{
			synchronized(this.list)
			{
				this.list.remove(iIdx);
				this.list.trimToSize();

				this.fireIntervalRemoved(iIdx, iIdx);

				if (EzimSound.getInstance() != null)
					EzimSound.getInstance().playStateChg();
			}
		}
	}

	/**
	 * update an existing contact with the new DTX port
	 * @param iaIn address of the contact to be updated
	 * @param iPort new DTX port of the contact
	 */
	public void updContactPort(InetAddress iaIn, int iPort)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = this.idxContact(iaTmp);

		if (iIdx > -1)
		{
			try
			{
				EzimContact ecTmp = this.getElementAt(iIdx);

				if (ecTmp != null && ecTmp.getAddress().equals(iaTmp))
				{
					ecTmp.setPort(iPort);
					this.fireContentsChanged(iIdx, iIdx);
				}
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}

	/**
	 * update an existing contact with the new name
	 * @param iaIn address of the contact to be updated
	 * @param strName new name of the contact
	 */
	public void updContactName(InetAddress iaIn, String strName)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = this.idxContact(iaTmp);

		if (iIdx > -1)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);

			if (ecTmp != null && ecTmp.getAddress().equals(iaTmp))
			{
				ecTmp.setName(strName);
				this.fireContentsChanged(iIdx, iIdx);
			}
		}
	}

	/**
	 * update an existing contact with the new system state
	 * @param iaIn address of the contact to be update
	 * @param iState new state of the contact
	 */
	public void updContactSysState(InetAddress iaIn, int iState)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = this.idxContact(iaTmp);

		if (iIdx > -1)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);

			if (ecTmp != null && ecTmp.getAddress().equals(iaTmp))
			{
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
							iaTmp
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
							iaTmp
							, EzimLang.HasLeftPlazaOfSpeech
						);
					}
				}

				ecTmp.setSysState(iState);
				this.fireContentsChanged(iIdx, iIdx);

				if (EzimSound.getInstance() != null)
					EzimSound.getInstance().playStateChg();
			}
		}
	}

	/**
	 * update an existing contact with the new state
	 * @param iaIn address of the contact to be update
	 * @param iState new state of the contact
	 */
	public void updContactState(InetAddress iaIn, int iState)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = this.idxContact(iaTmp);

		if (iIdx > -1)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);

			if (ecTmp != null && ecTmp.getAddress().equals(iaTmp))
			{
				ecTmp.setState(iState);
				this.fireContentsChanged(iIdx, iIdx);

				if (EzimSound.getInstance() != null)
					EzimSound.getInstance().playStateChg();
			}
		}
	}

	/**
	 * update an existing contact with the new status
	 * @param iaIn address of the contact to be updated
	 * @param strStatus new status of the contact
	 */
	public void updContactStatus(InetAddress iaIn, String strStatus)
	{
		InetAddress iaTmp = this.normalizeAddressIfLocal(iaIn);
		int iIdx = this.idxContact(iaTmp);

		if (iIdx > -1)
		{
			EzimContact ecTmp = this.getElementAt(iIdx);

			if (ecTmp != null && ecTmp.getAddress().equals(iaTmp))
			{
				ecTmp.setStatus(strStatus);
				this.fireContentsChanged(iIdx, iIdx);

				if (EzimSound.getInstance() != null)
					EzimSound.getInstance().playStatusChg();
			}
		}
	}
}
