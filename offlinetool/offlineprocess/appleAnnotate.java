package com.apple.video.offlineprocess.Annotation;

/*input：file folder or file
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
	/*load参数*/
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
		/*传进来的是一个包含很多子文件夹的folder*/
		File folder = new File(inpath);
		File[] files = folder.listFiles();//这个files[i]是直接包含图片的子文件夹
		for(int i =0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				String filename = null;//这个filename是要写成txt的，也是子文件夹的名字
				filename = files[i].getName();
				String tempresult = null;//每个filename得到的结果
				tempresult = annotate(files[i].getAbsolutePath());//这里的inpath是一个文件夹路径
				writetxt(filename+".txt",tempresult,outpath);
				System.out.println(filename+".txt已经写好！");
			}
			else
			{
				System.out.println("图片文件夹格式不正确");
			}
		}
	}
	/*标注的函数,返回的是一个文件夹下所有图片的标注结果（label p）*/
	public String annotate(String inpath)
	{
//		String results=null;
		StringBuffer sb_result = new StringBuffer();
		inpath +="//";
		String [] imageTestNames = new File(inpath).list();//輸入的需要標注的圖片路徑
		Vector<String> list = new Vector<String>();//list中放jpg文件的名字
		for(int i=0; i<imageTestNames.length; i++){
			if(imageTestNames[i].indexOf("jpeg")!=-1||imageTestNames[i].indexOf("jpg")!=-1){
					list.add(imageTestNames[i]);
			}
		}
				
				String []imageTestPathList = new String[list.size()];//imageTestPathList放jpg文件的路径
				for(int i=0; i<imageTestPathList.length; i++){
					imageTestPathList[i] = inpath + list.elementAt(i);
				}
				
				// load test images for annotation
				FileInputStream imageStream;//用文件流来读图片
				
//				TODO 這裡需要改，圖片多的時候容易死掉
				Vector<BufferedImage> imageTest = new Vector<BufferedImage>();//把图片都读进来，放在imageTest中
				for(String imagePath:imageTestPathList){
					try {
						imageStream = new FileInputStream(imagePath);
						BufferedImage bimg = ImageIO.read(imageStream);
						if(bimg == null){//这个应该是不会发生的
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
					/*这个返回的就是一个文件夹下面所有图片的标签+概率
					 * 而且第一个位置是图片的名字
					 * */
					String[][] annoResult = annotator.annotate();
					
//					File folder = new File(outpath);
//					if(!folder.exists())
//						folder.mkdirs();
					for(int i =0;i<annoResult.length;i++)
					{
						/*创建图片txt*/
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
	
	/*把标记的结果写进txt文件*/
	public void writetxt(String filename,String results,String outpath) throws IOException
	{
		File file = new File(outpath+"\\"+filename);
		FileWriter writer = new FileWriter(file);
		writer.write(results);
		writer.flush();
		writer.close();
	}
	
}
