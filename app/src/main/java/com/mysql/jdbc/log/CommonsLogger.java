/*
 Copyright (C) 2006 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as 
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package com.mysql.jdbc.log;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

public class CommonsLogger implements com.mysql.jdbc.log.Log {
	private Log logger;
	
	public CommonsLogger(String instanceName) {
		try {
			logger = LogFactory.getLogger("com.mysql.jbdc.log", instanceName);
		}catch (Exception e){
			//ignore
		}
	}

	public boolean isDebugEnabled() {
		return this.logger.isInfoEnabled();
	}

	public boolean isErrorEnabled() {
		return this.logger.isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return this.logger.isFatalEnabled();
	}

	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		return this.logger.isTraceEnabled();
	}

	public boolean isWarnEnabled() {
		return this.logger.isWarnEnabled();
	}

	public void logDebug(Object msg) {
		this.logger.logDebug(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logDebug(Object msg, Throwable thrown) {
		this.logger.logDebug(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

	public void logError(Object msg) {
		this.logger.logError(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logError(Object msg, Throwable thrown) {
		this.logger.logFatal(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

	public void logFatal(Object msg) {
		this.logger.logFatal(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logFatal(Object msg, Throwable thrown) {
		this.logger.logFatal(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

	public void logInfo(Object msg) {
		this.logger.logInfo(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logInfo(Object msg, Throwable thrown) {
		this.logger.logInfo(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

	public void logTrace(Object msg) {
		this.logger.logTrace(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logTrace(Object msg, Throwable thrown) {
		this.logger.logTrace(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

	public void logWarn(Object msg) {
		this.logger.logWarn(LogUtils.expandProfilerEventIfNecessary(msg));
	}

	public void logWarn(Object msg, Throwable thrown) {
		this.logger.logWarn(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
	}

}
