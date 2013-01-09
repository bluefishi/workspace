package com.apple.video.offlineprocess;

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
	private TaskProcess tprocess = new TaskProcess(this);
	
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
	private Text psavepathtext;
	
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
		shell.setSize(819, 469);
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
		
		//composite是盛放除了上面三个button以外所有东西的容器
//		TODO:显示一个初始界面，写上操作手册
		composite = new Composite(shell, SWT.NONE);
		stacklayout = new StackLayout();
		composite.setLayout(stacklayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
		group_1.setLayout(new GridLayout(8,false));
		
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
		final Button startButton = new Button(group_1, SWT.NONE);
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
		composite_2.setLayout(new GridLayout(4,false));
		
		group_2 = new Group(composite_2, SWT.NONE);
		GridData gd_group_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2.widthHint = 372;
		gd_group_2.heightHint = 274;
		group_2.setLayoutData(gd_group_2);
		
		label_1 = new Label(group_2, SWT.NONE);
		label_1.setBounds(23, 28, 61, 17);
		label_1.setText("\u9009\u62E9\uFF1A");
		
		Button button_1 = new Button(group_2, SWT.CHECK);
		button_1.setBounds(40, 66, 69, 17);
		button_1.setText("\u56FE\u50CF\u6807\u6CE8");
		
		Button button_2 = new Button(group_2, SWT.CHECK);
		button_2.setBounds(40, 102, 69, 17);
		button_2.setText("\u8BED\u97F3\u8BC6\u522B");
		
		pictext = new Text(group_2, SWT.BORDER);
		pictext.setBounds(118, 60, 147, 23);
		String picpath = savepathtext.getText()+"\\frames";
		pictext.setText(picpath);
		
		wavtext = new Text(group_2, SWT.BORDER);
		wavtext.setBounds(118, 96, 147, 23);
		String wavpath = savepathtext.getText()+"\\wav";
		wavtext.setText(wavpath);
		
		Label label = new Label(group_2, SWT.NONE);
		label.setBounds(40, 171, 61, 17);
		label.setText("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		
		psavepathtext = new Text(group_2, SWT.BORDER);
		psavepathtext.setBounds(118, 171, 152, 23);
		String psavepath = savepathtext.getText()+"\\processresult";
		psavepathtext.setText(psavepath);
		
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		Label lblNewLabel = new Label(composite_2, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 304;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("New Label");
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		pstartbutton = new Button(composite_2, SWT.NONE);
		pstartbutton.setText("\u5F00\u59CB\u6807\u6CE8");
//		pstartbutton.setEnabled(false);
		//TODO:先做一个语音识别的功能出来
		pstartbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:校验路径
				final String inpath = wavtext.getText();
				final String outpath = psavepathtext.getText();
				new Thread(){//为后台开启一个新线程
					public void run(){
						tprocess.start(inpath, outpath);
					}
				}.start();
			}
		});
//		TagButton
	//composite_2	
	
		/*
		 * composite_3下面的功能界面
		 * */
		composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_3.setLayout(new GridLayout(1,false));//table,progressbar,group
		
		
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
//		 savepathtext.getText()+"processresult";
		savepathtext.setText(dir);
		pictext.setText(dir+"\\frames");
		wavtext.setText(dir+"\\wav");
		psavepathtext.setText(dir+"\\out");
	}
	/*得到gui中的路径，传给Task*/
	public String getPath(String textname)
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
}
