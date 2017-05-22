package pl.edu.pw.elka.ner

import pl.edu.pw.elka.data.preprocessing.LabelValuePair
import spock.lang.Specification

import java.util.stream.Collectors

class NamedEntityLabelMapperTest extends Specification {

    def "entity found, but wasn't labeled"() {
        given:
            def entities = Arrays.asList("not labeled", "labeled person", "labeled date")
            def pairs = Arrays.asList(LabelValuePair.from("Person", "labeled person"),
                    LabelValuePair.from("Date", "labeled date"))
        when:
            def mappedPairs = new NamedEntityLabelMapper().mapToLabelValuePairs(entities, pairs)
        then:
            3 == mappedPairs.size()
    }

    def "not labeled entity gets label"() {
        given:
            def entities = Arrays.asList("other expected", "labeled person", "labeled date")
            def pairs = Arrays.asList(LabelValuePair.from("Person", "labeled person"),
                LabelValuePair.from("Date", "labeled date"))
        when:
            def mappedPairs = new NamedEntityLabelMapper().mapToLabelValuePairs(entities, pairs)
        then:
            1 == mappedPairs.stream()
                    .filter{ p -> p.label == "OTHER" }
                    .collect(Collectors.toList())
                    .size()
    }
}
