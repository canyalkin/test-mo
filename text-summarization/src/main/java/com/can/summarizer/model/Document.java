package com.can.summarizer.model;

import java.util.List;

public class Document {
	
	private Sentence title;
	private List<Sentence> sentenceList;
	private boolean isRef=false;
	private boolean hasPosTag=false;
	private StructuralProperties structuralProperties=null; 
	private boolean featureVectorCreated=false;
	
	
	public Document(boolean isRef) {
		this.isRef=isRef;
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

	public boolean isHasPosTag() {
		return hasPosTag;
	}

	public void setHasPosTag(boolean hasPosTag) {
		this.hasPosTag = hasPosTag;
	}

	public StructuralProperties getStructuralProperties() {
		return structuralProperties;
	}

	public void createStructuralProperties() {
		this.structuralProperties = new StructuralProperties(this);
	}

	

	public boolean isFeatureVectorCreated() {
		return featureVectorCreated;
	}

	public void setFeatureVectorCreated(boolean featureVectorCreated) {
		this.featureVectorCreated = featureVectorCreated;
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

	public void clear() {
		if(structuralProperties!=null)
			structuralProperties.clear();
		structuralProperties=null;
		for (Sentence sentence : sentenceList) {
			sentence.clear();
		}
		hasPosTag=false;
		
		
	}
	
	

}
