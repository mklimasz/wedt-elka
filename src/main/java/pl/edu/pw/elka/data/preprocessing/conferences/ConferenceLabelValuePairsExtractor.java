package pl.edu.pw.elka.data.preprocessing.conferences;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

import gate.util.GateException;
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
        return Arrays.stream(ConferenceLabel.values())
                .flatMap(tag -> Jsoup.parse(data)
                        .select(tag.tagName())
                        .stream()
                        .map(e -> LabelValuePair.from(tag.toString(), e.text())))
                .collect(Collectors.toList());
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
        List<LabelValuePair> pairs = new FilesLoader().loadFiles(conferencesDir)
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
