package cz.cvut.fit.machaj20.triplifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DvdParser {

	private FileContentsGetter fileContentsGetter;

	@Autowired
	public DvdParser(FileContentsGetter fileContentsGetter) {
		this.fileContentsGetter = fileContentsGetter;
	}

	public List<List<String>> parse(File dvds) throws IOException {
		List<List<String>> parsed = new ArrayList<>();
		String content = fileContentsGetter.get(dvds);

		Integer lineNumber = 0;
		for (String line : content.split("\n")) {
			String[] columns = line.split("\\|");
			if (lineNumber++ % 50 != 0) { // Dataset is too big, so lets pick only some of them
				continue;
			}

			List<String> entry = new ArrayList<>();
			entry.add(columns[0]); // dvd title
			entry.add(columns[1]); // studio
			entry.add(columns[2]); // released
			entry.add(columns[3]); // status
			entry.add(columns[4]); // sound
			entry.add(columns[5]); // versions
			entry.add(columns[6]); // price
			entry.add(columns[7]); // rating
			entry.add(columns[8]); // year
			entry.add(columns[9]); // genre
			entry.add(columns[10]); // aspect
			entry.add(columns[11]); // UPC
			entry.add(columns[12]); // dvd release date
			entry.add(columns[13]); // id
			entry.add(columns[14]); // timestamp
			parsed.add(entry);
		}

		return parsed;
	}

}
