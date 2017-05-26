package pl.edu.pw.elka.data.preprocessing.conferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

import pl.edu.pw.elka.data.preprocessing.CleanTextExtractor;
import pl.edu.pw.elka.data.preprocessing.FilesLoader;
import pl.edu.pw.elka.data.preprocessing.LabelValuePair;

public class ConferenceCleanTextExtractor implements CleanTextExtractor {

    private static final String PUNCTUATION_MARKS_REGEX = "[,.;!?(){}\\[\\]<>%\"_\\-\\*]";
    private static final String XML_TAGS_REGEX = "<.*?>";
    private static final String SPACES_REGEX = "[ ]{2,}";
    private static final String UTF_8 = "UTF-8";
	
	@Override
	public String extract(String data) {
		
		// HTML clearing
		String noHtmlText = Jsoup.parse(data).text();
		// Regex clearing
        noHtmlText = noHtmlText.replaceAll(XML_TAGS_REGEX, "");
        noHtmlText = noHtmlText.replaceAll(PUNCTUATION_MARKS_REGEX, "");
        noHtmlText = noHtmlText.replace("\n", "");
        noHtmlText = noHtmlText.replaceAll(SPACES_REGEX, " ");
        return noHtmlText;
        
	}
	
    public static void main(String[] args) throws IOException {
    	
    	// input
        String conferencesDir = args[0];
        
        // output
        String resultFile1 = args[1];
        
        // Extractor
        CleanTextExtractor cleanTextExtractor = new ConferenceCleanTextExtractor();
        
        // HTML clearing
        String noHtmlText = new FilesLoader().loadFiles(conferencesDir, new String[]{"html"})
                .stream()
                .map(f -> {
                    try {
                    	if(!f.getName().contains("0.html")) return "";
                        return cleanTextExtractor.extract(FileUtils.readFileToString(f, UTF_8));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal conference file");
                    }
                })
                .collect(Collectors.joining(" "));

        // Printing output
        FileUtils.writeStringToFile(new File(resultFile1), noHtmlText, UTF_8);
        
    }
	
}
