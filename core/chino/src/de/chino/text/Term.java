package de.chino.text;

import java.util.ArrayList;

public interface Term {

	String getTerm();

	String getSentence();

	TermStatus getStatus();

	void setSentence(String s);

	String getRomanization();

	String getTranslation();

	String makeExportTemplateLine(String statusList, String exportTemplate);
	
	String getKey();

	int getWordCount();

	Text getText();

	ArrayList<char[]> getBigram();

}

