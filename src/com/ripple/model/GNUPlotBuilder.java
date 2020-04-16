package com.ripple.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.JavaPlot.Key;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.ripple.beans.ServerInfo;

public class GNUPlotBuilder {
	
	  private String filepath;
	  private Float average;
	  private Long min;
	  private Long max;
	  
	  public GNUPlotBuilder (String filepath) {
		  this.filepath = filepath;
	  }
	
	  //Helper to build the file in the expected format
	  public void createFile (List <ServerInfo> siList) {
		  	String fileContent = "";
		  	for (ServerInfo si : siList) {
		  		fileContent += formatRecord(si);
		  	}
			
			try {
			    Files.write(Paths.get(this.filepath), fileContent.getBytes());
			
			} catch (IOException e) {
			    e.printStackTrace();
			}
	  }
	  
	  //Helper to build the file in the expected format
	  public void deleteFile () {
		  File file = new File(filepath); 
          
	        if(file.delete()) { 
	            System.out.println("File deleted successfully"); 
	        } else { 
	            System.out.println("No file to delete"); 
	        } 
	  }
	  
	  //This method is to set the min time.
	  public void setMinTime (List <ServerInfo> siList) throws ParseException {
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
	      
	      long cycletime = 0;
	      long mintime = siList.size()*1000;
				  
		  	for (int i=0;i<siList.size()-1;i++) {
		  		
		  		String currentsequence = siList.get(i).getResult().getInfo().getValidated_ledger().getSeq();
		  		String nextsequence = siList.get(i+1).getResult().getInfo().getValidated_ledger().getSeq();
		  		Date currenttime = sdf.parse(siList.get(i).getResult().getInfo().getTime());
		  		Date nexttime = sdf.parse(siList.get(i+1).getResult().getInfo().getTime());
		  		
		  		if (currentsequence.equals(nextsequence)) {
		  			cycletime+=Math.abs(nexttime.getTime() - currenttime.getTime());
		  		} else {
		  			cycletime+=Math.abs(nexttime.getTime() - currenttime.getTime());
		  			if (cycletime<mintime) {
		  				mintime = cycletime;
		  			}
	  				cycletime = 0;
		  		}
		  	}
	  		this.min = mintime/1000;
	  }
	  
	  //This method is to set the max time.
	  public void setMaxTime (List <ServerInfo> siList) throws ParseException {
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
	      
	      long cycletime = 0;
	      long maxtime = 0;
				  
		  	for (int i=getBeginSequence(siList);i<getEndSequence(siList)-1;i++) {
		  		String currentsequence = siList.get(i).getResult().getInfo().getValidated_ledger().getSeq();
		  		String nextsequence = siList.get(i+1).getResult().getInfo().getValidated_ledger().getSeq();
		  		Date currenttime = sdf.parse(siList.get(i).getResult().getInfo().getTime());
		  		Date nexttime = sdf.parse(siList.get(i+1).getResult().getInfo().getTime());
		  		
		  		if (currentsequence.equals(nextsequence)) {
		  			cycletime+=Math.abs(nexttime.getTime() - currenttime.getTime());
		  		} else {
		  			cycletime+=Math.abs(nexttime.getTime() - currenttime.getTime());
		  			if (cycletime>maxtime) {
		  				maxtime = cycletime;
		  			}
	  				cycletime = 0;
		  		}
		  	}
	  		this.max=maxtime/1000;
	  }

	  //To calculate the average, we simply divide the total time by the number of new ledgers 
	  public void setAverageTime (List <ServerInfo> siList, int frequency) throws ParseException {	      
	      int count = 1;
	      long cycletime = 0; 
	    		  
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
		  
		  	for (int i=getBeginSequence(siList);i<getEndSequence(siList)-1;i++) {
		  		String currentsequence = siList.get(i).getResult().getInfo().getValidated_ledger().getSeq();
		  		String nextsequence = siList.get(i+1).getResult().getInfo().getValidated_ledger().getSeq();
		  		Date currenttime = sdf.parse(siList.get(i).getResult().getInfo().getTime());
		  		Date nexttime = sdf.parse(siList.get(i+1).getResult().getInfo().getTime());
	  			cycletime+=Math.abs(nexttime.getTime() - currenttime.getTime());
		  		if (!currentsequence.equals(nextsequence)) {
		  			count++;
		  		} 
		  	}
		  	float average = (float)cycletime/count/1000;
	  		this.average=average;
	  }

