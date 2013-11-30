package ngram.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.summary.calculations.NGramCalculator;

public class NGramTest {

	private Sentence humanSentence;
	private Sentence humanSentence2;
	
	private Sentence systemSentence;
	private Sentence systemSentence2;
	
	List<Sentence> system;
	List<Sentence> human;
	
	@Before
	public void setUp()
	{
		String s="abcd efgh ghi wer olkt";
		humanSentence=createSentence(s);
		humanSentence2=createSentence(s);
		
		systemSentence=createSentence(s);
		systemSentence2=createSentence(s);
		
	}

	private Sentence createSentence(String s) {
		Sentence sentence=new Sentence(s);
		String[] words=s.split(" ");
		List<Word> word=new LinkedList<Word>();
		for (String string : words) {
			Word curWord=new Word(string);
			word.add(curWord);
		}
		sentence.setWords(word);
		return sentence;
	}
	
	private void prepareWordBasedNGram(Sentence sentence,int n){
		sentence.setNgramList(NGramCalculator.findNGram(n, sentence,RougeNType.wordBased));
	}
	
	private void prepareCharBasedNGram(Sentence sentence,int n){
		List<String> wordsAsStringList = sentence.getWordsAsStringList();
		StringBuffer buffer=new StringBuffer();
		for (String string : wordsAsStringList) {
			buffer.append(string);
		}
		List<String> ngramList=NGramCalculator.findNGram(n, createSentence(buffer.toString()),RougeNType.charBased);
		sentence.setNgramList(ngramList);
	}
	
	@Test
	public void testRougeNBasedOnWordNGram() {
		system=new LinkedList<Sentence>();
		prepareWordBasedNGram(systemSentence,3);
		system.add(systemSentence);
		prepareWordBasedNGram(systemSentence2,3);
		system.add(systemSentence2);
		
		human=new LinkedList<Sentence>();
		prepareWordBasedNGram(humanSentence,3);
		human.add(humanSentence);
		prepareWordBasedNGram(humanSentence2,3);
		human.add(humanSentence2);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(human, system);
		Double result = rougeNCalculator.calculateRougeN(3);
		
		assertEquals((Double)1.0, result);
	}

		
	@Test
	public void testWord3GramTwoWords(){
		String s="can can";
		Sentence sentence=createSentence(s);
		LinkedList<String> expectesList=new LinkedList<String>();

		expectesList.add("can");
		expectesList.add("can");
		expectesList.add("cancan");
		
		assertEquals(expectesList, NGramCalculator.findNGram(3, sentence,RougeNType.wordBased));
		
		
	}
	
	@Test
	public void testWord2GramTwoWords(){
		String s="can can";
		Sentence sentence=createSentence(s);
		LinkedList<String> expectesList=new LinkedList<String>();

		expectesList.add("can");
		expectesList.add("can");
		expectesList.add("cancan");
		
		assertEquals(expectesList, NGramCalculator.findNGram(2, sentence,RougeNType.wordBased));
		
		
	}

	@Test
	public void testWord1GramTwoWords(){
		String s="can can";
		Sentence sentence=createSentence(s);
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("can");
		expectesList.add("can");
		assertEquals(expectesList, NGramCalculator.findNGram(1, sentence,RougeNType.wordBased));
		
		
	}
	
	@Test
	public void testWord1GramNoWords(){
		String s="";
		Sentence sentence=createSentence(s);
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("");
		assertEquals(expectesList, NGramCalculator.findNGram(1, sentence,RougeNType.wordBased));
	}
	
	@Test
	public void testWord2GramNoWords(){
		String s="";
		Sentence sentence=createSentence(s);
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("");
		assertEquals(expectesList, NGramCalculator.findNGram(2, sentence,RougeNType.wordBased));
	}
	
		
	@Test
	public void testChar6Gram(){
		String s="to_be";
		LinkedList<String> expectesList=new LinkedList<String>();

		expectesList.add("t");
		expectesList.add("o");
		expectesList.add("_");
		expectesList.add("b");
		expectesList.add("e");
		expectesList.add("to");
		expectesList.add("o_");
		expectesList.add("_b");
		expectesList.add("be");
		expectesList.add("to_");
		expectesList.add("o_b");
		expectesList.add("_be");
		expectesList.add("to_b");
		expectesList.add("o_be");
		expectesList.add("to_be");
		
		assertEquals(expectesList, NGramCalculator.findNGram(6, createSentence(s),RougeNType.charBased));
		
		
	}
	
