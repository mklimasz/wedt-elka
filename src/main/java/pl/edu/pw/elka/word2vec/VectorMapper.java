package pl.edu.pw.elka.word2vec;

import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.writer.impl.csv.CSVRecordWriter;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Text;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VectorMapper {

    private final WordVectors wordVectors;
    private static final long DEFAULT_VECTOR_SEED = 987654;
    private static double[] DEFAULT_VECTOR;

    public VectorMapper(WordVectors wordVectors) {
        this.wordVectors = wordVectors;
    }

    public double[] map(String[] entity) {
        return Arrays.stream(entity)
                .filter(s -> s.length() > 0)
                .map(this::getWordVector)
                .reduce(INDArray::add)
                .map(a -> a.data().asDouble())
                .orElseThrow(() -> new IllegalArgumentException("Provide non empty array"));
    }

    private INDArray getWordVector(String s) {
        return Nd4j.create(Optional.ofNullable(wordVectors.getWordVector(s))
                .orElseGet(() -> {
                    System.out.println("Couldn't find vector for word " + s);
                    return DEFAULT_VECTOR;
                }));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(args[1]));
        DEFAULT_VECTOR = getDefaultVector(wordVectors.lookupTable().layerSize());
        VectorMapper mapper = new VectorMapper(wordVectors);
        CSVRecordReader csvRecordReader = new CSVRecordReader(0, ",");
        File labels = new File(args[0]);
        csvRecordReader.initialize(new FileSplit(new File(labels.getParent(), labels.getName())));
        CSVRecordWriter csvRecordWriter = new CSVRecordWriter(new File(args[2]));
        while (csvRecordReader.hasNext()) {
            List<Writable> line = csvRecordReader.next();
            String name = line.get(0).toString();
            String className = "";
            if(line.size() > 1)
            	className = line.get(1).toString();
            double[] vector = mapper.map(name.split("[ ]"));
            List<Writable> result = new ArrayList<>();
            result.add(new Text("\"" + name + "\""));
            result.add(new Text(className));
            Arrays.stream(vector)
                    .forEach(el -> result.add(new DoubleWritable(el)));
            csvRecordWriter.write(result);
        }
        csvRecordReader.close();
        csvRecordWriter.close();
    }
    
    private static double[] getDefaultVector(int length)
    {
    	double[] vec = new double[length];
    	Random rand = new Random(DEFAULT_VECTOR_SEED);
    	for(int i = 0; i < vec.length; i++)
    	{
    		vec[i] = -4 + 8 * rand.nextDouble();
    	}
    	return vec;
    }
}