	  //Since the first transaction is likely to be incomplete, we use this method to remove the first sequence
	  private int getBeginSequence(List <ServerInfo> siList) {
	      int beginsequence=0;
	      
		  	for (int i=0;i<siList.size()-1;i++) {
		  		String currentsequence = siList.get(i).getResult().getInfo().getValidated_ledger().getSeq();
		  		String nextsequence = siList.get(i+1).getResult().getInfo().getValidated_ledger().getSeq();
		  		
		  		if (currentsequence.equals(nextsequence)) {
		  			beginsequence++;
		  		} else {
		  			beginsequence++;
		  			i=siList.size()-1;
		  		}
		  	}
		  	return beginsequence;
	  }
	  
	  //Since the end transaction is unlikely to be complete, we use this method to remove the last sequence
	  private int getEndSequence(List <ServerInfo> siList) {
	      //First we are getting rid of the first and last ledgers as they cannot be counted as full transaction time
	      int endsequence=siList.size();
	      
		  	for (int i=siList.size()-1;i>1;i--) {
		  		String currentsequence = siList.get(i).getResult().getInfo().getValidated_ledger().getSeq();
		  		String nextsequence = siList.get(i-1).getResult().getInfo().getValidated_ledger().getSeq();
		  		
		  		if (currentsequence.equals(nextsequence)) {
		  			endsequence--;
		  		} else {
		  			endsequence--;
		  			i=1;
		  		}
		  	}
		  	return endsequence;
	  }
	  
	//This method is used to execute GNU Plot and generate the plot
	public void createPlot (String gnupath) {
		JavaPlot p = new JavaPlot(gnupath);
		try {
				//Load result file from polling
				FileDataSet dataset = new FileDataSet(new File(this.filepath));
				DataSetPlot dsp = new DataSetPlot(dataset);
		        PlotStyle style = new PlotStyle();
		        
				//Configure line style
		        style.setStyle(Style.LINESPOINTS);
		        style.setLineWidth(2);
				dsp.setPlotStyle(style);
		        
				//Explicit data columns to be used
				dsp.set( "using", "1:2" );

				//Plot properties configuration
				p.addPlot(dsp);
			    p.set("title", "'New ledger min interval="+this.min+"s         Max interval="+this.max+"s         Average interval="+this.average+"s'");
			    p.set("xlabel","'Time (UTC)'");
				p.set("ylabel","'Sequence'");
				p.set("border","3");
				p.set("grid", "x y");
				p.set("format y", "'%6.0f'");
				p.setKey(Key.OFF);
				
				//Make axis a time line
			    p.set("xdata","time");
				p.set("timefmt","'%H:%M:%S'");
				p.set("format x","'%H:%M:%S'");
				
				//Make axis a time line
				p.plot();
				
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException | IOException e1) {
			System.out.println(e1.getMessage());
		}
  }
	  
	  //This method formats each Server Information object for future storage into the GNUPLOT file
	  private String formatRecord (ServerInfo si) {
		  String record;
		  String inputtime = si.getResult().getInfo().getTime();
		  String outputtime="";
		  Date date;

		  DateFormat inputFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
		  DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");

		  try {
			  date = inputFormat.parse(inputtime);
			  outputtime = outputFormat.format(date);
		  } catch (ParseException e) {
			  e.printStackTrace();
		  }
		  
		  record = outputtime + "  " + si.getResult().getInfo().getValidated_ledger().getSeq()+"\n";
		 
		  return record;
	  }

}
