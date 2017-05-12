package pl.edu.pw.elka.data.preprocessing.seminars

import spock.lang.Specification

class SeminarCleanTextExtractorTest extends Specification {

    def "real seminar data test"() {
        given:
            def seminarData = this.getClass().getResource("/test_seminar_data.txt").text
        when:
            def cleanSeminarData = new SeminarCleanTextExtractor().extract(seminarData)
        then:
            cleanSeminarData.equals(this.getClass().getResource("/clean_test_seminar_data.txt").text)
    }
}
