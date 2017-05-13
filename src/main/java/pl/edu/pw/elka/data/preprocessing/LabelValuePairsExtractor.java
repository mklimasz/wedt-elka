package pl.edu.pw.elka.data.preprocessing;

import java.util.List;

public interface LabelValuePairsExtractor {
    List<LabelValuePair> extract(String data);
}
