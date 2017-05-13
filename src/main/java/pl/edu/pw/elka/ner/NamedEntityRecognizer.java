package pl.edu.pw.elka.ner;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.Utils;
import gate.creole.ANNIEConstants;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class NamedEntityRecognizer implements AutoCloseable {

    private static final String CORPUS_NAME = "corpus";
    private LanguageAnalyser controller;

    public NamedEntityRecognizer(String gatePath) throws GateException, IOException {
        Gate.setGateHome(new File(gatePath));
        Gate.init();
        controller = (LanguageAnalyser) PersistenceManager
                .loadObjectFromFile(new File(new File(Gate.getPluginsHome(),
                        ANNIEConstants.PLUGIN_DIR), ANNIEConstants.DEFAULT_FILE));
    }


    public List<String> find(String doc, List<String> entityNames) throws ResourceInstantiationException,
            ExecutionException {
        Document document = Factory.newDocument(doc);
        Corpus corpus = Factory.newCorpus(CORPUS_NAME);
        corpus.add(document);
        controller.setCorpus(corpus);
        controller.execute();
        List<String> entities = extractEntities(entityNames, document);
        Factory.deleteResource(document);
        Factory.deleteResource(corpus);
        return entities;
    }

    private List<String> extractEntities(List<String> entityNames, Document document) {
        return document.getAnnotations()
                .get(new HashSet<>(entityNames))
                .stream()
                .map(e -> Utils.stringFor(document, e))
                .collect(Collectors.toList());
    }

    @Override
    public void close() throws Exception {
        Factory.deleteResource(controller);
    }
}
