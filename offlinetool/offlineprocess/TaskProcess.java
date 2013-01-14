package com.apple.video.offlineprocess;
import java.io.IOException;

import com.apple.video.offlineprocess.asr.ASR;
import com.apple.video.offlineprocess.Annotation.appleAnnotate;
public class TaskProcess {
	@SuppressWarnings("unused")
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
	
	/*���һ��type,��ʾ���ִ���ʽ
	 * ��������ֆ�����
	 * TODO 0114
	 * */
	public void start(String wavinpath,String picinpath,String outpath,int type) throws IOException
	{
		String temp = outpath;
		switch(type)
		{
		case 1://����ʶ��+ͼ���ע
			//����ʶ��
			outpath =temp+"\\asr";
			asr.recognizeAll(wavinpath,outpath);
			
			//ͼ���ע
//			TODO ���ﻹ�е����⣬ͼƬ��˫���ļ��еģ���Ҫ�޸ĳ���
			outpath =temp+ "\\label";
			System.out.println(picinpath+" "+outpath);
			annotate.annotateWrite(picinpath, outpath);
			break;
			
		case 2://����ʶ��
			outpath = temp+"\\asr";
			asr.recognizeAll(wavinpath,outpath);
			break;
			
		case 3://ͼ���ע
			outpath =temp+ "\\label";
			System.out.println(picinpath+" "+outpath);
			annotate.annotateWrite(picinpath, outpath);
			break;
		}
		
		
//		TODO ����get check �ķ�ʽ�϶����������
//		//�������������Ӧ�������
//		if(gui.getCheck("wav_check") && gui.getCheck("pic_check"))
//		{
//			//����ʶ��
//			inpath +="\\wav";
//			outpath +="\\asr";
//			asr.recognizeAll(inpath,outpath);
//			
//			//ͼ���ע
////			TODO ���ﻹ�е����⣬ͼƬ��˫���ļ��еģ���Ҫ�޸ĳ���
//			inpath +="\\frames";
//			outpath += "\\label";
//			annotate.annotate(inpath, outpath);
//		}
//		//��������
//		else if(gui.getCheck("wav_check"))
//		{
//			outpath +="\\asr";
//			asr.recognizeAll(inpath,outpath);
//		}
//		
//		//������ע
//		else if(gui.getCheck("pic_check"))
//		{
//			outpath += "\\label";
//			annotate.annotate(inpath, outpath);
//		}
		
		
		
	}

}
