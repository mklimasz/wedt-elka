package pl.edu.pw.elka.data.preprocessing.conferences;

import pl.edu.pw.elka.data.preprocessing.LabelValuePair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ConferenceNamedEntityLabelMapper {

    private static final String OTHER_LABEL = "OTHER";
    private static final String SPACE = "[ ]";

    List<LabelValuePair> mapToLabelValuePairs(List<String> entities, List<LabelValuePair> labelValuePairs) {
        List<LabelValuePair> result = mapTime(entities, labelValuePairs);
        result.addAll(mapLocations(entities, labelValuePairs));
        result.addAll(mapAbbreviations(entities, labelValuePairs));
        result.addAll(mapFinalversions(entities, labelValuePairs));
        result.addAll(mapNames(entities, labelValuePairs));
        result.addAll(mapSubmissions(entities, labelValuePairs));
        result.addAll(mapNotifications(entities, labelValuePairs));
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
                        .filter(p -> p.label.equals(ConferenceLabel.TIME.toString()))
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
                        .filter(p -> p.label.equals(ConferenceLabel.LOCATION.toString()))
                        .filter(p -> p.value.toLowerCase().equals(e.toLowerCase()))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
    
    private List<LabelValuePair> mapAbbreviations(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(ConferenceLabel.ABBREVIATION.toString()))
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
    
    private List<LabelValuePair> mapFinalversions(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(ConferenceLabel.FINAL_VERSION_DUE.toString()))
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
    
    private List<LabelValuePair> mapNames(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(ConferenceLabel.NAME.toString()))
                        .filter(p -> {
                            long count = Arrays.stream(e.split(SPACE))
                                    .filter(eV -> Arrays.asList(p.value.toLowerCase().split(SPACE)).contains(eV.toLowerCase()))
                                    .count();
                            return count >= 2;
                        })
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
    
    private List<LabelValuePair> mapSubmissions(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(ConferenceLabel.SUBMISSION.toString()))
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
    
    private List<LabelValuePair> mapNotifications(List<String> entities, List<LabelValuePair> labelValuePairs) {
        return entities.stream()
                .map(e -> labelValuePairs.stream()
                        .filter(p -> p.label.equals(ConferenceLabel.NOTIFICATION.toString()))
                        .filter(p -> p.value.equals(e))
                        .findAny()
                        .map(p -> LabelValuePair.from(p.label, e))
                        .orElse(LabelValuePair.from(OTHER_LABEL, e)))
                .filter(p -> !p.label.equals(OTHER_LABEL))
                .collect(Collectors.toList());
    }
}

