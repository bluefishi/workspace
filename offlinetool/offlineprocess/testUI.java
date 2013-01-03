package com.apple.video.offlineprocess;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class testUI {
	private Display display = Display.getDefault();
	private Shell shell = new Shell();
	private Task task = new Task(this);//Task 为后台处理类
	
//	private String filefolder;//视频文件夹   见 filechoice
	private String[] files;//视频文件集合
//	将界面组件设为类的实例变量
	
	private Button addVideoButton;
	private Button proVideoButton;
	private Button buildIndexButton;
	
	private Button chooseFileButton;//选择保存路径
	private Text text;//任务数文本框
	private Button startButton;//
	private Button stopButton;
	private ProgressBar progressBar;
	private Text consoleText;//输出调试信息的文本框

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testUI window = new testUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		shell.setSize(470, 341);
		shell.setText("全能视频处理");
		shell.setLayout(new GridLayout());
		
		//任务选择按钮组
		Group group1 = new Group(shell,SWT.NONE);
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group1.setLayout(new GridLayout(3,false));
		addVideoButton = new Button(group1,SWT.PUSH);
		addVideoButton.setText("添加视频");
		/*
		 *  添加视频按钮的动作
		 * 用FileDialog好了
		 * */
		addVideoButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				startButton.setEnabled(true);
//				DirectoryDialog dlg = new DirectoryDialog(shell);
//				dlg.setText("打开");
//				dlg.setMessage("选择视频文件夹：");
//				dlg.setFilterPath("c:/");
//				String dir = dlg.open();
//				if(dir!= null)
//				{
//					filefolder = dir;
//					System.out.println(dir);
//				}
//		TODO: file choice		
				
//				这个file写得挺美的，但是与后边的程序不符
				FileDialog dlg = new FileDialog(shell,SWT.OPEN|SWT.MULTI);
				dlg.setFilterNames(new String[]{"视频文件(*.mp4)","视频文件(*.flv)","视频文件(*.wmv)"});
				dlg.setFilterExtensions(new String[]{"*.mp4","*.flv","*.wmv","."});
				@SuppressWarnings("unused")
				String fileName = dlg.open();
				String[] fileNames = dlg.getFileNames();
//				System.out.println(fileName == null?"":fileName);
				files = new String[fileNames.length];
				for(int i=0;i<fileNames.length;i++)
				{
					//视频文件名，放在全局变量里
					System.out.println(dlg.getFilterPath()+"\\"+fileNames[i]);
					files[i] = dlg.getFilterPath()+"\\"+fileNames[i];
				}
					
					
			}
		});
		
		//TODO:可是如果这样的话，那么上一次没有处理完的呢？怎么办呀？
		proVideoButton = new Button(group1,SWT.PUSH);
		proVideoButton.setText("处理视频");
		proVideoButton.setEnabled(false);
		/*
		 *TODO： 处理视频按钮的动作
		 * */
		buildIndexButton = new Button(group1,SWT.PUSH);
		buildIndexButton.setText("建立索引");
		buildIndexButton.setEnabled(false);
		/*
		 * TODO：建立索引按钮的动作
		 * */
		
		consoleText = new Text(shell,SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
		consoleText.setLayoutData(new GridData(GridData.FILL_BOTH));
		progressBar = new ProgressBar(shell,SWT.NONE);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		任务的路径选择
		Group group = new Group(shell, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(5,false));
				
		new Label(group, SWT.NONE).setText("输出目录：");
		text = new Text(group, SWT.BORDER);
		text.setText("C:/");
		text.setLayoutData(new GridData(100,-1));
//	TODO：路径校验	
		chooseFileButton = new Button(group,SWT.PUSH);
		chooseFileButton.setText("...");
		// 路径选择器
		chooseFileButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setText("选择文件夹");
				dlg.setMessage("请选择保存位置：");
				dlg.setFilterPath("c:/");
				String dir = dlg.open();
				if(dir!= null)
					text.setText(dir);
//					System.out.println(dir);
			}
			
//			
			
			
		});
		
		startButton = new Button(group, SWT.PUSH);
		startButton.setText("\u6267\u884C");
		startButton.setEnabled(false);
		/*
		 * 开始按钮
		 * TODO：考虑text里面的内容合适的时候再开始
		 * */
		startButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setButtonState(false);
//				final String filefolder = files[0].substring(0,files[0].lastIndexOf("/"));
//				System.out.println(filefolder);
				final String outpath = text.getText();
				final int taskCount = files.length;
				//设置进度条的格数
				progressBar.setMaximum(taskCount-1);
				consoleText.insert("后台处理线程开始启动......\n");
				//为后台开启一个新线程
				new Thread(){
					public void run(){
//						TODO: 工作线程，需要 filepath / outputpath
						task.start(files,outpath,taskCount);
					}
					
				}.start();
				consoleText.insert("后台处理线程启动结束\n");
			}
		});
//		startButton.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e){
//				setButtonState(false);
//				String str = text.getText();
//				final int taskCount = new Integer(str).intValue();
//				//设置进度条的格数
//				progressBar.setMaximum(taskCount-1);
//				consoleText.insert("后台处理线程开始启动......\n");
//				//为后台开启一个新线程
//				new Thread(){
//					public void run(){
//						task.start(taskCount);
//					}
//					
//				}.start();
//				consoleText.insert("后台处理线程启动结束\n");
//			}
//		});
		//停止按钮
		stopButton = new Button(group, SWT.NONE);
		stopButton.setText("\u505C\u6B62");
		stopButton.setEnabled(false);
		stopButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				task.stop();//后台任务停止
				
			}
		});
		shell.layout();
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
	}
	public void setButtonState(boolean bFlag){
		startButton.setEnabled(bFlag);
		stopButton.setEnabled(!bFlag);
	}
	/*为后台类取界面组件的几个get方法*/
	public Shell getShell(){
		return shell;
	}
	public Text getConsoleText(){
		return consoleText;
	}
	public ProgressBar getProgressBar(){
		return progressBar;
	}
}
