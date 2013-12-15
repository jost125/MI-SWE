package cz.fit.cvut.machaj20.movieapp.model;

import com.hp.hpl.jena.rdf.model.Model;

import java.util.Map;

public class ModelFactory {

	private Map<String, String> datasources;

	public ModelFactory(Map<String, String> datasources) {
		this.datasources = datasources;
	}

	public Model createModel() {
		Model model = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();
		for (String datasourcePath : datasources.keySet()) {
			String datasourceFormat = datasources.get(datasourcePath);
			model.read(datasourcePath, datasourceFormat);
		}
		return model;
	}

}
