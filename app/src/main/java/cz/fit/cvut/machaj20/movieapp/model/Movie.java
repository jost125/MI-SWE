package cz.fit.cvut.machaj20.movieapp.model;

import java.util.List;

public class Movie {

	enum URL_TYPE {
		DVD,
		IMDB
	}

	private String node;
	private String url;
	private URL_TYPE urlType;
	private String name;
	private String company;
	private String rating;
	private String dbpediaUrl;
	private List<String> genres;
	private List<String> years;

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		this.urlType = url.startsWith("http://dvdlist") ? URL_TYPE.DVD : URL_TYPE.IMDB;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDbpediaUrl() {
		return dbpediaUrl;
	}

	public void setDbpediaUrl(String dbpediaUrl) {
		this.dbpediaUrl = dbpediaUrl;
	}

	public URL_TYPE getUrlType() {
		return urlType;
	}
}
