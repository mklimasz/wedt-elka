package pl.edu.pw.elka.data.preprocessing;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

public class FilesLoader {

    public Collection<File> loadFiles(String path) {
        return FileUtils.listFiles(new File(path), null, true);
    }
    
    public Collection<File> loadFiles(String path, String[] extensions) {
        return FileUtils.listFiles(new File(path), extensions, true);
    }
}
