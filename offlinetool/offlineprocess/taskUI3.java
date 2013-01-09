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
 * 0.ͼ��ģ����������
 * 1.�ȰѲ������ȥ��Ū��gridLayout  ok
 * 2.��������ť�±���stackLayout	
 * 3.��Ӱ�ť�¼�
 * */
public class taskUI3 {
	private Shell shell = new Shell();
	private Task3 task = new Task3(this);
	private TaskProcess tprocess = new TaskProcess(this);
	
	private Table table;
	
	/*
	 * ��Ҫ���ݺ͹���ı���
	 * */
	private String[] files;//ѡ�����Ƶ�ļ�
	private boolean[] isDone;//�ļ���ȡͼƬ��������״̬
	
	
	/*
	 * ��������
	 * */
	private Button addVideoButton;//�����Ƶ�İ�ť
	private Button proVideoButton;//������Ƶ��ť����ע+����ʶ��+***
	private Button buildIndexButton;//�������İ�ť
	
	private Composite composite;//���˿��Ƶ�����
	private StackLayout stacklayout;//�˿���
	private Composite composite_1;//�����Ƶ��������
	private Composite composite_2;//��ע��Ƶ��������
	private Composite composite_3;//����������������
	
	private TableViewer tv;//��table��jface�ؼ�
	private ProgressBar progressBar;//������
	private Button cancelButton ;//ȡ����ť
	private Group group_2;
	private Button pstartbutton;//process����Ŀ�ʼ��ť
	private Label label_1;
	private Text savepathtext;//����ͼƬ����Ƶ��λ��
	private Text pictext;//��ע��picture��path
	private Text wavtext;//����ʶ����wav��path
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
		shell.setText("��Ƶ������");
		shell.setLayout(new GridLayout());
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(0, 10, 771, 70);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(3,false));
		
		/*������Ҫ�����İ�ť*/
		addVideoButton = new Button(group, SWT.NONE);
		addVideoButton.setText("\u6DFB\u52A0\u89C6\u9891");
		
		proVideoButton = new Button(group, SWT.NONE);
		proVideoButton.setText("������Ƶ");
		proVideoButton.setEnabled(false);
		
		buildIndexButton = new Button(group, SWT.NONE);
		buildIndexButton.setText("��������");
		buildIndexButton.setEnabled(false);
		
		/*������ť�Ĺ���*/
//		TODO��������ť�Ĺ���
		/*�����Ƶ��ť
		 * ��һ���ļ�ѡ����
		 * ��ѡ����ļ���table����ʾ����*/
		addVideoButton.addSelectionListener(new SelectionAdapter(){
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e){
				
				FileDialog dlg = new FileDialog(shell,SWT.OPEN|SWT.MULTI);
				dlg.setFilterNames(new String[]{"��Ƶ�ļ�(*.mp4)","��Ƶ�ļ�(*.flv)","��Ƶ�ļ�(*.wmv)"});
				dlg.setFilterExtensions(new String[]{"*.mp4","*.flv","*.wmv","."});
				@SuppressWarnings("unused")
				String fileName = dlg.open();
				String[] fileNames = dlg.getFileNames();
				files = new String[fileNames.length];
				isDone = new boolean[fileNames.length];
				
				/*stackLayout ��ʾ������ܽ���
				 * ������Ƚ�Ư����
				 * �����ͷ�Ῡ��һ��*/
				stacklayout.topControl = composite_1;
				composite.layout();
				
				/*  ����table���������� */
				List list = new ArrayList();
				for(int i=0;i<fileNames.length;i++)
				{
					//��Ƶ�ļ���������ȫ�ֱ�����
					{ 
					FileEntity o = new FileEntity();
					o.setId(new Long(i+1));// id�ֶε����ͱ��������Long������Ҫת��һ��
					o.setName(fileNames[i]);
					o.setDone(false);//��û�д�����
					list.add(o);
					}
					files[i] = dlg.getFilterPath()+"\\"+fileNames[i];
				}
				Object data = FileFactory.getPeoples(files,isDone);
				tv.setInput(data);//��������table����ʾ����
				
//				TODO:��ʱ�����ť��������,Ϊ�˲��Ժ�ߵĹ��ܷ���
				proVideoButton.setEnabled(true);
			}
		});
		
		/* ������Ƶ��ť
		 * �кܶ�ѡ�񣺱�ǩ+ASR
		 * */
		proVideoButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:1.�� composite_2�л�Щ�ؼ�
				/*stackLayout ��ʾ������ܽ���*/
				stacklayout.topControl = composite_2;
				composite.layout();
				buildIndexButton.setEnabled(true);
//					TODO:��ʱ�����ť��������
			}
		});
		
		/*������Ƶ��ť����ע������ʶ��*/
		buildIndexButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:
				/*stackLayout ��ʾ������ܽ���*/
				stacklayout.topControl = composite_3;
				composite.layout();
			}
		});
		
		//composite��ʢ�ų�����������button�������ж���������
