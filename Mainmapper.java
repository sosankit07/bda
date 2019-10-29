
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.GenericOptionsParser; 
import java.io.IOException;
import java.util.Iterator;
public class Mainmapper {

	public static void main(String[] args) throws Exception {

Configuration conf=new Configuration(); 
String[] files=new GenericOptionsParser(conf,args).getRemainingArgs(); 
Path input=new Path(files[0]); 
Path output=new Path(files[1]); 
Job a=new Job(conf,"wordcount"); 
a.setJarByClass(Mainmapper.class); 
a.setMapperClass(yeartempmapper.class); 
a.setReducerClass(REducer.class); 
a.setOutputKeyClass(IntWritable.class); 
a.setOutputValueClass(FloatWritable.class); 
FileInputFormat.addInputPath(a, input); 
FileOutputFormat.setOutputPath(a, output); 
System.exit(a.waitForCompletion(true)?0:1); 
}
}

class yeartempmapper
		extends
		Mapper<LongWritable, Text, org.apache.hadoop.io.IntWritable, org.apache.hadoop.io.FloatWritable> {

	public void map(LongWritable ikey, Text ivalue, Context context)
			throws IOException, InterruptedException {
String line= ivalue.toString();
String [] tokens= StringUtils.split(line, "|");
if(tokens.length==5)
{
	int year =Integer.parseInt(tokens[1]);
	float temp= Float.parseFloat(tokens[4]);
	//write it to the mapper of the output
	context.write(new IntWritable(year),new FloatWritable(temp));
}
	}

}

class REducer extends
		Reducer<IntWritable, FloatWritable, IntWritable, FloatWritable> 
{

	public void reduce(IntWritable _key, Iterable<FloatWritable> values, Context context)
			throws IOException, InterruptedException {
		Iterator<FloatWritable> iterator =values.iterator();
		float maxtemp=0;
		if(iterator.hasNext())
		{
			maxtemp=iterator.next().get();
			
		}
		while(iterator.hasNext())
		{
			float temp= iterator.next().get();
			if (temp>maxtemp)
			{
				maxtemp=temp;
				
			}
			
		}
		context.write(_key, new FloatWritable(maxtemp));
	}

}
