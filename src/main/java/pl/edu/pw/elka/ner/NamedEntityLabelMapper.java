package pl.edu.pw.elka.ner;

import pl.edu.pw.elka.data.preprocessing.LabelValuePair;

import java.util.List;
import java.util.stream.Collectors;

public final class NamedEntityLabelMapper {

    private static final String OTHER_LABEL = "OTHER";

    public List<LabelValuePair> mapToLabelValuePairs(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .collect(Collectors.toList());
    }
}
