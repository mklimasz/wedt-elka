package pl.edu.pw.elka.data.preprocessing.conferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

import gate.util.GateException;
import pl.edu.pw.elka.data.preprocessing.CleanTextExtractor;
import pl.edu.pw.elka.data.preprocessing.FilesLoader;
import pl.edu.pw.elka.data.preprocessing.LabelValuePair;
import pl.edu.pw.elka.data.preprocessing.LabelValuePairsExtractor;
import pl.edu.pw.elka.ner.NamedEntityLabelMapper;
import pl.edu.pw.elka.ner.NamedEntityRecognizer;

public class ConferenceLabelValuePairsExtractor implements LabelValuePairsExtractor {
	
    private static final String UTF_8 = "UTF-8";
    private static final List<String> ENTITY_NAMES = Arrays.asList("Person", "Location", "Date");
	
	@Override
    public List<LabelValuePair> extract(String data) {
		// Extract all label-value pairs
        List<LabelValuePair> extraction = Arrays.stream(ConferenceLabel.values())
                .flatMap(tag -> Jsoup.parse(data)
                        .select(tag.tagName())
                        .stream()
                        .map(e -> LabelValuePair.from(tag.toString(), e.text())))
                .collect(Collectors.toList());
        // Remove duplicates
        List<LabelValuePair> helpList = new ArrayList<LabelValuePair>();
        HashSet<String> lookup = new HashSet<String>();
        for (LabelValuePair pair : extraction) {
			if(lookup.add(pair.label + pair.value.toLowerCase()))
				helpList.add(pair);
		}
        // Merge sibling rows with the same label
        CleanTextExtractor cleanTextExtractor = new ConferenceCleanTextExtractor();
        if(helpList.size() > 0) {
        	String currentValue = helpList.get(0).value;
        	extraction.clear();
        	for (int i = 1; i < helpList.size(); i++) {
        		if(helpList.get(i).label == helpList.get(i-1).label) {
        			currentValue = currentValue + " " + helpList.get(i).value;
        		}
        		else {
        			extraction.add(LabelValuePair.from(helpList.get(i-1).label, cleanTextExtractor.extract(currentValue)));
        			currentValue = helpList.get(i).value;
        		}
        	}
			extraction.add(LabelValuePair.from(helpList.get(helpList.size()-1).label,  cleanTextExtractor.extract(currentValue)));
        }
        
        return extraction;
    }
    	
	public static void main(String[] args) throws IOException, GateException {
		
		// input
        String conferencesDir = args[0];
        String cleanConferencesFile = args[1];
        String gatePath = args[2];
        // output
        String resultFile = args[3];
        
        // extractor object
        LabelValuePairsExtractor extractor = new ConferenceLabelValuePairsExtractor();
        
        // getting list of conferences labels
        List<LabelValuePair> pairs = new FilesLoader().loadFiles(conferencesDir, new String[]{"html"})
                .stream()
                .map(f -> {
                    try {
                        return extractor.extract(FileUtils.readFileToString(f, UTF_8));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal conference file");
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        
        // removing dupplicates
        List<LabelValuePair> helpList = new ArrayList<LabelValuePair>();
        HashSet<String> lookup = new HashSet<String>();
        for (LabelValuePair pair : pairs) {
			if(lookup.add(pair.label + pair.value.toLowerCase()))
				helpList.add(pair);
		}
        pairs = helpList;
        
        
        // clean text file
        String cleanConferenceFileText = FileUtils.readFileToString(new File(cleanConferencesFile), UTF_8);
        
        // getting GATE entities
        List<String> entities;
        try (NamedEntityRecognizer recognizer = new NamedEntityRecognizer(gatePath)) {
            entities = recognizer.find(cleanConferenceFileText, ENTITY_NAMES);
        } catch (Exception e) {
            throw new IllegalArgumentException("Gate framework issue, check path");
        }
        
        // printing result
        String result = new NamedEntityLabelMapper().mapToLabelValuePairs(entities, pairs)
                .stream()
                .map(p -> p.value + "," + p.label)
                .collect(Collectors.joining("\n"));
        FileUtils.writeStringToFile(new File(resultFile), result, UTF_8);
    }
	
}
