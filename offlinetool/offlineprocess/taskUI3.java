package com.apple.video.offlineprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;


/*
 * 0.图的模样画出来了
 * 1.先把布局填进去，弄成gridLayout  ok
 * 2.在三个按钮下边是stackLayout	
 * 3.添加按钮事件
 * */
public class taskUI3 {
	private Shell shell = new Shell();
	private Task3 task = new Task3(this);
	private TaskProcess taskprocess = new TaskProcess(this);
	private TaskIndex taskindex = new TaskIndex(this);
	
	private Table table;
	
	/*
	 * 需要传递和共享的变量
	 * */
	private String[] files;//选择的视频文件
	private boolean[] isDone;//文件抽取图片和声音的状态
	
	
	/*
	 * 界面的组件
	 * */
	private Button addVideoButton;//添加视频的按钮
	private Button proVideoButton;//处理视频按钮：标注+语音识别+***
	private Button buildIndexButton;//建索引的按钮
	
	private Composite composite;//放扑克牌的容器
	private StackLayout stacklayout;//扑克牌
	private Composite composite_1;//添加视频的那张牌
	private Composite composite_2;//标注视频的那张牌
	private Composite composite_3;//建立索引的那张牌
	
	private TableViewer tv;//放table的jface控件
	private ProgressBar progressBar;//进度条
	private Button cancelButton ;//取消按钮
	private Group group_2;
	private Button pstartbutton;//process那里的开始按钮
	private Label label_1;
	private Text savepathtext;//保存图片和视频的位置
	private Text pictext;//标注是picture的path
	private Text wavtext;//语音识别是wav的path
	private Button wav_check;//是否要处理语音
	private Button pic_check;//是否要处理图片
	private Text psavepathtext;//处理结果的保存位置
	private Label label_2;
	private Text indexsource;
	private Label lblIndex;
	private Text indexpath;
	private Button indexButton;
	private Button button;
	private Button btnRadioButton;
	private Label label_index;//index建立完，反馈给界面
	private Label label_3;
	private Label label_4;
	private Label label_5;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			taskUI3 window = new taskUI3();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
//		Shell shell = new Shell();
		shell.setSize(660, 444);
		shell.setText("视频处理工具");
		shell.setLayout(new GridLayout());
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(0, 10, 771, 70);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(3,false));
		
		/*三个主要工作的按钮*/
		addVideoButton = new Button(group, SWT.NONE);
		addVideoButton.setText("\u6DFB\u52A0\u89C6\u9891");
		
		proVideoButton = new Button(group, SWT.NONE);
		proVideoButton.setText("处理视频");
		proVideoButton.setEnabled(false);
		
		buildIndexButton = new Button(group, SWT.NONE);
		buildIndexButton.setText("建立索引");
		buildIndexButton.setEnabled(false);
		
		/*三个按钮的功能*/
//		TODO：三个按钮的功能
		/*添加视频按钮
		 * 打开一个文件选择器
		 * 把选择的文件在table中显示出来*/
		addVideoButton.addSelectionListener(new SelectionAdapter(){
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e){
				
				FileDialog dlg = new FileDialog(shell,SWT.OPEN|SWT.MULTI);
				dlg.setFilterNames(new String[]{"视频文件(*.mp4)","视频文件(*.flv)","视频文件(*.wmv)"});
				dlg.setFilterExtensions(new String[]{"*.mp4","*.flv","*.wmv","."});
				@SuppressWarnings("unused")
				String fileName = dlg.open();
				String[] fileNames = dlg.getFileNames();
				files = new String[fileNames.length];
				isDone = new boolean[fileNames.length];
				
				/*stackLayout 显示这个功能界面
				 * 放这儿比较漂亮，
				 * 放在最开头会咯噔一下*/
				stacklayout.topControl = composite_1;
				composite.layout();
				
				/*  放在table里面做数据 */
				List list = new ArrayList();
				for(int i=0;i<fileNames.length;i++)
				{
					//视频文件名，放在全局变量里
					{ 
					FileEntity o = new FileEntity();
					o.setId(new Long(i+1));// id字段的类型被定义成了Long，所以要转化一下
					o.setName(fileNames[i]);
					o.setDone(false);//还没有处理完
					list.add(o);
					}
					files[i] = dlg.getFilterPath()+"\\"+fileNames[i];
				}
				Object data = FileFactory.getPeoples(files,isDone);
				tv.setInput(data);//把数据在table中显示出来
				
//				TODO:暂时将激活按钮放在这里,为了测试后边的功能方便
				proVideoButton.setEnabled(true);
			}
		});
		
		/* 处理视频按钮
		 * 有很多选择：标签+ASR
		 * */
		proVideoButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:1.在 composite_2中画些控件
				/*stackLayout 显示这个功能界面*/
				stacklayout.topControl = composite_2;
				composite.layout();
				buildIndexButton.setEnabled(true);
