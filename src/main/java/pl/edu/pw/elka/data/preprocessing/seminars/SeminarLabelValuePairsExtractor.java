package pl.edu.pw.elka.data.preprocessing.seminars;

import gate.util.GateException;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import pl.edu.pw.elka.data.preprocessing.FilesLoader;
import pl.edu.pw.elka.data.preprocessing.LabelValuePair;
import pl.edu.pw.elka.data.preprocessing.LabelValuePairsExtractor;
import pl.edu.pw.elka.ner.NamedEntityRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class SeminarLabelValuePairsExtractor implements LabelValuePairsExtractor {

    private static final String UTF_8 = "UTF-8";
    private static final List<String> ENTITY_NAMES = Arrays.asList("Person", "Location", "Date");
    private static final String PUNCTUATION_MARKS_REGEX = "[,.;!?(){}\\[\\]<>%\"_\\-\\*]";
    private static final String SPACES_REGEX = "[ ]{2,}";

    @Override
    public List<LabelValuePair> extract(String data) {
        return Arrays.stream(SeminarLabel.values())
                .flatMap(tag -> Jsoup.parse(new SeminarHeaderTrimmer().trim(data))
                        .select(tag.tagName())
                        .stream()
                        .map(e -> LabelValuePair.from(tag.toString(), e.text()
                                .replaceAll(PUNCTUATION_MARKS_REGEX, "")
                                .replaceAll("\n", " ")
                                .replaceAll(SPACES_REGEX, " "))))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException, GateException {
        String seminarsDir = args[0];
        String cleanSeminarFile = args[1];
        String gatePath = args[2];
        String resultFile = args[3];
        LabelValuePairsExtractor extractor = new SeminarLabelValuePairsExtractor();
        List<LabelValuePair> pairs = new FilesLoader().loadFiles(seminarsDir)
                .stream()
                .map(f -> {
                    try {
                        return extractor.extract(FileUtils.readFileToString(f, UTF_8));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal seminar file");
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        String cleanSeminarFileText = FileUtils.readFileToString(new File(cleanSeminarFile), UTF_8);
        List<String> entities;
        try (NamedEntityRecognizer recognizer = new NamedEntityRecognizer(gatePath)) {
            entities = recognizer.find(cleanSeminarFileText, ENTITY_NAMES);
        } catch (Exception e) {
            throw new IllegalArgumentException("Gate framework issue, check path");
        }
        String result = new SeminarNamedEntityLabelMapper().mapToLabelValuePairs(entities, pairs)
                .stream()
                .map(p -> p.value.replaceAll("\n", " ") + "," + p.label)
                .collect(Collectors.joining("\n"));
        FileUtils.writeStringToFile(new File(resultFile), result, UTF_8);
    }
}
