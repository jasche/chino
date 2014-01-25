package de.chino.text;

import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.ietf.jgss.Oid;
import org.junit.Test;

public class TextItemTest {

	Dimension dimension = new Dimension() {};
	Point point = new Point() {};
	
	@Test
	public void testTextItem() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNotNull(fixture);
	}

	@Test
	public void testGetAfterItemDimension() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNull(fixture.getAfterItemDimension());
	}

	@Test
	public void testGetAfterItemPosition() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNull(fixture.getAfterItemPosition());
	}

	@Test
	public void testGetAfterItemValue() {
		TextItem fixture = new TextItem("foo", "bar");
		assertEquals("bar",fixture.getAfterItemValue());
	}

	@Test
	public void testGetLink() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNull(fixture.getLink());
	}

	@Test
	public void testGetTextItemDimension() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNull(fixture.getTextItemDimension());

	}

	@Test
	public void testGetTextItemLowerCaseValue() {
		TextItem fixture = new TextItem("foo", "bar");
		assertEquals("foo", fixture.getTextItemLowerCaseValue());
	}

	@Test
	public void testGetTextItemPosition() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNull(fixture.getTextItemPosition());
	}

	@Test
	public void testGetTextItemValue() {
		TextItem fixture = new TextItem("foo", "bar");
		assertEquals("foo", fixture.getTextItemValue());
	}

	@Test
	public void testIsLastWord() {
		TextItem fixture = new TextItem("foo", "bar");
		assertTrue(fixture.isLastWord());
	}

	@Test
	public void testIsPointOnTextItem() {
		TextItem fixture = new TextItem("foo", "bar");
		assertFalse(fixture.isPointOnTextItem(null));
	}

	@Test
	public void testSetAfterItemDimension() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setAfterItemDimension(dimension);
	}

	@Test
	public void testSetAfterItemPosition() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setAfterItemPosition(point);
	}

	@Test
	public void testSetLastWord() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setLastWord(false);
	}

	@Test
	public void testSetLink() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setLink(TermFactory.INSTANCE.create("s1", "s2", "s3", "s4", 1));
	}

	@Test
	public void testSetTextItemDimension() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setTextItemDimension(dimension);	
	}

	@Test
	public void testSetTextItemPosition() {
		TextItem fixture = new TextItem("foo", "bar");
		fixture.setTextItemPosition(point);	
	}

	@Test
	public void testToString() {
		TextItem fixture = new TextItem("foo", "bar");
		assertNotNull(fixture.toString());	
	}


}
