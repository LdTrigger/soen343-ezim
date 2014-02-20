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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactList;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMsgOut;

public class EzimContactTransferHandler
	extends TransferHandler
{
	// C O N S T A N T -----------------------------------------------------
	final public static String MIMETYPE
		= DataFlavor.javaJVMLocalObjectMimeType
		+ ";class=java.util.ArrayList";

	// A T T R I B U T E ---------------------------------------------------
	private DataFlavor dfLocal;
	private DataFlavor dfSerial;
	private JList jlstSrc = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the transfer handler
	 */
	private EzimContactTransferHandler()
	{
		try
		{
			this.dfLocal = new DataFlavor
			(
				EzimContactTransferHandler.MIMETYPE
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		this.dfSerial = new DataFlavor(ArrayList.class, "ArrayList");
	}

	// S I N G L E T O N ---------------------------------------------------
	private static EzimContactTransferHandler token = null;

	/**
	 * return the singleton class instance
	 * @return the singleton class instance
	 */
	public static EzimContactTransferHandler getInstance()
	{
		if (EzimContactTransferHandler.token == null)
		{
			EzimContactTransferHandler.token
				= new EzimContactTransferHandler();
		}

		return EzimContactTransferHandler.token;
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * check and see if the array has our MIMETYPE'd DataFlavor
	 * @param arrDfIn target array to be checked
	 */
	private boolean hasLocalArrayListFlavor(DataFlavor[] arrDfIn)
	{
		if (this.dfLocal == null) return false;

		for(int iCnt = 0; iCnt < arrDfIn.length; iCnt ++)
		{
			if (arrDfIn[iCnt].equals(this.dfLocal)) return true;
		}

		return false;
	}

	/**
	 * check and see if the array has our serialized DataFlavor
	 * @param arrDfIn target array to be checked
	 */
	private boolean hasSerialArrayListFlavor(DataFlavor[] arrDfIn)
	{
		if (this.dfSerial == null) return false;

		for(int iCnt = 0; iCnt < arrDfIn.length; iCnt ++)
		{
			if (arrDfIn[iCnt].equals(this.dfSerial)) return true;
		}

		return false;
	}

	// P R O T E C T E D ---------------------------------------------------
	/**
	 * execute post-data-export actions
	 * @param jcIn data source component
	 * @param tIn transferred data
	 * @param iActIn action performed
	 */
	protected void exportDone(JComponent jcIn, Transferable tIn, int iActIn)
	{
		// nothing needed to be done here
	}

	/**
	 * create a transferable to use as the source for a data transfer
	 * @param jcIn component holding the data to be transferred
	 */
	protected Transferable createTransferable(JComponent jcIn)
	{
		EzimContactTransferable ectOut = null;

		if
		(
			jcIn instanceof JList
			&& ((JList<?>) jcIn).getModel() instanceof EzimContactList
		)
		{
				@SuppressWarnings("unchecked")
				JList<EzimContact> jlstEc = (JList<EzimContact>) jcIn;

				ectOut = new EzimContactTransferable
				(
					jlstEc.getSelectedValuesList()
				);
		}

		return ectOut;
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * cause a transfer to a component from a clipboard or a DND drop
	 * @param jcIn drag and drop target component
	 * @param tIn data to import
	 */
	public boolean importData(JComponent jcIn, Transferable tIn)
	{
		if (! this.canImport(jcIn, tIn.getTransferDataFlavors()))
		{
			return false;
		}

		try
		{
			EzimMsgOut emoTarget = null;
			Object objTmp = null;

			emoTarget = (EzimMsgOut) jcIn.getParent().getParent()
				.getParent().getParent().getParent();

			if (this.hasLocalArrayListFlavor(tIn.getTransferDataFlavors()))
			{
				// buffer transferred data
				objTmp = tIn.getTransferData(this.dfLocal);

				if (objTmp instanceof ArrayList)
				{
					@SuppressWarnings("unchecked")
					ArrayList<EzimContact> alTmp
						= (ArrayList<EzimContact>) objTmp;

					// import transferred data
					emoTarget.addContacts(alTmp);
				}
			}
			else if
			(
				this.hasSerialArrayListFlavor(tIn.getTransferDataFlavors())
			)
			{
				// buffer transferred data
				objTmp = tIn.getTransferData(this.dfSerial);

				if (objTmp instanceof ArrayList)
				{
					@SuppressWarnings("unchecked")
					ArrayList<EzimContact> alTmp
						= (ArrayList<EzimContact>) objTmp;

					// import transferred data
					emoTarget.addContacts(alTmp);
				}
			}
			else
			{
				return false;
			}
		}
		catch(UnsupportedFlavorException ufe)
		{
			EzimLogger.getInstance().severe(ufe.getMessage(), ufe);
			return false;
		}
		catch(IOException ioe)
		{
			EzimLogger.getInstance().severe(ioe.getMessage(), ioe);
			return false;
		}

		return true;
	}

	/**
	 * check if a component can import this data type
	 * @param jcIn target component
	 * @param arrDfIn available data formats
	 */
	public boolean canImport(JComponent jcIn, DataFlavor[] arrDfIn)
	{
		boolean blnOut =
		(
			(
				jcIn.getParent().getParent().getParent().getParent()
					.getParent() instanceof EzimMsgOut
			)
			&& (
				this.hasLocalArrayListFlavor(arrDfIn)
				|| this.hasSerialArrayListFlavor(arrDfIn)
			)
		);

		return blnOut;
	}

	/**
	 * return the type of transfer actions supported by the source
	 */
	public int getSourceActions(JComponent jcIn)
	{
		return COPY;
	}

	// I N T E R N A L   C L A S S -----------------------------------------
	public class EzimContactTransferable
		implements Transferable
	{
		ArrayList<EzimContact> list;

		// C O N S T R U C T O R -------------------------------------------
		/**
		 * construct an instance of the contact transferable class
		 * @param alIn list of data to be transferred
		 */
		public EzimContactTransferable(List<EzimContact> lIn)
		{
			this.list = new ArrayList<EzimContact>(lIn);
		}

		// P U B L I C -----------------------------------------------------
		/**
		 * get an object which represents the data to be transferred
		 * @param dfIn the requested flavor for the data
		 * @return an object which represents the data to be transferred
		 */
		public Object getTransferData(DataFlavor dfIn)
			throws UnsupportedFlavorException
		{
			if (! this.isDataFlavorSupported(dfIn))
			{
				throw new UnsupportedFlavorException(dfIn);
			}

			return this.list;
		}

		/**
		 * Returns an array of DataFlavor objects indicating the flavors the
		 * data can be provided in
		 * @return an array of data flavors in which this data can be
		 *   transferred
		 */
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]
			{
				EzimContactTransferHandler.this.dfLocal
				, EzimContactTransferHandler.this.dfSerial
			};
		}

		/**
		 * check whether or not the specified data flavor is supported for
		 * this object
		 * @param dfIn the requested flavor for the data
		 * @return boolean indicating whether or not the data flavor is
		 *   supported
		 */
		public boolean isDataFlavorSupported(DataFlavor dfIn)
		{
			boolean blnOut = false;

			if (EzimContactTransferHandler.this.dfLocal == dfIn)
			{
				blnOut = true;
			}

			if (EzimContactTransferHandler.this.dfSerial == dfIn)
			{
				blnOut = true;
			}

			return blnOut;
		}
	}
}
