package com.can.summarizer.interfaces;

import java.util.List;

public interface ISynsetFinder {
	
	List<String> findHypernym(String word, String posTag);

}
