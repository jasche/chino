package de.chino;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;

import de.chino.annotation.AnnotationService;
import de.chino.text.Text;

public class ChineseTextTest {

	@Test
	public void testChineseTxt() throws Exception {
		File prefFile = new File(ClassLoader.getSystemResource("Chinese2_Settings.csv").getFile());
		Language language = new Language(prefFile);
		File file = new File(ClassLoader.getSystemResource("chinese.txt").getFile()); 

		Text text = new Text(file, language);
		String 	annotatedTxt = AnnotationService.INSTANCE.annotate(text);
		assertNotNull(annotatedTxt);
		System.out.println(annotatedTxt);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(annotatedTxt.getBytes()));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/HTML/BODY/cspan[1]/text()");
		assertEquals("一天", expr.evaluate(doc));

		expr = xpath.compile("/HTML/BODY/cspan[1]/@style");
		assertEquals("background-color:red", expr.evaluate(doc));

	}

}