	@Test
	public void testChar5Gram(){
		String s="to_be";
		LinkedList<String> expectesList=new LinkedList<String>();

		expectesList.add("t");
		expectesList.add("o");
		expectesList.add("_");
		expectesList.add("b");
		expectesList.add("e");
		expectesList.add("to");
		expectesList.add("o_");
		expectesList.add("_b");
		expectesList.add("be");
		expectesList.add("to_");
		expectesList.add("o_b");
		expectesList.add("_be");
		expectesList.add("to_b");
		expectesList.add("o_be");
		expectesList.add("to_be");
		
		assertEquals(expectesList, NGramCalculator.findNGram(5, createSentence(s),RougeNType.charBased));
		
		
	}
	
	
	@Test
	public void testChar3Gram(){
		String s="to_be";
		LinkedList<String> expectesList=new LinkedList<String>();

		expectesList.add("t");
		expectesList.add("o");
		expectesList.add("_");
		expectesList.add("b");
		expectesList.add("e");
		expectesList.add("to");
		expectesList.add("o_");
		expectesList.add("_b");
		expectesList.add("be");
		expectesList.add("to_");
		expectesList.add("o_b");
		expectesList.add("_be");
		
		assertEquals(expectesList, NGramCalculator.findNGram(3, createSentence(s),RougeNType.charBased));
		
		
	}
	
	@Test
	public void testChar2Gram(){
		String s="to_be";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("t");
		expectesList.add("o");
		expectesList.add("_");
		expectesList.add("b");
		expectesList.add("e");
		expectesList.add("to");
		expectesList.add("o_");
		expectesList.add("_b");
		expectesList.add("be");
		assertEquals(expectesList, NGramCalculator.findNGram(2,createSentence(s),RougeNType.charBased));
	}
	
	@Test
	public void testChar1Gram(){
		String s="to_be";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("t");
		expectesList.add("o");
		expectesList.add("_");
		expectesList.add("b");
		expectesList.add("e");
		assertEquals(expectesList, NGramCalculator.findNGram(1,createSentence(s),RougeNType.charBased));
	}
	
	@Test
	public void testChar3GramOneCharacter(){
		String s="t";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("t");
		assertEquals(expectesList, NGramCalculator.findNGram(3, createSentence(s),RougeNType.charBased));
	}
	
	@Test
	public void testChar2GramOneCharacter(){
		String s="t";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("t");
		assertEquals(expectesList, NGramCalculator.findNGram(2, createSentence(s),RougeNType.charBased));
	}
	
	
	@Test
	public void testChar1GramOneCharacter(){
		String s="t";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("t");
		assertEquals(expectesList, NGramCalculator.findNGram(1,createSentence(s),RougeNType.charBased));
	}
	
	@Test
	public void testCharNGramNoCharacter(){
		String s="";
		LinkedList<String> expectesList=new LinkedList<String>();
		expectesList.add("");
		assertTrue( NGramCalculator.findNGram(3,createSentence(s),RougeNType.charBased).isEmpty());
	}
	
	@Test
	public void testRougeNWordNGram() {
		system=new LinkedList<Sentence>();
		prepareWordBasedNGram(systemSentence,3);
		system.add(systemSentence);
		prepareWordBasedNGram(systemSentence2,3);
		system.add(systemSentence2);
		
		human=new LinkedList<Sentence>();
		prepareWordBasedNGram(humanSentence,3);
		human.add(humanSentence);
		prepareWordBasedNGram(humanSentence2,3);
		human.add(humanSentence2);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(human, system);
		Double result = rougeNCalculator.calculateRougeN(3);
		
		assertEquals((Double)1.0, result);
	}
	
