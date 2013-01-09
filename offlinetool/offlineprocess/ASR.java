package com.apple.video.offlineprocess.process;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 1.дcontrol�ļ�
 * 2.pockesphinxʶ��
 * ��Щ���ߵ�λ�û��Ǹ����⣡������������
 * etcдһ���ļ�������ÿ��дһ������������
 * etcдÿ���ļ�һ����Ϊ�������ʶ��������ܵ�����һ���ļ�
 * ��������ʱ��ʡ���ٶ���һ����
 * ԭ����ģ��
 * 
 * Date:2013 0109 
 * input:filefolder + outpath
 * filefolder�������wav�ļ��ˣ�outpath�������д�õ�text�ļ��ˡ�
 * 
*/

public class ASR {
	public ASR(){
		
	}
	
	public void recognizeAll(String inpath,String outpath)
	{
		File[] files = new File(inpath).listFiles();
		File outfile = new File(outpath);
		if(!outfile.exists())//������·�������ڣ��򴴽�֮
			outfile.mkdirs();
		String filename = null;
		for(int i=0;i<files.length;i++)
		{
			filename = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
			recognizeEach(inpath,outpath,filename);
			System.out.println(filename);
		}
	}
	private void recognizeEach(String inpath,String outpath,String filename)
	{
		//дtempfile
		File tempfile = new File(filename+".fileids");
		try {
			FileWriter writer = new FileWriter(tempfile);
			writer.write(filename);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//����ʶ��
		boolean flag = false;
		flag = usesphinx(inpath,outpath,filename);
		//delete tempfile
		if(flag == true)
			tempfile.delete();
	}
	
	private boolean usesphinx(String inpath,String outpath,String filename)
	{
		String[] cmd = new String[19];
		cmd[0] = "asr/pocketsphinx_batch";
		cmd[1] = "-hmm";
		cmd[2] = "asr/hub4wsj_sc_8k";
		cmd[3] = "-lm";
		cmd[4] = "asr/language_model.arpaformat.DMP";
		cmd[5] = "-dict";
		cmd[6] = "asr/cmudict.hub4.06d.dict";
		cmd[7] = "-ctl";
		cmd[8] = filename+".fileids";
		cmd[9] = "-adcin";
		cmd[10] = "yes";
		cmd[11] = "-adchdr";
		cmd[12] = "44";
		cmd[13] = "-cepdir";
		cmd[14] = inpath;
		cmd[15] = "-cepext";
		cmd[16] = ".wav";
		cmd[17] = "-hyp";
		cmd[18] = outpath+"\\"+filename+".txt";
		
		try { 
			Process pro = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
		String str;
		while((str =br.readLine())!=null)
			System.out.println(str);
		pro.waitFor(); 
		return true;
		} catch (IOException e) { 
		e.printStackTrace();
		return false;
		}catch (InterruptedException e2) { 
		e2.printStackTrace();
		return false;
		} 
	}
	
	public static void main(String args[])
	{
		String inpath = "D:\\Data\\testresult\\wav";
		String outpath = "D:\\Data\\testresult\\out\\asr";
		ASR asr = new ASR();
		asr.recognizeAll(inpath,outpath);
	}
}
