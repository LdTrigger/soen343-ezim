/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2013  Chun-Kwong Wong
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

public class EzimSound
{
	// internal sound effect file URL
	private static String stateChgUrl = "org/ezim/sound/fx/stateChg.wav";
	private static String statusChgUrl = "org/ezim/sound/fx/statusChg.wav";
	private static String msgInUrl = "org/ezim/sound/fx/msgIn.wav";
	private static String fileInUrl = "org/ezim/sound/fx/fileIn.wav";

	// sound effect audio clip
	private static Clip stateChg = null;
	private static Clip statusChg = null;
	private static Clip msgIn = null;
	private static Clip fileIn = null;

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
	 * initialize all sound effect audio input streams
	 */
	private void init()
	{
		// determine sound effect file URI
		String strStateChg = EzimSound.stateChgUrl;
		String strStatusChg = EzimSound.statusChgUrl;
		String strMsgIn = EzimSound.msgInUrl;
		String strFileIn = EzimSound.fileInUrl;

		// !!! ANY CHANGE BY CONFIGURATIONS SHOULD BE DONE HERE !!!

		// retrieve sound effect
		try
		{
			this.stateChg = AudioSystem.getClip();
			this.stateChg.open
			(
				AudioSystem.getAudioInputStream
				(
					ClassLoader.getSystemResource(strStateChg)
				)
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
		}

		try
		{
			this.statusChg = AudioSystem.getClip();
			this.statusChg.open
			(
				AudioSystem.getAudioInputStream
				(
					ClassLoader.getSystemResource(strStatusChg)
				)
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
		}

		try
		{
			this.msgIn = AudioSystem.getClip();
			this.msgIn.open
			(
				AudioSystem.getAudioInputStream
				(
					ClassLoader.getSystemResource(strMsgIn)
				)
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
		}

		try
		{
			this.fileIn = AudioSystem.getClip();
			this.fileIn.open
			(
				AudioSystem.getAudioInputStream
				(
					ClassLoader.getSystemResource(strFileIn)
				)
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
		}
	}

	// P U B L I C   M E T H O D -------------------------------------------
	/**
	 * return an EzimSound object
	 * @return the singleton EzimSound object
	 */
	public static EzimSound getInstance()
	{
		boolean blnEnabled = Boolean.parseBoolean
		(
			EzimConf.getInstance().settings.getProperty
			(
				EzimConf.ezimsoundEnabled
			)
		);

		if (blnEnabled)
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
		this.stateChg.setFramePosition(0);
		this.stateChg.start();
	}

	/**
	 * play the "user status changed" sound effect
	 */
	public void playStatusChg()
	{
		this.statusChg.setFramePosition(0);
		this.statusChg.start();
	}

	/**
	 * play the "incoming message arrived" sound effect
	 */
	public void playMsgIn()
	{
		this.msgIn.setFramePosition(0);
		this.msgIn.start();
	}

	/**
	 * play the "incoming file (request) arrived" sound effect
	 */
	public void playFileIn()
	{
		this.fileIn.setFramePosition(0);
		this.fileIn.start();
	}
}
