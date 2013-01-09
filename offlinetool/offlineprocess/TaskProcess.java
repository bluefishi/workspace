package com.apple.video.offlineprocess;
import com.apple.video.offlineprocess.process.ASR;
public class TaskProcess {
	private taskUI3 gui;
	private ASR asr;
	
//	private String inpath;
//	private String outpath;
	
	public TaskProcess(taskUI3 taskGUI3){
		this.gui = taskGUI3;
//		inpath = gui.getPath("wavtext");//"D:\\Data\\testresult\\wav";
//		outpath = gui.getPath("psavepathtext");//"D:\\Data\\testresult\\out\\wav";
		asr = new ASR();
//		LabelPic labelpic = new LabelPic();
	}
//	先不设取消按钮好了
//	public void stop(){
//		stopFlag = true;
//	}
	public void start(String inpath,String outpath)
	{
		outpath +="\\asr";
		asr.recognizeAll(inpath,outpath);
		//		labelpic.label();
		
	}

}
