package com.can.summarizer.model;

import java.util.List;

public class Document {
	
	private Sentence title;
	private List<Sentence> sentenceList;
	private boolean isRef=false;
	
	public Document(boolean isRef) {
		
	}
	
	/**
	 * @return the sentenceList
	 */
	public List<Sentence> getSentenceList() {
		return sentenceList;
	}

	/**
	 * @param sentenceList the sentenceList to set
	 */
	public void setSentenceList(List<Sentence> sentenceList) {
		this.sentenceList = sentenceList;
	}

	/**
	 * @return the title
	 */
	public Sentence getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(Sentence title) {
		this.title = title;
	}

	/**
	 * @return the isRef
	 */
	public boolean isRef() {
		return isRef;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<Sentence> sList = getSentenceList();
		StringBuffer stringBuffer=new StringBuffer();
		for (Sentence sentence : sList) {
			stringBuffer.append((sentence.getOriginalSentence()));
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}
	
	

}
