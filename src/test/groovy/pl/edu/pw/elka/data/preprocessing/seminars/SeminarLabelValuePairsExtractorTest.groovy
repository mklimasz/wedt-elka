package pl.edu.pw.elka.data.preprocessing.seminars

import spock.lang.Specification

class SeminarLabelValuePairsExtractorTest extends Specification {

    def "real seminar data contains two tags"() {
        given:
            def seminarData = this.getClass().getResource("/test_seminar_data.txt").text
        when:
            def labelsValuesPairs = new SeminarLabelValuePairsExtractor().extract(seminarData)
        then:
            3 == labelsValuesPairs.size()
    }
}
