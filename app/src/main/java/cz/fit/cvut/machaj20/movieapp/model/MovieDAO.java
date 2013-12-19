package cz.fit.cvut.machaj20.movieapp.model;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MovieDAO {

	private Model model;
	private Map<String, String> prefixes;

	public MovieDAO(Model model, Map<String, String> prefixes) {
		this.prefixes = prefixes;
		this.model = model;
	}

	public Integer getMoviesCount() {
		return executeQuery(
			"SELECT (COUNT(DISTINCT ?name) as ?moviesCount) WHERE {" +
			"	?movie a schema:Movie; " +
			"		schema:name ?name" +
			"}",
			new MapCallback() {
				@Override
				public Integer execute(ResultSet resultSet) {
					QuerySolution row = resultSet.next();
					return row.get("moviesCount").asLiteral().getInt();
				}
			}
		);
	}

	public List<Movie> getMoviesByFilter(Map<String, String> filters) {
		StringBuilder filterQuery = new StringBuilder();
		for (String column : filters.keySet()) {
			String value = filters.get(column);
			switch (column) {
				case "movieName":
					filterQuery.append(" 		FILTER(regex(?movieName, \"").append(escape(value)).append("\", \"i\")) .");
					break;
				case "movieGenre":
					filterQuery.append(" 		FILTER(regex(?movieGenre, \"").append(escape(value)).append("\", \"i\")) .");
					break;
				case "movieCompany":
					filterQuery.append(" 		FILTER(regex(?movieCompany, \"").append(escape(value)).append("\", \"i\")) .");
					break;
			}
		}
		return executeQuery(
			"SELECT ?movieNode ?movieName ?movieUrl ?movieCompany ?movieRating ?dbpediaUrl (GROUP_CONCAT(distinct ?movieGenre ; separator = \",\") AS ?movieGenres) (GROUP_CONCAT(distinct ?movieYear ; separator = \",\") AS ?movieYears) WHERE {" +
				"	?movieNode a schema:Movie; " +
				"		schema:name ?movieName . " +
				filterQuery.toString() +
				"		OPTIONAL { ?movieNode schema:productionCompany ?movieCompany } . " +
				"		OPTIONAL { ?movieNode schema:genre ?movieGenre } . " +
				"		OPTIONAL { ?movieNode schema:url ?movieUrl } . " +
				"		OPTIONAL { ?movieNode schema:copyrightYear ?movieYear } . " +
				"		OPTIONAL { ?movieNode schema:aggregateRating [ schema:ratingValue ?movieRating ] } . " +
				"		OPTIONAL { ?movieNode owl:sameAs ?dbpediaUrl } . " +
				"}" +
				"GROUP BY ?movieNode ?movieName ?movieUrl ?movieCompany ?movieRating ?dbpediaUrl " +
				"ORDER BY ?movieRating",
			new MapCallback() {
				@Override
				public List<Movie> execute(ResultSet resultSet) {
					List<Movie> movies = new ArrayList<>();
					while (resultSet.hasNext()) {
						QuerySolution row = resultSet.next();
						Movie movie = new Movie();
						if (row.get("movieNode") != null) movie.setNode(row.get("movieNode").asResource().getURI());
						if (row.get("movieName") != null) movie.setName(row.get("movieName").asLiteral().getString());
						if (row.get("movieUrl") != null) movie.setUrl(row.get("movieUrl").asResource().getURI());
						if (row.get("movieCompany") != null) movie.setCompany(row.get("movieCompany").asLiteral().getString());
						if (row.get("movieRating") != null) movie.setRating(String.valueOf((double)Math.round(row.get("movieRating").asLiteral().getDouble() * 100) / 100.0));
						if (row.get("dbpediaUrl") != null) movie.setDbpediaUrl(row.get("dbpediaUrl").asResource().getURI());
						movie.setGenres(valueAsList(row, "movieGenres"));
						movie.setYears(valueAsList(row, "movieYears"));

						if (movie.getName() != null) {
							movies.add(movie);
						}
					}
					return movies;
				}
			}
		);
	}

	private <MappedResult> MappedResult executeQuery(String queryString, MapCallback callback) {
		StringBuilder sb = new StringBuilder();
		for (String prefix : prefixes.keySet()) {
			String ns = prefixes.get(prefix);
			sb.append("PREFIX ").append(prefix).append(": <").append(ns).append(">\n");
		}
		sb.append(queryString);

		Query query = QueryFactory.create(sb.toString());

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		MappedResult mappedResult = callback.execute(results);

		// Important – free up resources used running the query
		qe.close();

		return mappedResult;
	}

	private String escape(String value) {
		return value.replace("\"", "\\\"");
	}

	private List<String> valueAsList(QuerySolution row, String columnName) {
		List<String> list = new ArrayList<>();
		if (row.get(columnName) != null) {
			list.addAll(Arrays.asList(row.get(columnName).asLiteral().getString().split(",")));
		}

		return list;
	}
}
