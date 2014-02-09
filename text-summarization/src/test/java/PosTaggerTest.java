import static org.junit.Assert.*;

import org.junit.Test;

import com.can.pos.tagger.OpenNLPPosTagger;
import com.can.summarizer.model.WordType;


public class PosTaggerTest {

	@Test
	public void test() {
		OpenNLPPosTagger myTagger = OpenNLPPosTagger .getInstance("src\\main\\resources\\openNLP\\en-pos-maxent.bin");
		String[] output = myTagger.getPOSTags(new String[]{"this","house","is","red"});
		System.out.println(output);
	}

}
