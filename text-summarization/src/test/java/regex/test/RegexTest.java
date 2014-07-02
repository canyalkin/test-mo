package regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

public class RegexTest extends TestCase {

	private Pattern p;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		//"([\\w]\\.[\\w]\\.|[\\w].*[\\w]|[\\w])"
		//p = Pattern.compile( "([a-zA-Z0-9]\\.[a-zA-Z0-9]\\.|[a-zA-Z0-9].*[a-zA-Z0-9]|[a-zA-Z0-9])" );
		//p=Pattern.compile( "(\\w*?.*\\w)" );
		p=Pattern.compile( "(\\w+)" );
		super.setUp();
	}
	
	@Test
	public void testWordRegex(){
		Matcher matcher = p.matcher("``wI'm6t*");
		if(matcher.find()){
			assertEquals("wI'm6t", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testNonWordRegex(){
		Matcher matcher = p.matcher("^-*-");
		if(matcher.find()){
			assertEquals("", matcher.group(0));
		}
		else{
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testWordWithSpaceCharsRegex(){
		Matcher matcher = p.matcher("  `wI'm6t ");
		if(matcher.find()){
			assertEquals("wI'm6t", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testTwoCharsRegex(){
		Matcher matcher = p.matcher("ab");
		if(matcher.find()){
			assertEquals("ab", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	@Test
	public void testSingleCharRegex(){
		Matcher matcher = p.matcher("a");
		if(matcher.find()){
			assertEquals("a", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testAbbreviation(){
		Matcher matcher = p.matcher("u.s.");
		if(matcher.find()){
			assertEquals("u.s.", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	@Test
	public void testDotEnding(){
		Matcher matcher = p.matcher("uss.");
		if(matcher.find()){
			assertEquals("uss", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testThreeDotsEnding(){
		Matcher matcher = p.matcher("uss...");
		if(matcher.find()){
			assertEquals("uss", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	
	@Test
	public void testQuoteStarting(){
		Matcher matcher = p.matcher("``there");
		if(matcher.find()){
			assertEquals("there", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testQuoteEnding(){
		Matcher matcher = p.matcher("there``");
		if(matcher.find()){
			assertEquals("there", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testQuoteEndingWithComma(){
		Matcher matcher = p.matcher("there,``");
		if(matcher.find()){
			assertEquals("there", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	@Test
	public void testApostropheAtTheEnd(){
		Matcher matcher = p.matcher("indian'");
		if(matcher.find()){
			assertEquals("indian", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testStartWithUnderScore(){
		Matcher matcher = p.matcher("_indian");
		if(matcher.find()){
			assertEquals("indian", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testWordHasDash(){
		Matcher matcher = p.matcher("can-yalkin");
		if(matcher.find()){
			assertEquals("can-yalkin", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	@Test
	public void testWordStartsWithCapitalLetter(){
		Matcher matcher = p.matcher("Can");
		if(matcher.find()){
			assertEquals("Can", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
	@Test
	public void testWordHasUnderScoreInTheWord(){
		Matcher matcher = p.matcher("can_yalkin");
		if(matcher.find()){
			assertEquals("can_yalkin", matcher.group(0));
		}
		else{
			assertTrue(false);
		}
		
	}
	
}
