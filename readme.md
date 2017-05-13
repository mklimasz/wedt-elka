Gate framework download and install - [link](https://gate.ac.uk/download/)

Seminars data - [link](https://people.cs.umass.edu/~mccallum/data/sa-tagged.tar.gz)

Classification:
1. time
2. speaker
3. location

Clear seminars data:
./gradlew cleanSeminars -PseminarsDir="/example/dir" -PresultFile="/example/dir/clean.txt"

Extract entities with labels from seminar data:
./gradlew labelSeminars -PseminarsDir="/example/dir" -PcleanSeminarsFile="/example/dir/clean.txt" -PgatePath="/example/dir/gateframework" -PresultFile="/example/dir/labels.csv"




Conferences data - [link](http://ii.pw.edu.pl/%7Epandrusz/data/conferences/conferences-data-0.2.tgz)

Classification:
1. TIME
2. LOCATION
3. NAME
4. ABBREVIATION
5. SUBMISSION
6. NOTIFICATION
7. FINAL_VERSION_DUE

Clear conferences data:
./gradlew cleanConferences -PconferencesDir="/example/dir" -PcleanTextFile="/example/dir/cleanText.txt" -PlvFile="/example/dir/labelValuePairs.txt"