//		TODO:��ʾһ����ʼ���棬д�ϲ����ֲ�
		composite = new Composite(shell, SWT.NONE);
		stacklayout = new StackLayout();
		composite.setLayout(stacklayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//composite_1,composite_2,composite_3 �Ƿ�����button��Ӧ�Ĺ��ܽ����
		/*
		 * composite_1����Ĺ��ܽ���
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
		//����table�ı�ͷ
		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("\u72B6\u6001");
		tLayout.addColumnData(new ColumnWeightData(40));
		new TableColumn(table, SWT.NONE).setText("\u76EE\u6807\u6587\u4EF6");
		tLayout.addColumnData(new ColumnWeightData(40));
		new TableColumn(table, SWT.NONE).setText("\u5904\u7406\u8FDB\u5EA6");
		// �����������ͱ�ǩ��
		tv.setContentProvider(new TableViewerContentProvider());
		tv.setLabelProvider(new TableViewerLabelProvider());
		
				
		progressBar = new ProgressBar(composite_1, SWT.NONE);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group_1.setLayout(new GridLayout(8,false));
		
		Label label = new Label(group_1, SWT.NONE);
		label.setText("\u8F93\u51FA\u8DEF\u5F84\uFF1A");
		
		//����·��
		savepathtext = new Text(group_1, SWT.BORDER);
		savepathtext.setText("C:");
		savepathtext.setLayoutData(new GridData(208,-1));
		
		/*
		 * ��ť��ѡ�񱣴�·��
		 * ��һ���ļ���ѡ����
		 * ����ѡ���·�������text����ʾ����
		 * */
		Button chooseFileButton = new Button(group_1, SWT.NONE);
		chooseFileButton.setText("...");
		chooseFileButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setText("ѡ���ļ���");
				dlg.setMessage("��ѡ�񱣴�λ�ã�");
				dlg.setFilterPath("D:\\Data\\testresult");
				String dir = dlg.open();
				if(dir!= null)
//					savepathtext.setText(dir);
					setPaths(dir);//����·����������������text�����д�����Ƶ���������·��
			}
		});
		
		/*
		 * ��ʼ��ť����ʼ��ȡͼƬ������
		 * */
		
		/*
		 * ȡ����ť������һ���Ի���ѯ���Ƿ�Ҫȡ������
		 * ͣ�����񣬽�table�б����
		 * */
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		final Button startButton = new Button(group_1, SWT.NONE);
		startButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				setButtonState(false);
//				TODO:�����и�ҵ���߼���Ҫ��ƣ������ʼ֮�����ô��Ӱ�������İ�ť
				startButton.setEnabled(false);
				final String outpath = savepathtext.getText();//��ȡ����·��
				final int taskCount = files.length;//Ҫ������ļ�������
				progressBar.setMaximum(taskCount);//���ý������ĸ���
				new Thread(){//Ϊ��̨����һ�����߳�
					public void run(){
						task.start(files,outpath,taskCount);
					}
				}.start();
				cancelButton.setEnabled(true);
			}
		});
		startButton.setText("\u5F00\u59CB");
		//		TODO:����ȡ�����������⣬��������ͣ��������Ҫ���Ǹ��߳��ܺ��˲ſ�����Ӧ����¼�
				cancelButton = new Button(group_1, SWT.NONE);
				cancelButton.setText("\u53D6\u6D88");
				cancelButton.setEnabled(false);
				cancelButton.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
//				void openWarning(Shell parent,String title,String message);
						boolean b = MessageDialog.openConfirm(shell, "����", "��ȷ��Ҫȡ����");
						if(b)
							task.stop();
						else
							System.out.println("�Ҳ�����ȡ��");
					}
				});
		}
		
		/*
		 * composite_2����Ĺ��ܽ���
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
		//TODO:����һ������ʶ��Ĺ��ܳ���
		pstartbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
//				TODO:У��·��
				final String inpath = wavtext.getText();
				final String outpath = psavepathtext.getText();
				new Thread(){//Ϊ��̨����һ�����߳�
					public void run(){
						tprocess.start(inpath, outpath);
					}
				}.start();
			}
		});
//		TagButton
	//composite_2	
	
		/*
		 * composite_3����Ĺ��ܽ���
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
	
	/*����·��*/
	private void setPaths(String dir)
	{
//		 savepathtext.getText()+"processresult";
		savepathtext.setText(dir);
		pictext.setText(dir+"\\frames");
		wavtext.setText(dir+"\\wav");
		psavepathtext.setText(dir+"\\out");
	}
	/*�õ�gui�е�·��������Task*/
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
	
	/*����processButton*/
	public void setprocessBtnState(boolean bFlag)
	{
		proVideoButton.setEnabled(bFlag);
	}
	public void setcancelBtnState(boolean bFlag)
	{
		cancelButton.setEnabled(bFlag);
	}
	/*Ϊ��̨��ȡ��������ļ���get����*/
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
