# Gredit - Gregorian Music Editor

Gredit is a Graphical-User-Interface used to manually correct the scan results of the included [OMR](https://gitlab2.informatik.uni-wuerzburg.de/softwarepraktikum/automatic-omr-using-opencv) library.

## Installation

To build the program following dependencies are required:
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
* [Maven](https://maven.apache.org/download.cgi)


To install the program follow these steps:

### 1. Clone the project
```
git clone https://gitlab2.informatik.uni-wuerzburg.de/softwarepraktikum/SWP18SS_OMR-Postcorrection.git && cd SWP18SS_OMR-Postcorrection/
```

### 2. Build with Maven
```    
mvn package
```
or
```
mvn clean package
```
to make sure it is build smoothly 

### Move the jar to where you want it to be
```
mv target/gredit-1.0.0-SNAPSHOT-jar-with-dependencies.jar.jar PATH/gredit.jar
```

## Usage
```
cd PATH
java -jar gredit.jar
``` 
This will start the GUI from which new Projects can be created by opening a PNG or JPG picture or old Projects can be further edited by loading a YAML file.