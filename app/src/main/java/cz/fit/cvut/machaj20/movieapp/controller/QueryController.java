package cz.fit.cvut.machaj20.movieapp.controller;

import cz.fit.cvut.machaj20.movieapp.form.QueryForm;
import cz.fit.cvut.machaj20.movieapp.model.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class QueryController {

	private MovieDAO movieDAO;

	@Autowired
	public QueryController(MovieDAO movieDAO) {
		this.movieDAO = movieDAO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showResults(@ModelAttribute("query") QueryForm queryForm, BindingResult bindingResult, ModelMap model) {
		Map<String, String> filters = new HashMap<>();
		if (queryForm.getName() != null) filters.put("movieName", queryForm.getName());
		if (queryForm.getCompany() != null) filters.put("movieCompany", queryForm.getCompany());
		if (queryForm.getGenre() != null) filters.put("movieGenre", queryForm.getGenre());
		model.addAttribute("results", movieDAO.getMoviesByFilter(filters));
		model.addAttribute("command", queryForm);
		return "results";
	}

}
