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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.ezim.core.EzimLogger;

public class EzimImage
{
	// main window icon
	public static ImageIcon icoMain;

	// about icon
	public static ImageIcon icoLblAbout;

	// state icons
	public static ImageIcon[] icoSysStates;
	public static ImageIcon[] icoStates;

	// EzimMain buttons
	public static ImageIcon[] icoButtons;

	// EzimTextEditingMenu icons
	public static ImageIcon[] icoTxtPops;

	/**
	 * retrieve icons from the sprite sheet specified by the given URL
	 * @param strUrl URL of the sprite sheet
	 * @param iW width of the icons on the sprite sheet
	 * @param iH height of the icons on the sprite sheet
	 * @param iI width of the icons to be scaled to
	 * @param iJ height of the icons to be scaled to
	 * @return icons
	 */
	private static ImageIcon[] loadIcons
	(
		String strUrl
		, int iW
		, int iH
		, int iI
		, int iJ
	)
	{
		ImageIcon[] arrOut = null;
		URL urlImg = null;
		BufferedImage biTmp = null;
		ArrayList<ImageIcon> alTmp = new ArrayList<ImageIcon>();

		// determine the sprite sheet's URL
		urlImg = ClassLoader.getSystemResource(strUrl);

		// load sprite sheet
		try
		{
			biTmp = ImageIO.read(urlImg);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		// find out the dimensions of the sprite sheet
		int iWidth = biTmp.getWidth();
		int iHeight = biTmp.getHeight();

		// load icons from the sprite sheet
		if (iW == iI && iH == iJ)
		{
			for(int iX = 0; iX < iWidth; iX += iW)
			{
				for(int iY = 0; iY < iHeight; iY += iH)
				{
					alTmp.add
					(
						new ImageIcon
						(
							biTmp.getSubimage(iX, iY, iW, iH)
						)
					);
				}
			}
		}
		// load and scale icons to 32x32 from the sprite sheet
		else
		{
			for(int iX = 0; iX < iWidth; iX += iW)
			{
				for(int iY = 0; iY < iHeight; iY += iH)
				{
					alTmp.add
					(
						new ImageIcon
						(
							biTmp.getSubimage(iX, iY, iW, iH)
								.getScaledInstance
								(
									iI
									, iJ
									, Image.SCALE_SMOOTH
								)
						)
					);
				}
			}
		}

		// convert the icons for output
		alTmp.trimToSize();
		arrOut = new ImageIcon[alTmp.size()];
		arrOut = alTmp.toArray(arrOut);

		return arrOut;
	}

	/**
	 * retrieve icons from the sprite sheet specified by the given URL and
	 * scale them to 32x32
	 * @param strUrl URL of the sprite sheet
	 * @param iW width of the icons on the sprite sheet
	 * @param iH height of the icons on the sprite sheet
	 * @return icons
	 */
	private static ImageIcon[] loadIcons
	(
		String strUrl
		, int iW
		, int iH
	)
	{
		return EzimImage.loadIcons(strUrl, iW, iH, 32, 32);
	}

	/**
	 * initialize public static class members
	 */
	public static void init()
	{
		URL urlImg = null;

		// load main window frame icon
		urlImg = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/ezim.png"
		);
		EzimImage.icoMain = new ImageIcon(urlImg);

		// load about icon
		urlImg = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/lblabout.png"
		);
		EzimImage.icoLblAbout = new ImageIcon
		(
			new ImageIcon(urlImg).getImage().getScaledInstance
			(
				16
				, 16
				, Image.SCALE_SMOOTH
			)
		);

		int iStateiconSize = EzimConf.UI_STATEICON_SIZE;

		// load system state icons
		EzimImage.icoSysStates = EzimImage.loadIcons
		(
			"org/ezim/image/icon/sysstates.png"
			, 32, 32
			, iStateiconSize, iStateiconSize
		);

		// load user state icons
		EzimImage.icoStates = EzimImage.loadIcons
		(
			"org/ezim/image/icon/states.png"
			, 32, 32
			, iStateiconSize, iStateiconSize
		);

		// load button images
		EzimImage.icoButtons = EzimImage.loadIcons
		(
			"org/ezim/image/icon/buttons.png"
			, 32, 32
		);

		// load text editing pop-up menu icons
		EzimImage.icoTxtPops = EzimImage.loadIcons
		(
			"org/ezim/image/icon/txtpopicons.png"
			, 32, 32
			, 16, 16
		);
	}
}
