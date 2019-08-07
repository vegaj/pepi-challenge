# The Workshop P.E.P.I. Coding Challenge
#### José Miguel Vega Moreno

This is my implementation to the _PEPI Challenge_ that you can find [here](https://github.com/The-Workshop-Inventors-of-play/pepi-challenge). It is a command line application that is developed on Java10. 

## Dependencies

In order to be able to run this project, you will need:
* [__Java 10__](https://jdk.java.net/12/) (or newer)
* [__Maven__](https://maven.apache.org/download.cgi)

## Build instructions

1) Clone this repository: `git clone https://github.com/vegaj/poker-travels.git` or download and extract the zip.
2) Navigate with a terminal to the directory that contains the **_pom.xml_** file.
3) Execute the command: `mvn install`

_**NOTE:**
Make sure that your environment variable **JAVA_HOME** is pointing to your **java10+** installation directory or else the build will fail._

## Run 

In the same directory run:
```
java -jar target/poker-travels-{version}-jar-with-dependencies.jar <input_file.json> <max_days>
```
Where __<input_file.json>__ is a JSON file containing the cities, the awards and the connection between cities and their costs; and __<max_days>__ is the maxium number of days the travel can last (this is an integer between 2 and 7 inclusive).

