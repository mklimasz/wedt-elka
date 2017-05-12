package pl.edu.pw.elka.data.preprocessing.seminars;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class SeminarHeaderTrimmer {

    private static final String SPLIT_WORD = "Abstract:";

    String trim(String data) {
        String[] textParts = data.split(SPLIT_WORD);
        return IntStream.range(1, textParts.length)
                .mapToObj(i -> textParts[i])
                .collect(Collectors.joining(" "));
    }
}
