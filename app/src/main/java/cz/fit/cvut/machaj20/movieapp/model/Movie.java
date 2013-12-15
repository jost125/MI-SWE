package cz.fit.cvut.machaj20.movieapp.model;

import java.util.List;

public class Movie {

	private String name;
	private List<String> genres;
	private List<String> years;

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
}
