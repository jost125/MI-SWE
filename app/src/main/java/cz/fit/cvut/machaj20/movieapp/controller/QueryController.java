package cz.fit.cvut.machaj20.movieapp.controller;

import cz.fit.cvut.machaj20.movieapp.form.QueryForm;
import cz.fit.cvut.machaj20.movieapp.model.MovieDAO;
import cz.fit.cvut.machaj20.movieapp.model.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
		Pair pair = movieDAO.getMoviesByName(queryForm.getName());
		model.addAttribute("numberOfResults", pair.getKey());
		model.addAttribute("results", pair.getValue());
		model.addAttribute("command", queryForm);
		return "results";
	}

}
