package com.apple.video.offlineprocess;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import com.apple.video.offlineprocess.asr.ASR;
import com.apple.video.offlineprocess.Annotation.appleAnnotate;
public class TaskProcess {
	private taskUI3 gui;
	private ASR asr;//����ʶ��
	private appleAnnotate annotate;//��ע
	
	public TaskProcess(taskUI3 taskGUI3){
		this.gui = taskGUI3;
		asr = new ASR();
		annotate = new appleAnnotate();
	}
//	�Ȳ���ȡ����ť����
//	public void stop(){
//		stopFlag = true;
//	}
	
	/*�õ�ǰ̨��label��������ʾ�����ڴ������Ϣ*/
	private void freshLabel(final String message){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getLabel().setText(message);
			}
		});
	}
	
	/*���һ��type,��ʾ���ִ���ʽ
	 * */
	public void start(final String wavinpath,final String picinpath,final String outpath,int type) throws IOException
	{
		switch(type)
		{
		case 1:
			freshLabel("���ڽ�������ʶ��....");
			new Thread(){
				public void run(){
					wavprocess(wavinpath,outpath);
					try {
						picprocess(picinpath,outpath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			break;
		case 2:
			new Thread(){
				public void run(){
					wavprocess(wavinpath,outpath);
				}
			}.start();
			break;
		case 3:
			freshLabel("���ڽ���ͼ���ע...");
			new Thread(){
				public void run(){
					try {
						picprocess(picinpath,outpath);//�������Ƕ���final�ˣ���ͻ�������
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			break;
		default://ѡ��һ��������
			break;
		}
	}
	
	private void wavprocess(String inpath,String outpath)
	{
		freshLabel("���ڽ�������ʶ��....");
		outpath =outpath+"\\asr";
		File[] files = new File(inpath).listFiles();
		File outfile = new File(outpath);
		if(!outfile.exists())//������·�������ڣ��򴴽�֮
			outfile.mkdirs();
		String filename = null;
		for(int i=0;i<files.length;i++)
		{
			filename = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
			freshLabel("���ڴ���..."+filename);
			asr.recognizeEach(inpath,outpath,filename);
			System.out.println(filename);
		}
		freshLabel("����ʶ�������");
	}
	private void picprocess(String inpath,String outpath) throws IOException
	{
		freshLabel("���ڽ���ͼ���ע...");
		outpath =outpath+ "\\label";
		int total_num = 0;
		File folder = new File(inpath);
		File[] files_pic = folder.listFiles();//���files[i]��ֱ�Ӱ���ͼƬ�����ļ���
		for(int i =0;i<files_pic.length;i++)
		{
			if(files_pic[i].isDirectory())
			{
				String filename_pic = null;//���filename��Ҫд��txt�ģ�Ҳ�����ļ��е�����
				filename_pic = files_pic[i].getName();
				String tempresult = null;//ÿ��filename�õ��Ľ��
				tempresult = annotate.annotate(files_pic[i].getAbsolutePath());//�����inpath��һ���ļ���·��
				annotate.writetxt(filename_pic+".txt",tempresult,outpath);
				System.out.println(filename_pic+".txt�Ѿ�д�ã�");
				freshLabel(filename_pic+".txt�Ѿ�д�ã�");
				total_num++;
			}
			else
			{
				System.out.println("ͼƬ�ļ��и�ʽ����ȷ");
				freshLabel("ͼƬ�ļ��и�ʽ����ȷ");
			}
		}
		freshLabel("����ע��"+total_num);
		
	}

}
