package com.apple.video.offlineprocess.Annotation;

/*input��file folder or file
 * output: txt files with the name of image groups (or images)name
 * */

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;


public class appleAnnotate {
	/*load����*/
	// Data variables
	private String [] labelNameList = null;
	private int[][] label_train = null;
	private String[] imageNameList = null;
	
	private final String imageIndexPath = "train images/train_index";	
	private final String featureTrainDirectory = "train_features/Corel/";
	private final String labelTrainDirectory = "Concepts/";
	private final String labelFilePath = labelTrainDirectory + "label_train";
	private final String labelNamePath = labelTrainDirectory + "label_name.txt";
	
	public appleAnnotate(){
		load();
	}
	
	public static void main(String[] args) throws IOException
	{
		String inpath = "D:\\Data\\testresult\\frames";
		String outpath = "D:\\Data\\testresult\\out\\label";
		
//		String result = null;
		appleAnnotate t = new appleAnnotate();
		t.annotateWrite(inpath, outpath);
//		result = t.annotate(inpath,outpath);
//		t.writetxt("test.txt", result, outpath);
	}
	private void load()
	{
		// load labelNameList
		try {
			BufferedReader br = new BufferedReader(new FileReader(labelNamePath));
			List<String> list = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			labelNameList = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				labelNameList[i] = list.get(i);
				//System.out.println(labelNameList[i]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("open file "+labelNamePath+" error!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("read file "+labelNamePath+" error!");
		}
		
		// load train image index
		try {
			BufferedReader br = new BufferedReader(new FileReader(imageIndexPath));
			List<String> list = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			imageNameList = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				imageNameList[i] = list.get(i);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("open file "+imageIndexPath+" error!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("read file "+imageIndexPath+" error!");
		}
		
		// load labels of training images
		 try {
			BufferedReader br_label = new BufferedReader( new FileReader(labelFilePath));
			String line_label = null;
			List<int[]> list_label=new ArrayList<int[]>();
			while(( line_label = br_label.readLine())!=null){
				String[] strArr = line_label.split("\t");
				int[] intArr=new int[strArr.length];
				for(int j=0;j<strArr.length;j++){
					intArr[j]=Integer.valueOf(strArr[j]);
				}
				list_label.add(intArr);
			}
			label_train=new int[list_label.size()][];
			for(int row=0;row<list_label.size();row++){
				label_train[row]=list_label.get(row);	 
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("open file "+labelFilePath+" error!");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("read file "+labelFilePath+" error!");
		}
	}

	public void annotateWrite(String inpath,String outpath) throws IOException
	{
		/*����������һ�������ܶ����ļ��е�folder*/
		File folder = new File(inpath);
		File[] files = folder.listFiles();//���files[i]��ֱ�Ӱ���ͼƬ�����ļ���
		for(int i =0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				String filename = null;//���filename��Ҫд��txt�ģ�Ҳ�����ļ��е�����
				filename = files[i].getName();
				String tempresult = null;//ÿ��filename�õ��Ľ��
				tempresult = annotate(files[i].getAbsolutePath());//�����inpath��һ���ļ���·��
				writetxt(filename+".txt",tempresult,outpath);
				System.out.println(filename+".txt�Ѿ�д�ã�");
			}
			else
			{
				System.out.println("ͼƬ�ļ��и�ʽ����ȷ");
			}
		}
	}
	/*��ע�ĺ���,���ص���һ���ļ���������ͼƬ�ı�ע�����label p��*/
	public String annotate(String inpath)
	{
//		String results=null;
		StringBuffer sb_result = new StringBuffer();
		inpath +="//";
		String [] imageTestNames = new File(inpath).list();//ݔ�����Ҫ��ע�ĈDƬ·��
		Vector<String> list = new Vector<String>();//list�з�jpg�ļ�������
		for(int i=0; i<imageTestNames.length; i++){
			if(imageTestNames[i].indexOf("jpeg")!=-1||imageTestNames[i].indexOf("jpg")!=-1){
					list.add(imageTestNames[i]);
			}
		}
				
				String []imageTestPathList = new String[list.size()];//imageTestPathList��jpg�ļ���·��
				for(int i=0; i<imageTestPathList.length; i++){
					imageTestPathList[i] = inpath + list.elementAt(i);
				}
				
				// load test images for annotation
				FileInputStream imageStream;//���ļ�������ͼƬ
				
//				TODO �@�e��Ҫ�ģ��DƬ��ĕr����������
				Vector<BufferedImage> imageTest = new Vector<BufferedImage>();//��ͼƬ��������������imageTest��
				for(String imagePath:imageTestPathList){
					try {
						imageStream = new FileInputStream(imagePath);
						BufferedImage bimg = ImageIO.read(imageStream);
						if(bimg == null){//���Ӧ���ǲ��ᷢ����
							System.out.println("null bufferedimage!");
						}
						imageTest.add(bimg);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						System.err.println("cannot open "+imagePath);
					} catch (IOException e1) {
						e1.printStackTrace();
						System.err.println("read image: "+imagePath+"  failed!");
					}					
				}
				
				// annotate
				try {
					Annotator annotator = new Annotator(featureTrainDirectory, labelTrainDirectory, imageTest, inpath, imageTestNames);
					/*������صľ���һ���ļ�����������ͼƬ�ı�ǩ+����
					 * ���ҵ�һ��λ����ͼƬ������
					 * */
					String[][] annoResult = annotator.annotate();
					
//					File folder = new File(outpath);
//					if(!folder.exists())
//						folder.mkdirs();
					for(int i =0;i<annoResult.length;i++)
					{
						/*����ͼƬtxt*/
						String filename=annoResult[i][0].substring(0,annoResult[i][0].lastIndexOf("."))+".txt";
						System.out.println(filename);
//						File file = new File(outpath+"\\"+filename);
//						FileWriter writer = new FileWriter(file);
						System.out.println("in test.java "+i+" "+annoResult[i][0]);
						for(int j=1;j<annoResult[i].length;j++)
						{
							sb_result.append(annoResult[i][j]+"\r\n");
//							writer.write(annoResult[i][j]+"\r\n");
							System.out.println("in test.java "+annoResult[i][j]);
						}
//						writer.flush();
//						writer.close();
					}
						
				} catch (IOException e1) {
					e1.printStackTrace();
					System.err.println("annotator initializion failed!");
				}
				
		return sb_result.toString();
	}
	
	/*�ѱ�ǵĽ��д��txt�ļ�*/
	public void writetxt(String filename,String results,String outpath) throws IOException
	{
		File file = new File(outpath+"\\"+filename);
		FileWriter writer = new FileWriter(file);
		writer.write(results);
		writer.flush();
		writer.close();
	}
	
}
