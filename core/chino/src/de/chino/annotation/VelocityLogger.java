package de.chino.annotation;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

import de.chino.log.LogService;

 
public class VelocityLogger implements LogChute {
	private final static String tag = "Velocity";
 
	@Override
	public void init(RuntimeServices arg0) throws Exception {
	}
 
	@Override
	public boolean isLevelEnabled(int level) {
		return level > LogChute.DEBUG_ID;
	}
 
	@Override
	public void log(int level, String msg) {
		switch(level) {
			case LogChute.DEBUG_ID:
				LogService.INSTANCE.d(tag,msg);
				break;
			case LogChute.ERROR_ID:
				LogService.INSTANCE.e(tag,msg);
				break;
			case LogChute.INFO_ID:
				LogService.INSTANCE.i(tag,msg);
				break;
			case LogChute.TRACE_ID:
				LogService.INSTANCE.d(tag,msg);
				break;
			case LogChute.WARN_ID:
				LogService.INSTANCE.w(tag,msg);
		}
	}
 
	@Override
	public void log(int level, String msg, Throwable t) {
		switch(level) {
			case LogChute.DEBUG_ID:
				LogService.INSTANCE.d(tag,msg,t);
				break;
			case LogChute.ERROR_ID:
				LogService.INSTANCE.e(tag,msg,t);
				break;
			case LogChute.INFO_ID:
				LogService.INSTANCE.i(tag,msg,t);
				break;
			case LogChute.TRACE_ID:
				LogService.INSTANCE.d(tag,msg,t);
				break;
			case LogChute.WARN_ID:
				LogService.INSTANCE.w(tag,msg,t);
		}
	}
}