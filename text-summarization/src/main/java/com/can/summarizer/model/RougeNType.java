package com.can.summarizer.model;

public enum RougeNType {
	charBased("charBased"), wordBased("wordBased");

	private String text;
	private RougeNType(String text) {
		this.text=text;
	}

	public String getText() {
		return this.text;
	}

	public static  RougeNType getFromValue(String value){
		RougeNType resp = charBased;
		if(value.equals("char")||value.equals("charBased")){
			resp=charBased;
		}else if(value.equals("word")||value.equals("wordBased")){
			resp=wordBased;
		}
		return resp;
	}
}
