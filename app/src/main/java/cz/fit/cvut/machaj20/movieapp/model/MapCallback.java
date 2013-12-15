package cz.fit.cvut.machaj20.movieapp.model;

import com.hp.hpl.jena.query.ResultSet;

public interface MapCallback {

	public <MappedResult> MappedResult execute(ResultSet resultSet);

}
