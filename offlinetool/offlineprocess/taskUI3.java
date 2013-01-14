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
 * 0.ͼ��ģ����������
 * 1.�ȰѲ������ȥ��Ū��gridLayout  ok
 * 2.��������ť�±���stackLayout	
 * 3.��Ӱ�ť�¼�
 * */
public class taskUI3 {
	private Shell shell = new Shell();
	private Task3 task = new Task3(this);
	private TaskProcess taskprocess = new TaskProcess(this);
	private TaskIndex taskindex = new TaskIndex(this);
	
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
	private Button wav_check;//�Ƿ�Ҫ��������
	private Button pic_check;//�Ƿ�Ҫ����ͼƬ
	private Text psavepathtext;//�������ı���λ��
	private Label label_2;
	private Text indexsource;
	private Label lblIndex;
	private Text indexpath;
	private Button indexButton;
	private Button button;
	private Button btnRadioButton;
	private Label label_index;//index�����꣬����������
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
		
		
		composite = new Composite(shell, SWT.NONE);
		stacklayout = new StackLayout();
		composite.setLayout(stacklayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		/*�����ֲ�*/
		final Composite composite_0 = new Composite(composite,SWT.NONE);
		composite_0.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite_0.setLayout(new GridLayout(3,false));
		final Label label_intro = new Label(composite_0,SWT.NONE);
		label_intro.setText("����ָ��:");
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
		group_1.setLayout(new GridLayout(10,false));
		
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
				new Label(group_1, SWT.NONE);
				new Label(group_1, SWT.NONE);
				final Button startButton = new Button(group_1, SWT.NONE);
				GridData gd_startButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_startButton.widthHint = 64;
				startButton.setLayoutData(gd_startButton);
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
						GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_cancelButton.widthHint = 61;
						cancelButton.setLayoutData(gd_cancelButton);
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
		label_1.setText("ѡ���ܣ�");
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
		
		final Button pic_check = new Button(group_2, SWT.CHECK);//ͼ���ע�ĸ�ѡ��
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
		
		wav_check = new Button(group_2, SWT.CHECK);//����ʶ��ĸ�ѡ��
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
		pstartbutton.setText("��ʼ");
		//		pstartbutton.setEnabled(false);
				//TODO:����һ������ʶ��Ĺ��ܳ���
				pstartbutton.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
		//				TODO:У��·��
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
						else//ûѡ���ܣ�ʲô������
							return;
						
					
					}
				});
//		TagButton
	//composite_2	
	
		/*
		 * composite_3����Ĺ��ܽ���
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
		button.setText("�½������ļ�");
		button.setSelection(true);
//		btnRadioButton.setText("Radio Button");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		btnRadioButton = new Button(composite_3, SWT.RADIO);
		btnRadioButton.setText("������������׷�Ӄ���");
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
		indexButton.setText("��ʼ");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		label_index = new Label(composite_3, SWT.NONE);
		label_index.setText("\u7CFB\u7EDF\u4FE1\u606F\uFF1A");
		indexButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
//			TODO
				//����������ļ���raw�ļ�������������Ҫ����Щ�ļ����д�����
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
//						label_index.setText(label_index.getText()+"������������");
					}
				}.start();
//				TODO:���������з����ģ����߽��������Ѿ�������
//				label_index.setText(label_index.getText()+"������������");
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
	
	/*����·��*/
	private void setPaths(String dir)
	{
		savepathtext.setText(dir);
		pictext.setText(dir+"\\frames");
		wavtext.setText(dir+"\\wav");
		psavepathtext.setText(dir+"\\out");
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
	
	public String getPath(String textname)/*�õ�gui�е�·��������Task*/
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
	public boolean getCheck(String checkname)/*�õ�check��ֵ	 * ����ͨ����������ж�ĳһ��check�Ƿ�ѡ��*/
	{
		if(checkname.equals("wav_check"))
			return wav_check.getSelection();
		else if(checkname.equals("pic_check"))
			return pic_check.getSelection();
		else
			System.out.println("���������check�Ƿ�д��");
		return false;
	}
	
	/*����������ķ�������������û��ʹ��*/
	public void fromIndexLabel(String message){
		label_index.setText(label_index.getText()+message);
	}
}
