package pl.edu.pw.elka.data.preprocessing

import spock.lang.Specification

class FilesLoaderTest extends Specification {

    def "test files loader"() {
        given:
            def dir = File.createTempDir()
            File.createTempFile("tmpFile", null, dir)
        when:
            def files = new FilesLoader().loadFiles(dir.absolutePath)
        then:
            1 == files.size()
    }

    def "test recursive files loader"() {
        given:
            def dir = File.createTempDir()
            File.createTempFile("tmpFile", null, dir)
            def subDir =  new File(dir.absolutePath + File.separator + "subdir")
            subDir.mkdir()
            File.createTempFile("subdirTmpFile", null, subDir)
        when:
            def files = new FilesLoader().loadFiles(dir.absolutePath)
        then:
            2 == files.size()
    }
}
