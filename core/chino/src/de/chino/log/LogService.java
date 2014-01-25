package de.chino.log;

public interface LogService {

	LogService INSTANCE = new LogServiceImpl();

	void d(String tag, String msg);

	void e(String tag, String msg);

	void i(String tag, String msg);

	void w(String tag, String msg);

	void d(String tag, String msg, Throwable t);

	void i(String tag, String msg, Throwable t);

	void e(String tag, String msg, Throwable t);

	void w(String tag, String msg, Throwable t);
}

class LogServiceImpl implements LogService {

	@Override
	public void d(String tag, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void e(String tag, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void i(String tag, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void w(String tag, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void d(String tag, String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void i(String tag, String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void e(String tag, String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void w(String tag, String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}
	
}