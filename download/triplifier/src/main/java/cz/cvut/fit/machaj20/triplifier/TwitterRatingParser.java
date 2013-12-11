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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TwitterRatingParser {

	private FileContentsGetter fileContentsGetter;

	@Autowired
	public TwitterRatingParser(FileContentsGetter fileContentsGetter) {
		this.fileContentsGetter = fileContentsGetter;
	}

	public List<List<String>> parse(File movies, File ratings) throws IOException {
		List<List<String>> parsedMovies = parseMovies(movies);
		List<List<String>> parsedRatings = parseRatings(ratings);
		Map<String, List<Double>> groupedRatings = groupRatingsByMovies(parsedRatings);

		return addRatings(parsedMovies, groupedRatings);
	}

	private List<List<String>> addRatings(List<List<String>> parsedMovies, Map<String, List<Double>> groupedRatings) {
		for (List<String> line : parsedMovies) {
			String movieId = line.get(0);
			List<Double> ratingsForMovie = groupedRatings.get(movieId);
			int numberOfRatings = ratingsForMovie.size();

			Double sum = 0.0;
			for (Double rank : ratingsForMovie) {
				sum += rank;
			}
			line.add(String.valueOf(sum / numberOfRatings));
			line.add(String.valueOf(numberOfRatings));
		}

		return parsedMovies;
	}

	private List<List<String>> parseRatings(File ratings) throws IOException {
		List<List<String>> parsed = new ArrayList<>();
		String content = fileContentsGetter.get(ratings);
		for (String line : content.split("\n")) {
			String regex="^(?<userId>.+?)::(?<movieId>.+?)::(?<rating>.+?)::(?<ratingTimestamp>.+?)$";
			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(line);

			if(!matcher.find()) {
				throw new InvalidFormatException();
			}

			List<String> entry = new ArrayList<>();
			entry.add(matcher.group("userId"));
			entry.add(matcher.group("movieId"));
			entry.add(matcher.group("rating"));
			entry.add(matcher.group("ratingTimestamp"));
			parsed.add(entry);
		}

		return parsed;
	}

	private Map<String,List<Double>> groupRatingsByMovies(List<List<String>> parsedRatings) {
		Map<String, List<Double>> groupedRatings = new HashMap<>();
		for (List<String> line: parsedRatings) {
			String movieId = line.get(1);
			String rating = line.get(2);
			if (!groupedRatings.containsKey(movieId)) {
				groupedRatings.put(movieId, new ArrayList<Double>());
			}

			groupedRatings.get(movieId).add(Double.parseDouble(rating));
		}

		return groupedRatings;
	}

	private List<List<String>> parseMovies(File movies) throws IOException {
		List<List<String>> parsed = new ArrayList<>();
		String content = fileContentsGetter.get(movies);
		for (String line : content.split("\n")) {
			String regex="^(?<movieId>.+?)::(?<movie>.+?) \\((?<year>.+?)\\)::(?<genres>.*)$";
			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(line);

			if(!matcher.find()) {
				throw new InvalidFormatException();
			}

			List<String> entry = new ArrayList<>();
			entry.add(matcher.group("movieId"));
			entry.add(matcher.group("movie"));
			entry.add(matcher.group("year"));
			entry.add(matcher.group("genres"));
			parsed.add(entry);
		}

		return parsed;
	}

}