//					TODO:暂时将激活按钮放在这里
			}
		});
		
		/*处理视频按钮（标注，语音识别）*/
		buildIndexButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:
				/*stackLayout 显示这个功能界面*/
				stacklayout.topControl = composite_3;
				composite.layout();
			}
		});
		
		
		composite = new Composite(shell, SWT.NONE);
		stacklayout = new StackLayout();
		composite.setLayout(stacklayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		/*操作手册*/
		final Composite composite_0 = new Composite(composite,SWT.NONE);
		composite_0.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_0.setLayout(new GridLayout(3,false));
		final Label label_intro = new Label(composite_0,SWT.NONE);
		label_intro.setText("操作指南:");
		composite_0.layout();
		stacklayout.topControl=composite_0;
		new Label(composite_0, SWT.NONE);
		new Label(composite_0, SWT.NONE);
		new Label(composite_0, SWT.NONE);
		new Label(composite_0, SWT.NONE);
		
		label_3 = new Label(composite_0, SWT.NONE);
		label_3.setText("1. \u70B9\u51FB\u201C\u6DFB\u52A0\u89C6\u9891\u201D\u6309\u94AE\uFF0C\u6DFB\u52A0\u89C6\u9891\u5E76\u62BD\u53D6\u5173\u952E\u5E27\u548C\u97F3\u9891\u3002");
		new Label(composite_0, SWT.NONE);
		new Label(composite_0, SWT.NONE);
		
		label_4 = new Label(composite_0, SWT.NONE);
		label_4.setText("2. \u70B9\u51FB\u201C\u5904\u7406\u89C6\u9891\u201D\u6309\u94AE\uFF0C\u6807\u6CE8\u56FE\u7247\uFF0C\u5BF9\u89C6\u9891\u8FDB\u884C\u8BED\u97F3\u8BC6\u522B\u3002");
		new Label(composite_0, SWT.NONE);
		new Label(composite_0, SWT.NONE);
		
		label_5 = new Label(composite_0, SWT.NONE);
		label_5.setText("3. \u70B9\u51FB\u201D\u5EFA\u7ACB\u7D22\u5F15\u201C\u6309\u94AE\uFF0C\u9009\u62E9\u7D22\u5F15\u6E90\u6587\u4EF6\uFF0C\u5BF9\u6587\u672C\u6587\u4EF6\u5EFA\u7ACB\u7D22\u5F15\u3002");
		composite.layout();
		
		
		//composite_1,composite_2,composite_3 是放三个button对应的功能界面的
		/*
		 * composite_1下面的功能界面
		 * */
		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_1.setLayout(new GridLayout(1,false));//table,progressbar,group
		{
		tv = new TableViewer(composite_1,SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION);
		table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableLayout tLayout = new TableLayout();
		table.setLayout(tLayout);
		//设置table的表头
		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("\u72B6\u6001");
		tLayout.addColumnData(new ColumnWeightData(40));
		new TableColumn(table, SWT.NONE).setText("\u76EE\u6807\u6587\u4EF6");
		tLayout.addColumnData(new ColumnWeightData(40));
		new TableColumn(table, SWT.NONE).setText("\u5904\u7406\u8FDB\u5EA6");
		// 设置内容器和标签器
		tv.setContentProvider(new TableViewerContentProvider());
		tv.setLabelProvider(new TableViewerLabelProvider());
		
				
		progressBar = new ProgressBar(composite_1, SWT.NONE);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group_1.setLayout(new GridLayout(10,false));
		
		Label label = new Label(group_1, SWT.NONE);
		label.setText("\u8F93\u51FA\u8DEF\u5F84\uFF1A");
		
		//保存路径
		savepathtext = new Text(group_1, SWT.BORDER);
		savepathtext.setText("C:");
		savepathtext.setLayoutData(new GridData(208,-1));
		
		/*
		 * 按钮：选择保存路径
		 * 打开一个文件夹选择器
		 * 并把选择的路径在左侧text中显示出来
		 * */
		Button chooseFileButton = new Button(group_1, SWT.NONE);
		chooseFileButton.setText("...");
		chooseFileButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setText("选择文件夹");
				dlg.setMessage("请选择保存位置：");
				dlg.setFilterPath("D:\\Data\\testresult");
				String dir = dlg.open();
				if(dir!= null)
//					savepathtext.setText(dir);
					setPaths(dir);//设置路径，包括这个卡里的text，还有处理视频里面的三个路径
			}
		});
		
		/*
		 * 开始按钮：开始抽取图片和声音
		 * */
		
		/*
		 * 取消按钮：弹出一个对话框，询问是否要取消任务
		 * 停掉任务，将table列表清空
		 * */
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
				new Label(group_1, SWT.NONE);
				new Label(group_1, SWT.NONE);
				final Button startButton = new Button(group_1, SWT.NONE);
				GridData gd_startButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_startButton.widthHint = 64;
				startButton.setLayoutData(gd_startButton);
				startButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
