package cz.cvut.fit.machaj20.triplifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatasetsPreparer {

	private DvdParser dvdParser;
	private ImdbCacheParser imdbCacheParser;
	private TwitterRatingParser twitterRatingParser;

	@Autowired
	public DatasetsPreparer(DvdParser dvdParser, ImdbCacheParser imdbCacheParser, TwitterRatingParser twitterRatingParser) {
		this.dvdParser = dvdParser;
		this.imdbCacheParser = imdbCacheParser;
		this.twitterRatingParser = twitterRatingParser;
	}

	public Map<String, List<List<String>>> prepareData(String datasetDir) throws IOException {
		File dvdsFile = new File(datasetDir + "/dvd/Dvd.csv");
		File imdbFile = new File(datasetDir + "/imdb-ratings/top");
		File twitterMoviesFile = new File(datasetDir + "/twitter-ratings/movies.dat");
		File twitterRatingsFile = new File(datasetDir + "/twitter-ratings/ratings.dat");

		List<File> files = new ArrayList<>();
		files.add(dvdsFile);
		files.add(imdbFile);
		files.add(twitterMoviesFile);
		files.add(twitterRatingsFile);

		try {
			checkFileExistance(files);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		Map<String, List<List<String>>> prepared = new HashMap<>();
		prepared.put("dvd", dvdParser.parse(dvdsFile));
		prepared.put("imdb", imdbCacheParser.parse(imdbFile));
		prepared.put("twitter", twitterRatingParser.parse(twitterMoviesFile, twitterRatingsFile));

		return prepared;
	}

	private void checkFileExistance(List<File> files) throws FileNotFoundException {
		for (File file : files) {
			if (!file.exists()) {
				throw new FileNotFoundException("File " + file.getName() + " not exists");
			}
		}
	}
}