	@Test
	public void testRougeNWordNGram2() {
		
		String s="pulses may ease schizophrenic voices";
		Sentence candidate = createSentence(s);//system (candidate)
		
		String s2="magnetic pulse series sent through brain may ease schizophrenic voices";
		Sentence refSentence1 = createSentence(s2);//ref (human)
		
		String s3="yale finds magnetic stimulation some relief to schizophrenics imaginary voices";
		Sentence refSentence2 = createSentence(s3);//ref (human)
		
		
		system=new LinkedList<Sentence>();
		prepareWordBasedNGram(candidate,1);
		system.add(candidate);
		
		human=new LinkedList<Sentence>();
		
		prepareWordBasedNGram(refSentence1, 1);
		human.add(refSentence1);
		
		prepareWordBasedNGram(refSentence2,1);
		human.add(refSentence2);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(human,system);
		Double result = rougeNCalculator.calculateRougeN(1);
		
		assertEquals((Double)0.25, result);
	}
	
	
	@Test
	public void testRouge1WordNGram() {
		
		String s="pulses may ease schizophrenic voices";
		Sentence candidate = createSentence(s);//system (candidate)
		
		String s2="magnetic pulse series sent through brain may ease schizophrenic voices";
		Sentence refSentence1 = createSentence(s2);//ref (human)
		
		String s3="yale finds magnetic stimulation some relief to schizophrenics imaginary voices";
		Sentence refSentence2 = createSentence(s3);//ref (human)
		
		
		system=new LinkedList<Sentence>();
		prepareWordBasedNGram(candidate,1);
		system.add(candidate);
		
		human=new LinkedList<Sentence>();
		
		prepareWordBasedNGram(refSentence1, 1);
		human.add(refSentence1);
		
		prepareWordBasedNGram(refSentence2,1);
		human.add(refSentence2);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(human,system);
		Double result = rougeNCalculator.calculateRougeN(1);
		
		assertEquals((Double)0.2105, result);
	}
	
	
	@Test
	public void testRouge1WordNGramRealTest() {
		
		String s="typhoon yunya packing 80 mph winds slammed eastern coast luzon island includes mount pinatubo early day";
		Sentence candidate1 = createSentence(s);//system (candidate)
		system=new LinkedList<Sentence>();
		prepareWordBasedNGram(candidate1,1);
		system.add(candidate1);
		
		Sentence candidate2 = createSentence("uickly weakened tropical storm 60 mph winds time centered about 70 miles east volcano");
		prepareWordBasedNGram(candidate2,1);
		system.add(candidate2);
		
		Sentence candidate3 = createSentence("u.s. military began flying home hundreds 28,000 americans crowded subic bay naval base bizarre tropical blizzard thick volcanic ash caused power failures across base friday during third day increasingly violent eruptions mount pinatubo");
		prepareWordBasedNGram(candidate3,1);
		system.add(candidate3);
		
		Sentence candidate4 = createSentence("friday eight thunderous explosions shot cloud ash steam nearly 19 miles high");
		prepareWordBasedNGram(candidate4,1);
		system.add(candidate4);
		
		Sentence candidate5 = createSentence("friday's eruptions hurled ash over clark 10 miles east volcano subic 25 miles southwest clark personnel temporarily housed");
		prepareWordBasedNGram(candidate5,1);
		system.add(candidate5);
		
		Sentence candidate6 = createSentence("gray ash fell  manila 60 miles south");
		prepareWordBasedNGram(candidate6,1);
		system.add(candidate6);
		
		Sentence candidate7 = createSentence("subic base spokesman robert coble said generators shut down from time time clean out volcanic ash plunging base darkness");
		prepareWordBasedNGram(candidate7,1);
		system.add(candidate7);
		
		
		String ref1="five explosions shook mt";
		Sentence refSentence1 = createSentence(ref1);//ref (human)
		human=new LinkedList<Sentence>();
		
		prepareWordBasedNGram(refSentence1, 1);
		human.add(refSentence1);
		
		String ref2="pinatubo friday spreading ash clark far away manila";
		Sentence refSentence2 = createSentence(ref2);//ref (human)
		prepareWordBasedNGram(refSentence2,1);
		human.add(refSentence2);
		
		Sentence refSentence3 = createSentence("friday's blasts part same eruption largest pinatubo awoke sunday 600-year slumber");//ref (human)
		prepareWordBasedNGram(refSentence3,1);
		human.add(refSentence3);
		
		Sentence refSentence4 = createSentence("typhoon yunya packing 60 mph winds hit east coast luzon includes mt");//ref (human)
		prepareWordBasedNGram(refSentence4,1);
		human.add(refSentence4);
		
		Sentence refSentence5 = createSentence("pinatubo quickly weakened tropical storm");//ref (human)
		prepareWordBasedNGram(refSentence5,1);
		human.add(refSentence5);
		
		Sentence refSentence6 = createSentence("u.s. military began flying home 28,000 americans crowded subic bay naval base");//ref (human)
		prepareWordBasedNGram(refSentence6,1);
		human.add(refSentence6);
		
		Sentence refSentence7 = createSentence("philippine president aquino dismissed fabrication report warned volcanic damage case nuclear incident clark");//ref (human)
		prepareWordBasedNGram(refSentence7,1);
		human.add(refSentence7);
		
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(human,system);
		Double result = rougeNCalculator.calculateRougeN(1);
		System.out.println(result);
		assertEquals((Double)0.2105, result);
	}
	
}
