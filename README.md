JavaParser and Maven sample
---

This fully working sample Maven project parses and generates code with [JavaParser](http://www.javaparser.org).

This sample is targeted at people without [Maven](https://maven.apache.org/) experience.

To build it, you will need to download and unpack the latest (or recent) version of Maven (https://maven.apache.org/download.cgi)
and put the `mvn` command on your path.

Then, you will need to install a Java 1.8 (or higher) JDK (not JRE!), and make sure you can run `java` from the command line.

If required, install git. If you haven't already done so, clone this sample repository with `git clone https://github.com/javaparser/javaparser-maven-sample.git`.

Change to the folder of where this sample project was installed (where the pom.xml file is located).
Now run `mvn clean install` and Maven will compile your project, 
and put the results into two jar files in the `target` directory.

You can now run the sample from the command line with
`java -jar target/javaparser-maven-sample-1.0-SNAPSHOT-shaded.jar`.
This runs the sample program, LogicPositivizer, which reads, parses, and modifies the code in resources/Blabla.java and then writes the modified file with the same name to the output folder.

To better understand this sample, you can read [JavaParser: Visited.](https://leanpub.com/javaparservisited) In this book, key contributors to the JavaParser library teach you how you can use JavaParser to programmatically analyse, transform and generate your java code base.

How you run this code is up to you, but usually you would start by using an IDE like [NetBeans](https://netbeans.org/), [Intellij IDEA](https://www.jetbrains.com/idea/), or [Eclipse](https://eclipse.org/ide/).

The Maven dependencies may lag behind the official releases a bit.

If you notice some problems with this setup, please open an issue.
