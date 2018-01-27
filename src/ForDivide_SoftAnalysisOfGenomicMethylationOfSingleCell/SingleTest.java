package ForDivide_SoftAnalysisOfGenomicMethylationOfSingleCell;

public class SingleTest {
	
	//1-22 X, Y
	static long[] locat = {0,249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	static int WATSON = 1;
	static int CRICK = 2;
	
	public SingleTest(){
		String resource = "E:/morehouse/download/";
		String filename,rfile;
			filename = "chr22_divide_map.txt";
			rfile = "chr22.txt";
		 	//step 1 repeat filter
			new RepeatFilterForEachChr((int)locat[22], resource+filename,
					resource+"REPEAT/"+rfile, 
					resource+"SD/"+rfile,
					resource+"CNV/"+rfile, 
					resource+"REPEAT_FILTER/"+filename,
					WATSON);
			//step4 filter multiple repeat reads
			new MultiRepeatsFilter((int)locat[22], resource+"REPEAT_FILTER/"+filename, 
					resource+"REPEAT/"+rfile,
					resource+"SD/"+rfile,
					resource+"CNV/"+rfile, 
					resource+"MUTIREPEAT_FILTER/"+filename,
					WATSON);
			//step 5  overlap filter
			new OverlapFilter((int)locat[22], resource+"MUTIREPEAT_FILTER/"+filename, 
					resource+"OVERLAP_FILTER/"+filename);
	}
	
	public static void main(String args[]){
		new SingleTest();
	}

}
