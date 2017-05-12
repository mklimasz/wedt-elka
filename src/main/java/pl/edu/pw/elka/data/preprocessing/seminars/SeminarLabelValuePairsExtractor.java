package pl.edu.pw.elka.data.preprocessing.seminars;

import io.vavr.Tuple2;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SeminarLabelValuePairsExtractor {

    public List<Tuple2<SeminarLabel, String>> extract(String data) {
        return Arrays.stream(SeminarLabel.values())
                .flatMap(tag -> Jsoup.parse(new SeminarHeaderTrimmer().trim(data))
                        .select(tag.tagName())
                        .stream()
                        .map(e -> new Tuple2<>(tag, e.text())))
                .collect(Collectors.toList());
    }
}