//				setButtonState(false);
//				TODO:这里有个业务逻辑需要设计，点击开始之后会怎么样影响其他的按钮
						startButton.setEnabled(false);
						final String outpath = savepathtext.getText();//获取保存路径
						final int taskCount = files.length;//要处理的文件的总数
						progressBar.setMaximum(taskCount);//设置进度条的格数
						new Thread(){//为后台开启一个新线程
							public void run(){
								task.start(files,outpath,taskCount);
							}
						}.start();
						cancelButton.setEnabled(true);
					}
				});
				startButton.setText("\u5F00\u59CB");
				//		TODO:现在取消还是有问题，不能立刻停下来，需要等那个线程跑好了才可以响应这个事件
						cancelButton = new Button(group_1, SWT.NONE);
						GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_cancelButton.widthHint = 61;
						cancelButton.setLayoutData(gd_cancelButton);
						cancelButton.setText("\u53D6\u6D88");
						cancelButton.setEnabled(false);
						cancelButton.addSelectionListener(new SelectionAdapter(){
							public void widgetSelected(SelectionEvent e){
//				void openWarning(Shell parent,String title,String message);
								boolean b = MessageDialog.openConfirm(shell, "标题", "你确定要取消吗？");
								if(b)
									task.stop();
								else
									System.out.println("我不是想取消");
							}
						});
		}
		
		/*
		 * composite_2下面的功能界面
		 * */
		//composite_2
		composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_2.setLayout(new GridLayout(1,false));
		
		group_2 = new Group(composite_2, SWT.NONE);
//		GridData gd_group_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		gd_group_2.widthHint = 372;
//		gd_group_2.heightHint = 274;
//		group_2.setLayoutData(gd_group_2);
		group_2.setLayoutData(new GridData(GridData.FILL_BOTH));
		group_2.setLayout(new GridLayout(5,false));
		
		label_1 = new Label(group_2, SWT.NONE);
//		label_1.setLayoutData(new GridData(GridData.CENTER));
//		label_1.setBounds(23, 28, 61, 17);
		label_1.setText("选择功能：");
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
//		pictext.setBounds(118, 60, 210, 23);
		String picpath = savepathtext.getText()+"\\frames";
//		wavtext.setBounds(118, 96, 210, 23);
		String wavpath = savepathtext.getText()+"\\wav";
