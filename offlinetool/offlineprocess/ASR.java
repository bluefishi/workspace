package com.apple.video.offlineprocess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 1.写control文件
 * 2.pockesphinx识别
 * 那些工具的位置还是个问题！！！！！！！
 * etc写一个文件，还是每个写一个？？？？？
 * etc写每个文件一个，为的是最后识别出来的能单独成一个文件
 * 建索引的时候，省得再多做一步了
 * 原来的模型
 * cmd[0] = "asr/pocketsphinx_batch";
		cmd[1] = "-hmm";
		cmd[2] = "asr/hub4wsj_sc_8k";
		cmd[3] = "-lm";
		cmd[4] = "asr/hub4.5000.DMP";
		cmd[5] = "-dict";
		cmd[6] = "asr/cmu07a.dic";
		cmd[7] = "-ctl";
		cmd[8] = "asr/etc/"+filename+".fileids";
		cmd[9] = "-adcin";
		cmd[10] = "yes";
		cmd[11] = "-adchdr";
		cmd[12] = "44";
		cmd[13] = "-cepdir";
		cmd[14] = wavpath;
		cmd[15] = "-cepext";
		cmd[16] = ".wav";
		cmd[17] = "-hyp";
		cmd[18] = outpath+"\\"+filename+".hpy";*/


public class ASR {
	String folder = null;
	String outputpath = null;
	
//	有什么需要初始化呢？
	public ASR(String folder,String outputpath){
		this.folder = folder;
		this.outputpath = outputpath;
	}
	
	private void recognizeAll()
	{
		//TODO: 判断是不是正确路径
		//TODO: 写个log啥的，记录一下，都处理过什么东西。。。。。
		//TODO: 把temp也放在outputpath里面吗
		File[] files = new File(folder).listFiles();
		String content = null;
		for(int i=0;i<files.length;i++)
		{
			content = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
			recognizeEach(content);
			System.out.println(content);
		}
	}
	private void recognizeEach(String filename)
	{
		//写tempfile
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
		//语音识别
		boolean flag = false;
		flag = usesphinx(filename);
		//delete tempfile
		if(flag == true)
			tempfile.delete();
	}
//	使用pocketshpinx识别
	private boolean usesphinx(String filename)
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
		cmd[14] = folder;
		cmd[15] = "-cepext";
		cmd[16] = ".wav";
		cmd[17] = "-hyp";
		cmd[18] = outputpath+"\\"+filename+".hpy";
		
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
		ASR asr = new ASR("D:/testvideo/outwav","D:/testvideo/outtext");
		asr.recognizeAll();
	}
}
