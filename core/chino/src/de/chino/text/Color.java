package de.chino.text;

public interface Color {

	Color INSTANCE = new ColorImpl();
	Color WHITE = null;

	Color create(int i, int j, int k);
}

class ColorImpl implements Color{

	@Override
	public Color create(int i, int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}
	
}