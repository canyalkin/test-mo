package com.can.summarizer.model;

import java.util.List;

public class Document {
	
	Sentence title;
	List<Sentence> sentenceList;
	boolean isRef=false;
	
	public Document(List<Sentence> sentenceList) {
		super();
		this.sentenceList = sentenceList;
	}

	public Document() {
		// TODO Auto-generated constructor stub
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

	/**
	 * @param isRef the isRef to set
	 */
	public void setRef(boolean isRef) {
		this.isRef = isRef;
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
