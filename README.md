JTextGrid
================================================================================
A library for processing TextGrid files used by the [Praat phonetic analysis software](http://www.fon.hum.uva.nl/praat/) in Java.

* **Version:** 2014-02-15
* **Author:** Todd Shore
* **Website:** https://github.com/errantlinguist/jtextgrid
* **Licensing:** Copyright 2011--2014 Todd Shore. Licensed for distribution under the Apache License 2.0: See the files `NOTICE` and `LICENSE`.

Requirements
--------------------------------------------------------------------------------
- Java SE 7+
- [Guava 11+](http://code.google.com/p/guava-libraries/)
	
Instructions
--------------------------------------------------------------------------------
- **Building & creating a JAR for inclusion in other projects:** Run `ant` in the application home directory, specifying the directory where the above-listed third-party JAR files are located through setting the Ant property `lib.dir`, e.g.

		ant -Dlib.dir="/usr/share/java"
	for many UNIX/Linux distributions. If this property is not set, it defaults to looking for the JARs in the folder `lib` under the project folder; As an alternative to setting the property value above, the directory `lib` can be created and the required JARs can be placed underneath it.
	
	
- **Running a demo:** Run `ant demo`, setting the `lib.dir` property for third-party JARs as described above.

- **Generating Javadocs:** Run `ant javadocs`, setting the `lib.dir` property for third-party JARs as described above.
