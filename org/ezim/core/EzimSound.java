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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.ezim.core.EzimLogger;
import org.ezim.core.EzimConf;

public class EzimSound
{
	// internal sound effect file URL
	private static String stateChgUrl = "org/ezim/sound/fx/stateChg.wav";
	private static String statusChgUrl = "org/ezim/sound/fx/statusChg.wav";
	private static String msgInUrl = "org/ezim/sound/fx/msgIn.wav";
	private static String fileInUrl = "org/ezim/sound/fx/fileIn.wav";

	// sound effect audio clip
	private Clip stateChg = null;
	private Clip statusChg = null;
	private Clip msgIn = null;
	private Clip fileIn = null;

	// singleton object
	private static EzimSound es = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the sound class
	 */
	private EzimSound()
	{
		this.init();
	}

	// P R I V A T E   M E T H O D -----------------------------------------
	/**
	 * initialize an audio clip with the given URL
	 * @param strIn URL of the audio clip
	 * @param strDefault default URL of the audio clip
	 * @return the initialized audio clip
	 */
	private Clip initClip(String strIn, String strDefault)
	{
		Clip clipOut = null;

		try
		{
			clipOut = AudioSystem.getClip();

			try
			{
				clipOut.open
				(
					AudioSystem.getAudioInputStream
					(
						new java.io.File(strIn)
					)
				);
			}
			catch(Exception e)
			{
				if (null != strIn && 0 < strIn.length())
					EzimLogger.getInstance().warning(e.getMessage(), e);

				clipOut.open
				(
					AudioSystem.getAudioInputStream
					(
						ClassLoader.getSystemResource(strDefault)
					)
				);
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
		}

		return clipOut;
	}

	/**
	 * initialize all sound effect audio input streams
	 */
	private void init()
	{
		this.stateChg = this.initClip
		(
			EzimConf.SOUND_STATECHG
			, EzimSound.stateChgUrl
		);

		this.statusChg = this.initClip
		(
			EzimConf.SOUND_STATUSCHG
			, EzimSound.statusChgUrl
		);

		this.msgIn = this.initClip
		(
			EzimConf.SOUND_MSGIN
			, EzimSound.msgInUrl
		);

		this.fileIn = this.initClip
		(
			EzimConf.SOUND_FILEIN
			, EzimSound.fileInUrl
		);
	}

	/**
	 * play the specified audio clip
	 * @param clipIn audio clip
	 */
	private void playClip(Clip clipIn)
	{
		if (null == clipIn) return;

		clipIn.setFramePosition(0);
		clipIn.start();
	}

	// P U B L I C   M E T H O D -------------------------------------------
	/**
	 * return an EzimSound object
	 * @return the singleton EzimSound object
	 */
	public static EzimSound getInstance()
	{
		if (EzimConf.SOUND_ENABLED)
		{
			if (EzimSound.es == null)
			{
				EzimSound.es = new EzimSound();
			}
		}
		else
		{
			EzimSound.es = null;
		}

		return EzimSound.es;
	}

	/**
	 * play the "user state changed" sound effect
	 */
	public void playStateChg()
	{
		this.playClip(this.stateChg);
	}

	/**
	 * play the "user status changed" sound effect
	 */
	public void playStatusChg()
	{
		this.playClip(this.statusChg);
	}

	/**
	 * play the "incoming message arrived" sound effect
	 */
	public void playMsgIn()
	{
		this.playClip(this.msgIn);
	}

	/**
	 * play the "incoming file (request) arrived" sound effect
	 */
	public void playFileIn()
	{
		this.playClip(this.fileIn);
	}
}
