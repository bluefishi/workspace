package com.apple.video.offlineprocess;
/*
 * Extract keyframe and wav 
 * input :video path / outputpath
 * output:keyframe + wav
 * */
import java.io.*;
import java.util.List;

import com.jvisionlab.core.FFmpegHelper;
import com.jvisionlab.video.frames.FFVideoFrame;

public class Extract {
	String ffmpegtool = null;
	FFmpegHelper ff = null;
	FFVideoFrame video = null;
	File folder = null;
	
	@SuppressWarnings("static-access")
	public Extract(){
		ffmpegtool = "ffmpeg.exe";
		FFmpegHelper ff =new FFmpegHelper();
		ff.setFFmpegPath(ffmpegtool);
	}
	public void ExtractEach(String filename,String outpath)
	{
		video = new FFVideoFrame(filename);
//		抽取关键帧
		try{
			video.extractKeyframe(null, null, 1, outpath);
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
				System.out.println("check your file");
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			
//			提取语音
		List<String> commend=new java.util.ArrayList<String>();  
		commend.add(ffmpegtool); 
		commend.add("-i");  
	    commend.add(filename);  
	    commend.add("-ar"); 
	    commend.add("16000");
	    commend.add("-y");
	    outpath +=filename.substring(filename.lastIndexOf("\\"),filename.lastIndexOf("."))+".wav";
	    System.out.println(outpath);
	    commend.add(outpath);
	    try {  
	         ProcessBuilder builder = new ProcessBuilder();  
	         builder.command(commend);  
	         builder.start();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return;  
	        }
	}
		
	
	public static void main(String[] args)throws Exception //throws Exception
	{
		Extract fe = new Extract();
		fe.ExtractEach("d:/testvideo/0000sIheness._-o-_.sheness_512kb.mp4","D:/0000sIheness._-o-_.sheness_512kb");
	}
}



