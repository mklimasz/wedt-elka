package pl.edu.pw.elka.data.preprocessing.conferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;

import io.vavr.Tuple2;
import pl.edu.pw.elka.data.preprocessing.CleanTextExtractor;

public class ConferenceLabelValuePairsExtractor implements CleanTextExtractor {

	@Override
    public String extract(String data) {
		
		// Getting label-value pairs
    	List<Tuple2<ConferenceLabel, String>> extraction = Arrays.stream(ConferenceLabel.values())
                .flatMap(tag -> Jsoup.parse(data)
                        .select(tag.tagName())
                        .stream()
                        .map(e -> new Tuple2<>(tag, e.text())))
                .collect(Collectors.toList());
    	
    	// Removing duplicates (order kept)
    	List<Tuple2<ConferenceLabel, String>> list = new ArrayList<Tuple2<ConferenceLabel, String>>();
    	HashSet<Tuple2<ConferenceLabel, String>> lookup = new HashSet<Tuple2<ConferenceLabel, String>>();
    	for (Tuple2<ConferenceLabel, String> item : extraction) {
    	    if (lookup.add(item)) {
    	        list.add(item);
    	    }
    	}
    	extraction = list;
    	
    	// Printing output
    	String output = "";
    	for (Tuple2<ConferenceLabel, String> pair : extraction) {
			output = output + pair._1.tagName() + "\t" + pair._2 + "\n";
		}
    	
    	return output.replace(" ", "");
    	
    }
	
}
