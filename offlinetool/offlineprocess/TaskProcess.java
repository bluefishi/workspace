package com.apple.video.offlineprocess;
import java.io.IOException;

import com.apple.video.offlineprocess.asr.ASR;
import com.apple.video.offlineprocess.Annotation.appleAnnotate;
public class TaskProcess {
	@SuppressWarnings("unused")
	private taskUI3 gui;
	private ASR asr;//语音识别
	private appleAnnotate annotate;//标注
	
	public TaskProcess(taskUI3 taskGUI3){
		this.gui = taskGUI3;
		asr = new ASR();
		annotate = new appleAnnotate();
	}
//	先不设取消按钮好了
//	public void stop(){
//		stopFlag = true;
//	}
	
	/*添加一个type,表示哪种处理方式
	 * 这个函数又啰嗦了
	 * TODO 0114
	 * */
	public void start(String wavinpath,String picinpath,String outpath,int type) throws IOException
	{
		String temp = outpath;
		switch(type)
		{
		case 1://语音识别+图像标注
			//语音识别
			outpath =temp+"\\asr";
			asr.recognizeAll(wavinpath,outpath);
			
			//图像标注
//			TODO 这里还有点问题，图片是双重文件夹的，需要修改程序
			outpath =temp+ "\\label";
			System.out.println(picinpath+" "+outpath);
			annotate.annotateWrite(picinpath, outpath);
			break;
			
		case 2://语音识别
			outpath = temp+"\\asr";
			asr.recognizeAll(wavinpath,outpath);
			break;
			
		case 3://图像标注
			outpath =temp+ "\\label";
			System.out.println(picinpath+" "+outpath);
			annotate.annotateWrite(picinpath, outpath);
			break;
		}
		
		
//		TODO 这种get check 的方式肯定会有问题的
//		//两个都做，这个应该最常用了
//		if(gui.getCheck("wav_check") && gui.getCheck("pic_check"))
//		{
//			//语音识别
//			inpath +="\\wav";
//			outpath +="\\asr";
//			asr.recognizeAll(inpath,outpath);
//			
//			//图像标注
////			TODO 这里还有点问题，图片是双重文件夹的，需要修改程序
//			inpath +="\\frames";
//			outpath += "\\label";
//			annotate.annotate(inpath, outpath);
//		}
//		//仅做语音
//		else if(gui.getCheck("wav_check"))
//		{
//			outpath +="\\asr";
//			asr.recognizeAll(inpath,outpath);
//		}
//		
//		//仅做标注
//		else if(gui.getCheck("pic_check"))
//		{
//			outpath += "\\label";
//			annotate.annotate(inpath, outpath);
//		}
		
		
		
	}

}
