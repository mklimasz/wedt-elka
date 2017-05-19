package pl.edu.pw.elka.word2vec;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;

public class WordVectorsProvider {

    public WordVectors load(File vectorsFile) {
        return WordVectorSerializer.readWord2VecModel(vectorsFile);
    }
}
