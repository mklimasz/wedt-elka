package pl.edu.pw.elka.svm;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

public class SvmPredict {

    public static void main(String[] args) throws Exception {
        LibSVM libSVM = new LibSVM();
        Instances data = prepareData(args[0]);
        libSVM.buildClassifier(data);
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(libSVM, data, 4, new Debug.Random(1));
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.toMatrixString());
    }

    private static Instances prepareData(String csvPath) throws Exception {
        File dataFile = new File(csvPath);
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(dataFile);
        Instances data = csvLoader.getDataSet();
        data.setClassIndex(1);
        Remove remove = new Remove();
        remove.setAttributeIndices("1");
        remove.setInputFormat(data);
        return Filter.useFilter(data, remove);
    }
}
