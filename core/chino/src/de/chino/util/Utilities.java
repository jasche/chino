package de.chino.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import de.chino.Constants;


public class Utilities {
	public static String readFileIntoString(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis,
					Constants.ENCODING);
			BufferedReader in = new BufferedReader(isr);
			String text = "";
			String line = "";
			while ((line = in.readLine()) != null) {
				line = line.trim();
				text += line + Constants.UNIX_EOL;
			}
			in.close();
			isr.close();
			fis.close();
			return text.trim();
		} catch (Exception e) {
			return "";
		}
	}

	public static String escapeHTML(String trans) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String calcPercent(int count, int total) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void openURLInDefaultBrowser(String replace) {
		// TODO Auto-generated method stub
		
	}

	public static String readFileFromJarIntoString(String textHtmlPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void showErrorMessage(String string) {
		// TODO Auto-generated method stub
		
	}

	public static String leftTrim(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Vector<String> getTextFileList(File textDir) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String createRegExpFromWildCard(String currVocabWordFilter) {
		// TODO Auto-generated method stub
		return null;
	}
}
