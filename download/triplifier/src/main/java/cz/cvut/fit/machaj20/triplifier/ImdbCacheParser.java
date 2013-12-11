package cz.cvut.fit.machaj20.triplifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImdbCacheParser {

	private FileContentsGetter fileContentsGetter;

	@Autowired
	public ImdbCacheParser(FileContentsGetter fileContentsGetter) {
		this.fileContentsGetter = fileContentsGetter;
	}

	public List<List<String>> parse(File imdbFile) throws IOException {
		List<List<String>> parsed = new ArrayList<>();

		String content = fileContentsGetter.get(imdbFile);
		String regex="<td class=\"posterColumn\".+?href=\"(?<url>.*?)\".*?" +
			"<td class=\"titleColumn\".+?title=\"(?<actors>.+?)\".*?>" +
			"(?<movie>.*?)</a>.*?<span class=\"secondaryInfo\">\\((?<year>.+?)\\).*?" +
			"<td class=\"ratingColumn\".*?title=\"(?<ratingWithInfo>.+?)\".*?>(?<rating>.*?)<";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);

		while(matcher.find()){
			List<String> entry = new ArrayList<>();
			entry.add("http://www.imdb.com/" + matcher.group("url"));
			entry.add(matcher.group("actors"));
			entry.add(matcher.group("movie"));
			entry.add(matcher.group("year"));
			entry.add(matcher.group("ratingWithInfo"));
			entry.add(matcher.group("rating"));
			parsed.add(entry);
		}

		return parsed;
	}

}
