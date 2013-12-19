package cz.fit.cvut.machaj20.movieapp.controller;

import cz.fit.cvut.machaj20.movieapp.form.QueryForm;
import cz.fit.cvut.machaj20.movieapp.model.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class DashboardController {

	private MovieDAO movieDAO;

	@Autowired
	public DashboardController(MovieDAO movieDAO) {
		this.movieDAO = movieDAO;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("moviesCount", movieDAO.getMoviesCount());
		model.addAttribute("command", new QueryForm());
		return "hello";
	}
}
