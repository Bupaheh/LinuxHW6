# DOC

This program outputs a sequence of responses from three substitution algorithms (FIFO, LRU, and OPT) based on a given sequence of accesses to the process pages and compares them by the number of responses of the second type ([Details](README.md))

##### Input data

Input data must be in text file in the following format:

```
<process data size> <RAM size>
<page request> <page request> ... <page request>
```

To use batch processing, each scheme must be separated using "-":

```
7 4
1 2 3 4 1 5 1 3 4 6 2 3 7
-
5 3
1 2 3 3 1 4 5 3 1 2 4
```

Examples of input data can be found in data/

##### Output data

The output data will be output in "output.txt" in the directory with jar file or in the root directory of the project.

##### How to run on Linux

Enter this in the terminal to run the jar file:

```bash
$ java -jar [JAR FILE] [TEXT FILE]
```

Jar file can be found in build/libs/
