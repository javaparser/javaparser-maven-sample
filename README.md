JavaParser and Maven sample
---

A fully working sample Maven project that parses and generates code with [JavaParser](http://www.javaparser.org)

This is targeted at people without [Maven](https://maven.apache.org/) experience.

To build it, you will need to download and unpack the latest (or recent) version of Maven (https://maven.apache.org/download.cgi)
and put the `mvn` command on your path.
Then, you will need to install a Java 1.8 (or higher) JDK (not JRE!), and make sure you can run `java` from the command line.
Now you can run `mvn clean install` and Maven will compile your project, 
an put the results it in two jar files in the `target` directory.
If you like to run from the command line,
execute `java -jar target/javaparser-maven-sample-1.0-SNAPSHOT-shaded.jar`.

How you run this code is up to you, but usually you would start by using an IDE like [NetBeans](https://netbeans.org/), [Intellij IDEA](https://www.jetbrains.com/idea/), or [Eclipse](https://eclipse.org/ide/).

The Maven dependencies may lag behind the official releases a bit.

If you notice some problems with this setup, please open an issue.
