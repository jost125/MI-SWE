package cz.cvut.fit.machaj20.triplifier;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Triplifier {
	private String nsMovie = "http://schema.org/Movie#";
	private String nsPerson = "http://schema.org/Person#";
	private String nsAggregateRating = "http://schema.org/AggregateRating#";
	private String nsActor = "http://schema.org/actor";
	private String nsName = "http://schema.org/name";
	private String nsCopyrightYear = "http://schema.org/copyrightYear";
	private String nsUrl = "http://schema.org/url";
	private String nsRating = "http://schema.org/aggregateRating";
	private String nsRatingValue = "http://schema.org/ratingValue";
	private String nsGenre = "http://schema.org/genre";
	private String nsProductionCompany = "http://schema.org/productionCompany";
	private String nsRdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private String nsRdfs = "http://www.w3.org/2000/01/rdf-schema#";
	private String nsSchema = "http://schema.org/";

	private Property url;
	private Property actor;
	private Property name;
	private Property year;
	private Property rating;
	private Property genre;
	private Property productionCompany;
	private Property ratingValue;
	private Property rdfType;

	private Model model;
	private Map<String, Integer> movieIndex = new HashMap<>();
	private Map<String, Integer> movieActorIndex = new HashMap<>();

	public void triplify(Map<String, List<List<String>>> preparedData, String outputPath) throws IOException {
		triplifyImdb(preparedData, outputPath);
		triplifyDvd(preparedData, outputPath);
		triplifyTwitterRating(preparedData, outputPath);
	}

	private void triplifyImdb(Map<String, List<List<String>>> preparedData, String outputPath) throws IOException {
		initModel();
		for (List<String> line : preparedData.get("imdb")) {
			String movieUrl = line.get(0);
			String movieActors = line.get(1);
			String movieName = line.get(2);
			String movieYear = line.get(3);

			Resource movie = createMovieSubject(String.valueOf(getIndexForMovie(movieName)));

			for (String movieActor : movieActors.split(",")) {
				String movieActorName = movieActor.trim();
				Resource person = createPerson(String.valueOf(getIndexForMovieActor(movieActor)));
				model.add(person, name, movieActorName);
				model.add(person, rdfType, model.createResource(nsSchema + "Person"));
				model.add(movie, actor, person);
			}

			model.add(movie, name, movieName);
			model.add(movie, url, createUrl(movieUrl));
			model.add(movie, year, movieYear);
			model.add(movie, rdfType, model.createResource(nsSchema + "Movie"));
		}
		writeModel(outputPath, "imdb.ttl");
	}

	private void triplifyDvd(Map<String, List<List<String>>> preparedData, String outputPath) throws IOException {
		initModel();
		for (List<String> line : preparedData.get("dvd")) {
			String movieName = line.get(0);
			String movieStudio = line.get(1);
			String moviePrice = line.get(6);
			String movieYear = line.get(8);
			String movieGenres = line.get(9);
			String movieUrl = "http://dvdlist.kazart.com/queryDVDList2.php3?id=" + line.get(13);

			Resource movie = createMovieSubject(String.valueOf(getIndexForMovie(movieName)));

			for (String movieGenre : movieGenres.split("/")) {
				String movieGenreName = movieGenre.trim();
				model.add(movie, genre, movieGenreName);
			}

			model.add(movie, name, movieName);
			model.add(movie, url, createUrl(movieUrl));
			model.add(movie, year, movieYear);
			model.add(movie, productionCompany, movieStudio);
			model.add(movie, rdfType, model.createResource(nsSchema + "Movie"));
		}
		writeModel(outputPath, "dvd.ttl");
	}

	private void triplifyTwitterRating(Map<String, List<List<String>>> preparedData, String outputPath) throws IOException {
		initModel();
		for (List<String> line : preparedData.get("twitter")) {
			String movieName = line.get(1);
			String movieYear = line.get(2);
			String movieGenres = line.get(3);
			String movieRating = line.get(4);

			Resource movie = createMovieSubject(String.valueOf(getIndexForMovie(movieName)));

			for (String movieGenre : movieGenres.split("\\|")) {
				String movieGenreName = movieGenre.trim();
				model.add(movie, genre, movieGenreName);
			}

			model.add(movie, name, movieName);
			model.add(movie, year, movieYear);

			Resource aggregateRating = createAggregateRatingSubject(getIndexForMovie(movieName));
			model.add(aggregateRating, ratingValue, movieRating);
			model.add(movie, rating, aggregateRating);
			model.add(movie, rdfType, model.createResource(nsSchema + "Movie"));
		}


		writeModel(outputPath, "twitter.ttl");
	}

	private void writeModel(String outputPath, String outName) throws IOException {
		File outputFile = new File(outputPath + "/" + outName);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			model.write(bos, "Turtle");
		}
	}

	public Integer getIndexForMovie(String movieName) {
		if (!movieIndex.containsKey(movieName)) {
			movieIndex.put(movieName, movieIndex.size() + 1);
		}
		return movieIndex.get(movieName);
	}

	public Integer getIndexForMovieActor(String movieActor) {
		if (!movieActorIndex.containsKey(movieActor)) {
			movieActorIndex.put(movieActor, movieActorIndex.size() + 1);
		}
		return movieActorIndex.get(movieActor);
	}

	private Resource createAggregateRatingSubject(Integer indexForMovie) {
		return model.createResource(nsAggregateRating + indexForMovie);
	}

	private Resource createMovieSubject(String movie) {
		return model.createResource(nsMovie + movie);
	}

	private Resource createUrl(String url) {
		return model.createResource(url);
	}

	private Resource createPerson(String person) {
		return model.createResource(nsPerson + person);
	}

	private void initModel() {
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("movie", nsMovie);
		model.setNsPrefix("playedBy", nsActor);
		model.setNsPrefix("person", nsPerson);
		model.setNsPrefix("aggregateRating", nsAggregateRating);
		model.setNsPrefix("url", nsUrl);
		model.setNsPrefix("genre", nsGenre);
		model.setNsPrefix("rating", nsRating);
		model.setNsPrefix("name", nsName);
		model.setNsPrefix("year", nsCopyrightYear);
		model.setNsPrefix("productionCompany", nsProductionCompany);
		model.setNsPrefix("rdf", nsRdf);
		model.setNsPrefix("rdfs", nsRdfs);
		model.setNsPrefix("schema", nsSchema);
		model.setNsPrefix("ratingValue", nsRatingValue);

		url = createPredicate(nsUrl);
		actor = createPredicate(nsActor);
		name = createPredicate(nsName);
		year = createPredicate(nsCopyrightYear);
		rating = createPredicate(nsRating);
		genre = createPredicate(nsGenre);
		productionCompany = createPredicate(nsProductionCompany);
		ratingValue = createPredicate(nsRatingValue);
		rdfType = createPredicate(nsRdf + "type");
	}

	private Property createPredicate(String predicate) {
		return model.createProperty(predicate);
	}

}
