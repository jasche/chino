package de.chino.annotation;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import de.chino.Language;
import de.chino.text.Text;

public class AnnotationServiceTest {
	
	private File prefFile = new File(ClassLoader.getSystemResource("Chinese2_Settings.csv").getFile());
	private Language language = new Language(prefFile);
	private File file = new File(ClassLoader.getSystemResource("chinese.txt").getFile()); 


	@Test
	public void testAnnotate() {
		assertEquals("<HTML>", AnnotationService.INSTANCE.annotate(new Text(file, language)).substring(0, 6));
	}

}