//		psavepathtext.setBounds(118, 171, 210, 23);
		String psavepath = savepathtext.getText()+"\\processresult";
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		final Button pic_check = new Button(group_2, SWT.CHECK);//图像标注的复选框
		pic_check.setSelection(true);
		//		pic_check.setLayoutData(new GridData(GridData.BEGINNING));
		//		pic_check.setBounds(40, 66, 69, 17);
				pic_check.setText("\u56FE\u50CF\u6807\u6CE8");

		pictext = new Text(group_2, SWT.BORDER);
		GridData gd_pictext = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_pictext.widthHint = 152;
		pictext.setLayoutData(gd_pictext);
		pictext.setText(picpath);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		wav_check = new Button(group_2, SWT.CHECK);//语音识别的复选框
		wav_check.setSelection(true);
		//		wav_check.setBounds(40, 102, 69, 17);
				wav_check.setText("\u8BED\u97F3\u8BC6\u522B");
		
		wavtext = new Text(group_2, SWT.BORDER);
		GridData gd_wavtext = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_wavtext.widthHint = 150;
		wavtext.setLayoutData(gd_wavtext);
		wavtext.setText(wavpath);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		Label label = new Label(group_2, SWT.NONE);
		//		label.setBounds(40, 171, 61, 17);
				label.setText("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		
		psavepathtext = new Text(group_2, SWT.BORDER);
		GridData gd_psavepathtext = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_psavepathtext.widthHint = 149;
		psavepathtext.setLayoutData(gd_psavepathtext);
		psavepathtext.setText(psavepath);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		pstartbutton = new Button(group_2, SWT.NONE);
		pstartbutton.setText("开始");
		//		pstartbutton.setEnabled(false);
				//TODO:先做一个语音识别的功能出来
				pstartbutton.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
		//				TODO:校验路径
						final String wavinpath = wavtext.getText();
						final String picinpath = pictext.getText();
						final String outpath = psavepathtext.getText();
//						int choice = 0;
						if(wav_check.getSelection() && pic_check.getSelection())
						{
//							choice = 1;
							new Thread(){
								public void run()
								{
									try{
										taskprocess.start(wavinpath,picinpath, outpath,1);
									}catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							}.start();
						}
							
						else if(wav_check.getSelection())
						{
//							choice = 2;
							new Thread(){
								public void run()
								{
									try{
										taskprocess.start(wavinpath,picinpath, outpath,2);
									}catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							}.start();
						}
						else if(pic_check.getSelection())
						{
//							choice = 3;
							new Thread(){
								public void run()
								{
									try{
										taskprocess.start(wavinpath,picinpath, outpath,3);
									}catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							}.start();
						}
						else//没选功能，什么都不做
							return;
						
					
					}
				});
//		TagButton
	//composite_2	
	
		/*
		 * composite_3下面的功能界面
		 * */
		//composite_3
		composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_3.setLayout(new GridLayout(4,false));//table,progressbar,group
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		button = new Button(composite_3, SWT.RADIO);
		button.setText("新建索引文件");
		button.setSelection(true);
//		btnRadioButton.setText("Radio Button");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		btnRadioButton = new Button(composite_3, SWT.RADIO);
		btnRadioButton.setText("向已有索引中追加內容");
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		label_2 = new Label(composite_3, SWT.NONE);
		label_2.setText("\u9009\u62E9\u6E90\u6587\u4EF6\uFF1A");
		
		indexsource = new Text(composite_3, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 224;
		indexsource.setLayoutData(gd_text);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		lblIndex = new Label(composite_3, SWT.NONE);
		lblIndex.setText("index\u4F4D\u7F6E\uFF1A");
		
		indexpath = new Text(composite_3, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_1.widthHint = 224;
		indexpath.setLayoutData(gd_text_1);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		indexButton = new Button(composite_3, SWT.NONE);
		GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button.widthHint = 73;
		indexButton.setLayoutData(gd_button);
		indexButton.setText("开始");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		label_index = new Label(composite_3, SWT.NONE);
		label_index.setText("\u7CFB\u7EDF\u4FE1\u606F\uFF1A");
		indexButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
//			TODO
				//索引传入的文件是raw文件，在索引里面要对这些文件进行处理先
				final String sourcePath = indexsource.getText();//"";
				final String indexPath = indexpath.getText();//"";
				System.out.println("indexsource:"+indexsource);
				System.out.println("indexpath:"+indexpath);
				
				new Thread(){
					public void run(){
						try {
							taskindex.start(sourcePath,indexPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						label_index.setText(label_index.getText()+"索引建好啦！");
					}
				}.start();
//				TODO:索引建好有反馈的，告诉界面索引已经建好了
//				label_index.setText(label_index.getText()+"索引建好啦！");
			}
		});
		
		//composite_3
		
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/*设置路径*/
	private void setPaths(String dir)
	{
		savepathtext.setText(dir);
		pictext.setText(dir+"\\frames");
		wavtext.setText(dir+"\\wav");
		psavepathtext.setText(dir+"\\out");
	}
	
	
	
	/*控制processButton*/
	public void setprocessBtnState(boolean bFlag)
	{
		proVideoButton.setEnabled(bFlag);
	}
	public void setcancelBtnState(boolean bFlag)
	{
		cancelButton.setEnabled(bFlag);
	}
	/*为后台类取界面组件的几个get方法*/
	public Shell getShell(){
		return shell;
	}
	public TableViewer getConsoleTableViewer(){
		return tv;
	}
	public ProgressBar getProgressBar(){
		return progressBar;
	}
	
	public String getPath(String textname)/*得到gui中的路径，传给Task*/
	{
		if(textname.equals("wavtext"))
			return wavtext.getText();
		else if(textname.equals("pictext"))
			return pictext.getText();
		else if(textname.equals("psavepathtext"))
			return psavepathtext.getText();
		else
			return null;
	}
	public boolean getCheck(String checkname)/*得到check的值	 * 想是通过这个函数判断某一个check是否被选中*/
	{
		if(checkname.equals("wav_check"))
			return wav_check.getSelection();
		else if(checkname.equals("pic_check"))
			return pic_check.getSelection();
		else
			System.out.println("请检查代码中check是否写错");
		return false;
	}
	
	/*索引建立完的反馈，但是现在没有使用*/
	public void fromIndexLabel(String message){
		label_index.setText(label_index.getText()+message);
	}
}
