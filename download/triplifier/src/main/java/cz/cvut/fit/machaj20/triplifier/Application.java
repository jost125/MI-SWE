package cz.cvut.fit.machaj20.triplifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class Application {

	private DatasetsPreparer datasetsPreparer;
	private Triplifier triplifier;

	@Autowired
	public Application(DatasetsPreparer datasetsPreparer, Triplifier triplifier) {
		this.datasetsPreparer = datasetsPreparer;
		this.triplifier = triplifier;
	}

	public void run(String datasetsPath, String outputPath) {
		File outputFile = new File(outputPath + "/triplified.ttl");
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			triplifier.triplify(datasetsPreparer.prepareData(datasetsPath), bos);
			System.out.println("triplified in file " + outputFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
}
