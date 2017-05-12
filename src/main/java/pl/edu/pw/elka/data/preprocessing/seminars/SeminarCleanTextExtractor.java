package pl.edu.pw.elka.data.preprocessing.seminars;

import org.apache.commons.io.FileUtils;
import pl.edu.pw.elka.data.preprocessing.CleanTextExtractor;
import pl.edu.pw.elka.data.preprocessing.FilesLoader;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public final class SeminarCleanTextExtractor implements CleanTextExtractor {

    private static final String PUNCTUATION_MARKS_REGEX = "[,.;!?(){}\\[\\]<>%\"_\\-\\*]";
    private static final String XML_TAGS_REGEX = "<.*?>";
    private static final String UTF_8 = "UTF-8";

    @Override
    public String extract(String data) {
        String xmlText = new SeminarHeaderTrimmer().trim(data);
        String noXmlText = xmlText.replaceAll(XML_TAGS_REGEX, "");
        return noXmlText.replaceAll(PUNCTUATION_MARKS_REGEX, "");
    }

    public static void main(String[] args) throws IOException {
        String seminarsDir = args[0];
        String resultFile = args[1];
        CleanTextExtractor extractor = new SeminarCleanTextExtractor();
        String seminarsTexts = new FilesLoader().loadFiles(seminarsDir)
                .stream()
                .map(f -> {
                    try {
                        return extractor.extract(FileUtils.readFileToString(f, UTF_8));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal seminar file");
                    }
                })
                .collect(Collectors.joining(" "));
        FileUtils.writeStringToFile(new File(resultFile), seminarsTexts, UTF_8);
    }
}
