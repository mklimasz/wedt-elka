package pl.edu.pw.elka.data.preprocessing;

public final class LabelValuePair {

    public final String label;
    public final String value;

    private LabelValuePair(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static LabelValuePair from(String label, String value) {
        return new LabelValuePair(label, value);
    }
}
