package com.can.summarizer.model;

public class Word {
		String word;
	WordType wordType=WordType.none;
	
	public Word() {
	}
	
	public Word(String word) {
		super();
		this.word = word;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public WordType getWordType() {
		return wordType;
	}
	public void setWordType(WordType wordType) {
		this.wordType = wordType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Words [word=" + word
				+ ", wordType=" + wordType + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		result = prime * result
				+ ((wordType == null) ? 0 : wordType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (wordType != other.wordType)
			return false;
		return true;
	}

	
	
	
	
}
