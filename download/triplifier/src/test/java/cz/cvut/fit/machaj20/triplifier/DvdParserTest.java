package cz.cvut.fit.machaj20.triplifier;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DvdParserTest {

	private DvdParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new DvdParser(new FileContentsGetter());
	}

	@Test
	public void testParse() throws Exception {
		File file = new File("fixtures/dvd/Dvd.csv");
		List<List<String>> parsed = parser.parse(file);
		assert parsed.size() == 253562;
		assert parsed.get(0).size() == 15;
	}
}
