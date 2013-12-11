package cz.cvut.fit.machaj20.triplifier;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;

@Component
public class FileContentsGetter {

	public String get(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
		}

		return sb.toString();
	}

}
