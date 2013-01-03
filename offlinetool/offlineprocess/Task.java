package com.apple.video.offlineprocess;

import org.eclipse.swt.widgets.Display;

public class Task {
	private testUI gui;
	private boolean stopFlag;
//	private Extract extract;
	private ExtractFW extractfw;
	
	public Task(testUI taskGUI){
		this.gui = taskGUI;
		extractfw = new ExtractFW("F");
	}
	public void stop(){
		stopFlag = true;
	}
	public void start(String[] files,String outpath,int taskCount)
	{
		String filename;
		String currentOut;
		stopFlag = false;
		insertConsoleText("后台线程开始执行任务......\n");
		for(int i =0;i<taskCount;i++)
		{
			if(stopFlag)
				break;
			filename = files[i];
			//				TODO:work goes here
			currentOut = outpath+"/"+filename.substring(filename.lastIndexOf("\\")+1,filename.lastIndexOf("."));
			System.out.println(currentOut);
			extractfw.ExtractEach(filename, currentOut);
//				Thread.sleep(100);
			insertConsoleText("任务"+(i+1)+"处理完毕\n");
			moveProgressBar(i);
			
		}
		insertConsoleText("后台线程结束执行任务\n");
		setTaskGUIButtonState(true);
	}
	public void start(int taskCount){
		stopFlag = false;
		insertConsoleText("后台线程开始执行任务......\n");
		for(int i =0;i<taskCount;i++)
		{
			if(stopFlag)
				break;
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			insertConsoleText("任务"+(i+1)+"处理完毕\n");
			moveProgressBar(i);
			
		}
		insertConsoleText("后台线程结束执行任务\n");
		setTaskGUIButtonState(true);
		
	}
	/*
	 * 界面按钮状态
	 * */
	private void setTaskGUIButtonState(final boolean bFlag){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				gui.setButtonState(bFlag);
			}
		});
	}
	/*
	 * 进度条显示
	 * */
	private void moveProgressBar(final int progress){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getProgressBar().setSelection(progress);
			}
		});
	}
	/*
	 *插入文本 
	 */
	private void insertConsoleText(final String str){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getConsoleText().insert(str);
			}
		});
	}
}
