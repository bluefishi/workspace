package com.apple.video.offlineprocess;

/*Date: 2013 01 09
 * This task should be renamed as TaskExtract*/


import org.eclipse.swt.widgets.Display;

import com.apple.video.offlineprocess.extract.Extract;

public class Task3 {
	private taskUI3 gui;
	private boolean stopFlag;
	private boolean[] isDone;//想用来实时地表示视频处理情况
	private Extract extract;
	
	public Task3(taskUI3 taskGUI3){
		this.gui = taskGUI3;
		extract = new Extract();
	}
	public void stop(){
		stopFlag = true;
	}
	public void start(String[] files,String outpath,int taskCount)
	{
		String filename;
		stopFlag = false;
		isDone = new boolean[taskCount];
		for(int i =0;i<taskCount;i++)
		{
			if(stopFlag)
				break;
			filename = files[i];
			/*
			 * main extraction work goes here
			 * */
			extract.ExtractEach(filename, outpath);
			isDone[i] = true;
			
			moveProgressBar(i+1);
			refreshTableViewer(files,isDone);
		}
		/*这一次的任务处理完了，把
		 * 1.处理视频的按钮激活
		 * 2.取消的按钮失效
		 * 3.开始的按钮先不管啦
		 * */
		setTaskGUIprocessBtnState(true);
		setTaskGUIcancelBtnState(false);
//		setTaskGUIpButtonState(true);
	}
	
	/*
	 * 界面按钮状态
	 * */
	private void setTaskGUIprocessBtnState(final boolean bFlag){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				gui.setprocessBtnState(bFlag);
			}
		});
	}
	private void setTaskGUIcancelBtnState(final boolean bFlag){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				gui.setcancelBtnState(bFlag);
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
	private void refreshTableViewer(final String[] files,final boolean[] isDone)//final boolean[] idDone
	{
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				Object data = FileFactory.getPeoples(files,isDone);
				gui.getConsoleTableViewer().setInput(data);
				}
			});
	}
}
