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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ezim.core.EzimConf;

public class EzimThreadPool extends ThreadPoolExecutor
{
	private static EzimThreadPool etpool = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the thread pool
	 */
	private EzimThreadPool()
	{
		super
		(
			EzimConf.THPOOL_SIZE_CORE
			, EzimConf.THPOOL_SIZE_MAX
			, EzimConf.THPOOL_KEEPALIVE
			, TimeUnit.MINUTES
			, new LinkedBlockingQueue<Runnable>()
		);
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimThreadPool getInstance()
	{
		if (EzimThreadPool.etpool == null)
			EzimThreadPool.etpool = new EzimThreadPool();

		return EzimThreadPool.etpool;
	}
}
