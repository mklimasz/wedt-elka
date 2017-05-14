package pl.edu.pw.elka.data.preprocessing.conferences;

public enum ConferenceLabel {
	
    TIME("when"), 
    LOCATION("wher"), 
    NAME("cname"), 
    ABBREVIATION("abbre"),
    SUBMISSION("subm"), 
    NOTIFICATION("notf"),
    FINAL_VERSION_DUE("finv");

    private String tagName;

    ConferenceLabel(String tagName) {
        this.tagName = tagName;
    }

    public String tagName() {
        return tagName;
    }
    
}
