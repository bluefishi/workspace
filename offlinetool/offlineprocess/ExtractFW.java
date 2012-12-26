package com.apple.video.offlineprocess;
/*
 * input:videofolderpath+keyframe_save_path
 * output:folder(videoname)+cut(keyframes)
 * 							+gradu(keyframes)
 * use: 1.-->tagging
 * 		2.-->shot combine
 * */
import java.io.*;
import java.util.List;

import com.jvisionlab.core.FFmpegHelper;
import com.jvisionlab.video.frames.FFVideoFrame;

public class ExtractFW {
	String choice = null;//选择处理什么，是抽帧还是抽wav
	String ffmpegtool = null;
	FFmpegHelper ff = null;
	FFVideoFrame video = null;
	File folder = null;
	
	@SuppressWarnings("static-access")
	public ExtractFW(String choice){
		ffmpegtool = "D:\\JavaWorkSpace\\OfflineProcess\\ffmpeg.exe";
		if(choice.equals("F"))
		{
			FFmpegHelper ff =new FFmpegHelper();
			ff.setFFmpegPath(ffmpegtool);
			this.choice = choice;
		}else if(choice.equals("W"))
		{
			this.choice = choice;
		}else{
			System.out.println("请选择'F'或者'W'!");
		}
		
		
	}
	public void ExtractAll(String filefolder,String outpath)
	{
		String currentOut = null;
		folder = new File(filefolder);
		if(folder.isDirectory())
		{
			File[] files = folder.listFiles();
			File currentFile = null;
			for(int i=0;i<files.length;i++)
			{
				currentFile = files[i];
				
				if(currentFile.isFile())
				{
					currentOut = outpath+"/"+currentFile.getName().substring(0,currentFile.getName().lastIndexOf("."));
					try{
						ExtractEach(currentFile.getAbsolutePath(),currentOut);
						System.out.println(currentFile.getAbsolutePath());
					}catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}else{
					System.out.println("亲，发现了不是视频的文件哟，你看着办吧");
					continue;
				}
				
			}
			
		}else{
			System.out.println("亲，要选个文件夹哟！");
			return;
		}
	}
	
	//每张图，抽帧，设置时间间隔video.extractKeyframe(null,null,1,outpath);
	public void ExtractEach(String filename,String outpath)
	{
		
		String choice = this.choice;
		if(choice.equals("F"))
		{
			video = new FFVideoFrame(filename);
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
		}
		else if(choice.equals("W"))
		{
			List<String> commend=new java.util.ArrayList<String>();  
			commend.add(ffmpegtool); 
			commend.add("-i");  
		    commend.add(filename);  
		    commend.add("-ar"); 
		    commend.add("16000");
		    commend.add("-y");
		    outpath +=".wav";
		    commend.add(outpath);
		    try {  
		         ProcessBuilder builder = new ProcessBuilder();  
		         builder.command(commend);  
		         builder.start();  
		          System.out.println("ok");
		        } catch (Exception e) {  
		            e.printStackTrace();  
		            return;  
		        }
		}
		else{
			System.out.println("亲，不可能呀，请选择'F'或者'W'!");
		}
	}
	
	
	public static void main(String[] args)throws Exception //throws Exception
	{
		ExtractFW fe = new ExtractFW("W");
		fe.ExtractAll("d:/testvideo", "D:/testvideo/outwav");
	}
}



