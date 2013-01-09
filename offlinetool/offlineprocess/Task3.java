package com.apple.video.offlineprocess;

/*Date: 2013 01 09
 * This task should be renamed as TaskExtract*/


import org.eclipse.swt.widgets.Display;

import com.apple.video.offlineprocess.extract.Extract;

public class Task3 {
	private taskUI3 gui;
	private boolean stopFlag;
	private boolean[] isDone;//������ʵʱ�ر�ʾ��Ƶ�������
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
		/*��һ�ε����������ˣ���
		 * 1.������Ƶ�İ�ť����
		 * 2.ȡ���İ�ťʧЧ
		 * 3.��ʼ�İ�ť�Ȳ�����
		 * */
		setTaskGUIprocessBtnState(true);
		setTaskGUIcancelBtnState(false);
//		setTaskGUIpButtonState(true);
	}
	
	/*
	 * ���水ť״̬
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
