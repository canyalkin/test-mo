package com.can.word.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;


public class RegexWord {
	
	private static final Logger LOGGER = Logger.getLogger(RegexWord.class);
	
	public static String extractWordWithRespectToPattern(String curWord) {
		//"([a-zA-Z0-9]\\.[a-zA-Z0-9]\\.|[a-zA-Z0-9].*[a-zA-Z0-9]|[a-zA-Z0-9])"
		// \\w _ karakterini de içeriyor
		Pattern p = Pattern.compile( "([a-zA-Z0-9]\\.[a-zA-Z0-9]\\.|[a-zA-Z0-9].*[a-zA-Z0-9]|[a-zA-Z0-9])" );
		LOGGER.trace("initial curWord:>"+curWord+"<");
		Matcher matcher = p.matcher(curWord);
		if(matcher.find()){
			curWord=matcher.group(0);
		}
		else{
			curWord="";
		}
		LOGGER.trace("after pattern match curWord:>"+curWord+"<");
		return curWord;
	}

}
