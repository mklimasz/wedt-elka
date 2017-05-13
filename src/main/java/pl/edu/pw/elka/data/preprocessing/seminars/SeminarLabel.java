package pl.edu.pw.elka.data.preprocessing.seminars;

enum SeminarLabel {
    TIME("stime"), SPEAKER("speaker"), LOCATION("location");

    private String tagName;

    SeminarLabel(String tagName) {
        this.tagName = tagName;
    }

    public String tagName() {
        return tagName;
    }
}
