package com.can.word.synset;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ISynsetFinder;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

@Component("WordNetSynsetFinder")
public class WordNetSynsetFinder implements ISynsetFinder {

	@Autowired
	IDictionary dictionary;
	
	@Override
	public List<String> findHypernym(String word, String posTag) {
		List<String> hypernym=new ArrayList<String>();
		IIndexWord idxWord=null; 
		if(posTag.startsWith("VB")){
			idxWord = dictionary.getIndexWord (word, POS.VERB );
			if(idxWord==null){
				idxWord = dictionary.getIndexWord (word, POS.NOUN );
			}
		}
		else if(posTag.startsWith("NN")){
			idxWord = dictionary.getIndexWord (word, POS.NOUN );
			if(idxWord==null){
				idxWord = dictionary.getIndexWord (word, POS.VERB);
			}
		}else{
			return hypernym;
		}
		if(idxWord!=null){
			IWordID wordID = idxWord.getWordIDs().get(0);
			IWord dictWord = dictionary.getWord(wordID);
			ISynset synSets = dictWord.getSynset();
			for(ISynsetID iSynsetId : synSets.getRelatedSynsets(Pointer.HYPERNYM)) {
		        List<IWord> iWords = dictionary.getSynset(iSynsetId).getWords();
		        for(IWord iWord2: iWords) {
		          String lemma = iWord2.getLemma();
		          hypernym.add(lemma);
		        }
		    }
			return hypernym;
		}
		
		return hypernym;
		
	}
	public static void main(String args[]){
		String wnhome = "src\\main\\resources\\wordnet\\dict";
		String path = wnhome;
		URL url;
		IDictionary dict =null;
		try {
			url = new URL ( "file" , null , path );
			// construct the dictionary object and open it
			dict = new Dictionary ( url ) ;
			dict.open();	
		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println("IOException in word stemmer");
			System.out.println(e.getMessage());
		}
		IIndexWord idxWord = dict.getIndexWord("recording", POS.NOUN);
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		ISynset synset = word.getSynset();
		List<ISynsetID> synsetList = synset.getRelatedSynsets();
		List<IWord> iWords = dict.getSynset(synsetList.get(0)).getWords();
		
		
	}

}
