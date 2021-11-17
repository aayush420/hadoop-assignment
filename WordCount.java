import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.lang.Math;
import java.util.StringTokenizer;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import opennlp.tools.stemmer.PorterStemmer;

public class WordCount {

  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      PorterStemmer stemmer = new PorterStemmer();
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(stemmer.stem(itr.nextToken()));
        context.write(word, one);
      }
    }
  }

  public static class TokenizerDoubleMapper extends Mapper<Object, Text, Text, DoubleWritable> {

    private final static DoubleWritable one = new DoubleWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      PorterStemmer stemmer = new PorterStemmer();
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(stemmer.stem(itr.nextToken()));
        context.write(word, one);
      }
    }
  }

  public static class IntSumReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    private Properties dfProps = new Properties();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
      InputStream iStream = new FileInputStream("./output1df/part-r-00000");
      dfProps.load(iStream);

    }

    private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
        throws IOException, InterruptedException {
      double sum = 0;
      for (DoubleWritable val : values) {
        sum += (double) val.get();
      }
      result.set((double) sum * Math.log(10000 / (sum + 1)));
      context.write(key, result);
    }
  }

  public static class IntSumReducerPerDoc extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += 1;
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "documnt frequency");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducerPerDoc.class);
    job.setReducerClass(IntSumReducerPerDoc.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1] + "df"));
    // Print output file path of the job
    job.waitForCompletion(true);

    Configuration conf1 = new Configuration();
    Job job1 = Job.getInstance(conf1, "normalized term frequency");
    job1.setJarByClass(WordCount.class);

    job1.addCacheFile(new Path(args[1] + "df/part-r-00000").toUri());
    job1.setMapperClass(TokenizerDoubleMapper.class);
    // job.setCombinerClass(IntSumReducer.class);
    job1.setReducerClass(IntSumReducer.class);
    job1.setOutputKeyClass(Text.class);
    job1.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path(args[1] + "tf"));

    System.exit(job1.waitForCompletion(true) ? 0 : 1);
  }
}
