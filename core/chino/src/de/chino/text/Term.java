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
	
	Term create(String s1, String s2, String s3, String s4, int i);
	
	Term INSTANCE = new TermImpl();

	String getKey();

	int getWordCount();

	Text getText();

	ArrayList<char[]> getBigram();

}

class TermImpl implements Term {

	@Override
	public String getTerm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSentence() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSentence(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRomanization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTranslation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String makeExportTemplateLine(String statusList,
			String exportTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Term create(String s1, String s2, String s3, String s4, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Text getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<char[]> getBigram() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
