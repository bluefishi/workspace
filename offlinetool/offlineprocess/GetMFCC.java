package com.apple.video.offlineprocess.feature.mfcc;
/*����д��������ÿ��wav�ļ���mfcc�����㲢����*/
import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.UnsupportedAudioFileException;
import comirva.audio.util.MFCC;

public class GetMFCC {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IllegalArgumentException, IOException, UnsupportedAudioFileException
	  {
		  MFCC mfcc = new MFCC(8000);
		  Vector result = new Vector();
		  result = mfcc.process("e:/test.wav",mfcc);
	      System.out.println(result.size());
	      mfcc.saveMFCC(result, "test.txt", "e:/DataSet");
	  }
	
	

}
