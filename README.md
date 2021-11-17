## Instructions
- Add opennlp-tools.jar to `hadoop classpath`.
- Compile source file -
```
hadoop com.sun.tools.javac.Main WordCount.java 
```
- Create jar archive from the class files -
```
jar cf wc.jar WordCount*.class 
```
- Delete the existing output1 folder (if it exists) -
```
rm -rf output1
```
- Run the jar file against the input directory - 
```
	hadoop jar wc.jar WordCount input1/wiki_tutorial output1
```
