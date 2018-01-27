package ForDivide_SoftAnalysisOfGenomicMethylationOfSingleCell;

public class Main {
	
	//1-22 X, Y
	static long[] locat = {0,249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	static int WATSON = 1;
	static int CRICK = 2;
	
	public Main(){
		String resource = "E:/morehouse/download/";
		String base = "E:/morehouse/z=50/divide/01/FormatConvert/";
		String filename,rfile;
		for(int i=1;i<=23;i++){
			filename = "Chr"+i+"_c+.txt";
	System.err.println(filename);
			rfile = "chr"+i+".txt";
			if(i ==23 ){
				filename = "ChrX_c+.txt";
				rfile = "chrX.txt";
			}
		 	//step 1 repeat filter
			new RepeatFilterForEachChr((int)locat[i], base+filename,
					resource+"REPEAT/"+rfile, 
					resource+"SD/"+rfile,
					resource+"CNV/"+rfile, 
					base+"REPEAT_FILTER/"+filename,
					CRICK);
			//step4 filter multiple repeat reads
			new MultiRepeatsFilter((int)locat[i], base+"REPEAT_FILTER/"+filename, 
					resource+"REPEAT/"+rfile,
					resource+"SD/"+rfile,
					resource+"CNV/"+rfile, 
					base+"MUTIREPEAT_FILTER/"+filename,
					CRICK);
			//step 5  overlap filter
			new OverlapFilter((int)locat[i], base+"MUTIREPEAT_FILTER/"+filename, 
					base+"OVERLAP_FILTER/"+filename);
			
			//STEP 6 If multiple mapping, 则留与那个与repeatmasker（任何）/CNV/segmental dups中一个overlap少的那个reads
			new MultipleMapFilter((int)locat[i], base+"OVERLAP_FILTER/"+filename, 
					resource+"REPEAT/"+rfile,
					resource+"SD/"+rfile,
					resource+"CNV/"+rfile, 
					base+"MUTIMAP_FILTER/"+filename,
					CRICK);
		}
	}
	
	public static void main(String args[]){
		new Main();
	}

}
