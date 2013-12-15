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

	public Pair<Integer, List<Movie>> getMoviesByName(String name) {
		return executeQuery(
			"SELECT ?movieName (GROUP_CONCAT(distinct ?movieGenre ; separator = \",\") AS ?movieGenres) (GROUP_CONCAT(distinct ?movieYear ; separator = \",\") AS ?movieYears) WHERE {" +
				"	?movie a schema:Movie; " +
				"		schema:genre ?movieGenre; " +
				"		schema:copyrightYear ?movieYear; " +
				"		schema:name ?movieName . " +
				" 		FILTER(regex(?movieName, \"" + escape(name) + "\", \"i\"))" +
				"}" +
				"GROUP BY ?movieName",
			new MapCallback() {
				@Override
				public Pair<Integer, List<Movie>> execute(ResultSet resultSet) {
					List<Movie> movies = new ArrayList<>();
					while (resultSet.hasNext()) {
						QuerySolution row = resultSet.next();
						Movie movie = new Movie();
						movie.setName(row.get("movieName").asLiteral().getString());
						movie.setGenres(valueAsList(row, "movieGenres"));
						movie.setYears(valueAsList(row, "movieYears"));
						movies.add(movie);
					}
					return new Pair<>(resultSet.getRowNumber(), movies);
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
