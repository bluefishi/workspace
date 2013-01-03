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
		insertConsoleText("��̨�߳̿�ʼִ������......\n");
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
			insertConsoleText("����"+(i+1)+"�������\n");
			moveProgressBar(i);
			
		}
		insertConsoleText("��̨�߳̽���ִ������\n");
		setTaskGUIButtonState(true);
	}
	public void start(int taskCount){
		stopFlag = false;
		insertConsoleText("��̨�߳̿�ʼִ������......\n");
		for(int i =0;i<taskCount;i++)
		{
			if(stopFlag)
				break;
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			insertConsoleText("����"+(i+1)+"�������\n");
			moveProgressBar(i);
			
		}
		insertConsoleText("��̨�߳̽���ִ������\n");
		setTaskGUIButtonState(true);
		
	}
	/*
	 * ���水ť״̬
	 * */
	private void setTaskGUIButtonState(final boolean bFlag){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				gui.setButtonState(bFlag);
			}
		});
	}
	/*
	 * ��������ʾ
	 * */
	private void moveProgressBar(final int progress){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getProgressBar().setSelection(progress);
			}
		});
	}
	/*
	 *�����ı� 
	 */
	private void insertConsoleText(final String str){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getConsoleText().insert(str);
			}
		});
	}
}
