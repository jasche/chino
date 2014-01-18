package de.chino.text;

import static org.junit.Assert.*;

import java.io.File;
import java.io.ObjectInputStream.GetField;

import org.junit.Test;

import de.chino.Language;

public class TextTest {

	
	private File prefFile = new File(ClassLoader.getSystemResource("Chinese2_Settings.csv").getFile());
	private Language language = new Language(prefFile);
	private File file = new File(ClassLoader.getSystemResource("test.txt").getFile()); 
	private File htmlFile = new File(ClassLoader.getSystemResource("test.txt").getFile().replaceAll(".txt", ".html")); 
	
	@Test
	public void testTextFileLanguage() {
		Text fixture = new Text(file, language);

		assertNotNull(fixture);
		
	}

	@Test
	public void testTextString() {
		Text fixture = new Text(ClassLoader.getSystemResource("test.txt").getFile(), language);

		assertNotNull(fixture);
	}

	@Test
	public void testGetFile() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getFile());
	}

	@Test
	public void testGetInfo() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getInfo());
	}

	@Test
	public void testGetMarkedText() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getMarkedText(true));
		assertNotNull(fixture.getMarkedText(false));
	}

	@Test
	public void testGetMarkedTextSentence() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getMarkedTextSentence("foo"));
	}

	@Test
	public void testGetMarkIndexEnd() {
		Text fixture = new Text(file, language);
		assertEquals(0, fixture.getMarkIndexEnd());
	}

	@Test
	public void testGetMarkIndexStart() {
		Text fixture = new Text(file, language);
		assertEquals(0, fixture.getMarkIndexStart());
	}

	@Test
	public void testGetMissingSentenceCount() {
		Text fixture = new Text(file, language);
		assertEquals(0, fixture.getMissingSentenceCount());
	}

	@Test
	public void testGetPointedTextItemIndex() {
		Text fixture = new Text(file, language);
		assertEquals(-1, fixture.getPointedTextItemIndex(null));
	}

	@Test
	public void testGetText() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getText());
	}

	@Test
	public void testGetTextItems() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getTextItems());
	}
   
	@Test
	public void testGetTextRange() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getTextRange(2, 5, true));
	}

	@Test
	public void testGetTextSentence() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.getTextSentence(null));
	}

	@Test
	public void testGetUnlearnedWordCount() {
		Text fixture = new Text(file, language);
		assertEquals(44, fixture.getUnlearnedWordCount());
	}

	@Test
	public void testIsCoordSet() {
		Text fixture = new Text(file, language);
		assertFalse(fixture.isCoordSet());
	}

	@Test
	public void testIsRangeMarked() {
		Text fixture = new Text(file, language);
		assertFalse(fixture.isRangeMarked());
	}

	@Test
	public void testMatchWithTerms() {
		Text fixture = new Text(file, language);
		fixture.matchWithTerms();
	}

	@Test
	public void testSaveTextToHTMLFileForReview() {
		Text fixture = new Text(file, language);
		fixture.saveTextToHTMLFileForReview(htmlFile);
	}

	@Test
	public void testSetCoordSet() {
		Text fixture = new Text(file, language);
		fixture.setCoordSet(true);
	}

	@Test
	public void testSetMarkIndexEnd() {
		Text fixture = new Text(file, language);
		fixture.setMarkIndexEnd(3);
	}

	@Test
	public void testSetMarkIndexStart() {
		Text fixture = new Text(file, language);
		fixture.setMarkIndexStart(3);
	}

	@Test
	public void testSetMissingSentences() {
		Text fixture = new Text(file, language);
		assertEquals(0, fixture.setMissingSentences());
	}

	@Test
	public void testSetRangeMarked() {
		Text fixture = new Text(file, language);
		fixture.setRangeMarked(false);
	}

	@Test
	public void testToString() {
		Text fixture = new Text(file, language);
		assertNotNull(fixture.toString());
	}

}
