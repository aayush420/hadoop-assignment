alias hadoop=~/tools/hadoop/hadoop-3.3.1/bin/hadoop
hadoop com.sun.tools.javac.Main WordCount.java 
jar cf wc.jar WordCount*.class 
rm -rf output1*
hadoop jar wc.jar WordCount input1/wiki_tutorial output1
