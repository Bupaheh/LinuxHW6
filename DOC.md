# DOC

This program outputs a sequence of responses from three substitution algorithms (FIFO, LRU, and OPT) based on a given sequence of accesses to the process pages and compares them by the number of responses of the second type ([Details](README.md))

##### Input data

Input data must be in text file in the following format:

```
<process data size> <RAM size>
<page request> <page request> ... <page request>
```

Example:

```
7 4
1 2 3 4 3 4 4 7 5 4 6 1 2
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

For each substitution algorithm project outputs the number of the RAM frame to replace for the next page required by the process (zero means that page currently in RAM). Then outputs number of replacements for each algorithm.

Example of output data:

```
fifo: 1 2 3 4 0 1 2 0 0 3 4 1 2
lru: 1 2 3 4 0 2 0 0 0 2 1 0 4
opt: 1 2 3 4 0 2 0 0 0 2 4 0 2
fifoCount: 10
lruCount: 8
optCount: 8
```

The output data will be in "output.txt" in the directory with jar file or in the root directory of the project. 

##### How to run project using gradle

Enter this in the terminal to run the project:

```bash
$ gradle run --args='[FILE]'
```

Example:
```bash
$ gradle run --args='data/test2.txt'
```

##### How to build jar file

Enter this in the terminal to build jar file:

```bash
$ gradle jar
```
Jar file can be found in build/libs/

##### How to run jar file

Enter this in the terminal to run the jar file:

```bash
$ java -jar [JAR FILE] [TEXT FILE]
```

Example:

```bash
$ java -jar build/libs/prog-2020-virt-mem-Bupaheh-1.0-SNAPSHOT.jar data/test2.txt
```

##### How to generate test using gradle

Enter this in the terminal to generate test with [NUMBER OF REQUESTS] requests:

```bash
$ gradle run --args='gen [PROCESS DATA SIZE] [RAM SIZE] [NUMBER OF REQUESTS]'
```

Example:

```bash
$ gradle run --args='gen 20 5 40'
```

Generated test will be in "outputTest.txt" in the directory with jar file or in the root directory of the project. 

##### How to generate test using jar

Enter this in the terminal to generate test with [NUMBER OF REQUESTS] requests:

```bash
$ java -jar gen [PROCESS DATA SIZE] [RAM SIZE] [NUMBER OF REQUESTS]
```

Example:

```bash
$ java -jar gen 20 5 40
```

Generated test will be in "outputTest.txt" in the directory with jar file or in the root directory of the project. 

##### Example of generated test

This test was generated using 'gen 20 5 40' argument:

```
20 5
2 5 3 18 1 8 9 20 6 16 2 3 17 10 18 5 8 9 5 11 18 17 14 14 3 20 9 6 19 6 8 3 20 11 7 18 6 7 12 6
```