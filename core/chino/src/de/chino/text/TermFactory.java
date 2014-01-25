package de.chino.text;

public interface TermFactory {
	
	
	Term create(String s1, String s2, String s3, String s4, int i);

	TermFactory INSTANCE = new TermFactoryImpl();

}

class TermFactoryImpl implements TermFactory {

	@Override
	public Term create(String s1, String s2, String s3, String s4, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
