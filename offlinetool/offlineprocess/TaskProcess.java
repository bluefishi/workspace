package com.apple.video.offlineprocess;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import com.apple.video.offlineprocess.asr.ASR;
import com.apple.video.offlineprocess.Annotation.appleAnnotate;
public class TaskProcess {
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
	
	/*得到前台的label，更新显示现在在处理的信息*/
	private void freshLabel(final String message){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				gui.getLabel().setText(message);
			}
		});
	}
	
	/*添加一个type,表示哪种处理方式
	 * */
	public void start(final String wavinpath,final String picinpath,final String outpath,int type) throws IOException
	{
		switch(type)
		{
		case 1:
			freshLabel("正在进行语音识别....");
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
			freshLabel("正在进行图像标注...");
			new Thread(){
				public void run(){
					try {
						picprocess(picinpath,outpath);//现在它们都是final了，这就会有问题
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			break;
		default://选择一个功能先
			break;
		}
	}
	
	private void wavprocess(String inpath,String outpath)
	{
		freshLabel("正在进行语音识别....");
		outpath =outpath+"\\asr";
		File[] files = new File(inpath).listFiles();
		File outfile = new File(outpath);
		if(!outfile.exists())//如果结果路径不存在，则创建之
			outfile.mkdirs();
		String filename = null;
		for(int i=0;i<files.length;i++)
		{
			filename = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
			freshLabel("正在处理..."+filename);
			asr.recognizeEach(inpath,outpath,filename);
			System.out.println(filename);
		}
		freshLabel("语音识别结束。");
	}
	private void picprocess(String inpath,String outpath) throws IOException
	{
		freshLabel("正在进行图像标注...");
		outpath =outpath+ "\\label";
		int total_num = 0;
		File folder = new File(inpath);
		File[] files_pic = folder.listFiles();//这个files[i]是直接包含图片的子文件夹
		for(int i =0;i<files_pic.length;i++)
		{
			if(files_pic[i].isDirectory())
			{
				String filename_pic = null;//这个filename是要写成txt的，也是子文件夹的名字
				filename_pic = files_pic[i].getName();
				String tempresult = null;//每个filename得到的结果
				tempresult = annotate.annotate(files_pic[i].getAbsolutePath());//这里的inpath是一个文件夹路径
				annotate.writetxt(filename_pic+".txt",tempresult,outpath);
				System.out.println(filename_pic+".txt已经写好！");
				freshLabel(filename_pic+".txt已经写好！");
				total_num++;
			}
			else
			{
				System.out.println("图片文件夹格式不正确");
				freshLabel("图片文件夹格式不正确");
			}
		}
		freshLabel("共标注："+total_num);
		
	}

}
