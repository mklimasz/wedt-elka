package pl.edu.pw.elka.data.preprocessing.seminars;

import pl.edu.pw.elka.data.preprocessing.LabelValuePair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class SeminarNamedEntityLabelMapper {

    private static final String OTHER_LABEL = "OTHER";
    private static final String SPACE = "[ ]";

    List<LabelValuePair> mapToLabelValuePairs(List<String> entities, List<LabelValuePair> labelValuePairs) {
        List<LabelValuePair> result = mapSpeakers(entities, labelValuePairs);
        result.addAll(mapLocations(entities, labelValuePairs));
        result.addAll(mapTime(entities, labelValuePairs));
        result.addAll(entities.stream()
                .filter(e -> !result.stream()
                        .map(p -> p.value)
                        .anyMatch(v -> v.equals(e)))
                .map(e -> LabelValuePair.from(OTHER_LABEL, e))
                .collect(Collectors.toList()));
        return result;
    }

    private List<LabelValuePair> mapTime(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(SeminarLabel.TIME.toString()))
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }

    private List<LabelValuePair> mapLocations(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(SeminarLabel.LOCATION.toString()))
                        .filter(p -> Arrays.stream(e.split(SPACE))
                                .anyMatch(eV -> Arrays.asList(p.value.split(SPACE)).contains(eV)))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }

    private List<LabelValuePair> mapSpeakers(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(SeminarLabel.SPEAKER.toString()))
                        .filter(p -> {
                            long count = Arrays.stream(e.split(SPACE))
                                    .filter(eV -> Arrays.asList(p.value.split(SPACE)).contains(eV))
                                    .count();
                            return count >= e.split(SPACE).length - 1 && count >= 1;
                        })
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
}

