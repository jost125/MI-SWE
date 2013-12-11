package cz.cvut.fit.machaj20.triplifier;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TwitterRatingParserTest {

	private TwitterRatingParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new TwitterRatingParser(new FileContentsGetter());
	}

	@Test
	public void testParse() throws Exception {
		File movies = new File("fixtures/twitter/movies.dat");
		File ratings = new File("fixtures/twitter/ratings.dat");
		List<List<String>> parsed = parser.parse(movies, ratings);
		assert parsed.size() == 13392;
		assert parsed.get(0).size() == 6;
		assert true;
	}
}
