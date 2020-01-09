package input_webpages_file_output_csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Executioner {

	private static final String INPUT_DIR = "input_output/input.csv";
	private static final String OUTPUT_DIR = "input_output/output.csv";

	public static void main(String[] args) {
		
		List<String> input = getInputFromCSV();
		String output = getLinksFromWebPageLinks(input);
		extractOutputToCSV(output);
		System.out.print("done");

	}

	private static List<String> getInputFromCSV() {
		List<String> input = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(INPUT_DIR))) {
			String line;
			while ((line = br.readLine()) != null) {
				input.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found, please check directory: " + INPUT_DIR);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return input;
		
	}

	private static String getLinksFromWebPageLinks(List<String> webpageLinks) {

		StringBuilder output = new StringBuilder();

		for (String webpageLink : webpageLinks) {
			try {
				Document doc = Jsoup.parse(new URL(webpageLink), 2000);
				Elements resultLinks = doc.select("a");
				for (Element link : resultLinks) {
					String href = link.attr("href");
					if (filterLinkCondition(href)) {
						output.append(href);
					} else {
						if(href.startsWith("/")) {
							output.append(webpageLink+href);
						} else {
							output.append(webpageLink+"/"+href);
						}
					}
					output.append("\n");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output.toString();
	}

	private static boolean filterLinkCondition(String href) {
		return href.startsWith("http");
	}

	private static void extractOutputToCSV(String output) {
		try (PrintWriter writer = new PrintWriter(new File(OUTPUT_DIR))) {
			writer.write(output);
		} catch (FileNotFoundException e) {
			System.out.println("File not found, please check directory: " + OUTPUT_DIR);
		}
	}

}
