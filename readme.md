Gate framework download and install - [link](https://gate.ac.uk/download/)

# Seminars

Seminars data - [link](https://people.cs.umass.edu/~mccallum/data/sa-tagged.tar.gz)

Classification:
1. TIME
2. SPEAKER
3. LOCATION

Clear seminars data:
```
./gradlew cleanSeminars -PseminarsDir="/example/dir" -PresultFile="/example/dir/clean.txt"
```

Make vectors using clean data using Glove:
Glove - [link](https://github.com/stanfordnlp/GloVe)

Extract entities with labels from seminar data:
```
./gradlew labelSeminars -PseminarsDir="/example/dir" -PcleanSeminarsFile="/example/dir/clean.txt" -PgatePath="/example/dir/gateframework" -PresultFile="/example/dir/labels.csv"
```

Vectorize seminars to csv:
```
 ./gradlew vectorize -PlabelsFile="/example/dir/labels.csv" -Pvectors="/example/dir/vectors.txt" -PresultFile="/example/dir/seminars.csv"
```

Classify using SVM:
```
./gradlew svmClassify -PcsvFile="/example/dir/seminars.csv"
```

# Conferences

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
```
./gradlew cleanConferences -PconferencesDir="/example/dir" -PcleanConferencesFile="/example/dir/cleanConferencesText.txt"
```

Make vectors using clean data using Glove:
Glove - [link](https://github.com/stanfordnlp/GloVe)

Extract entities with labels from conferences data:
```
./gradlew labelConferences -PconferencesDir="/example/dir" -PcleanConferencesFile="/example/dir/cleanConferencesText.txt" -PgatePath="/example/dir/gateframework" -PresultFile="/example/dir/labels.csv"
```

Vectorize conferences to csv:
```
 ./gradlew vectorize -PlabelsFile="/example/dir/labels.csv" -Pvectors="/example/dir/vectors.txt" -PresultFile="/example/dir/conferences.csv"
 ```
 
 Classify using SVM:
```
./gradlew svmClassify -PcsvFile="/example/dir/conferences.csv"
```
