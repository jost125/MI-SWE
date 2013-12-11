package cz.cvut.fit.machaj20.triplifier;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ImdbCacheParserTest {

	private ImdbCacheParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new ImdbCacheParser(new FileContentsGetter());
	}

	@Test
	public void testParse() throws Exception {
		File file = new File("fixtures/imdb/top");
		List<List<String>> parsed = parser.parse(file);
		assert parsed.size() == 250;
		assert parsed.get(0).size() == 6;
	}
}
