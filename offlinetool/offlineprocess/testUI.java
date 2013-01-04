package com.apple.video.offlineprocess;

import org.eclipse.swt.events.SelectionAdapter;

public class testUI {
	private Display display = Display.getDefault();
	private Shell shell = new Shell();
	private Task task = new Task(this);//Task Ϊ��̨������
	
	private String[] files;//��Ƶ�ļ�����
//	�����������Ϊ���ʵ������
	
	private Button addVideoButton;
	private Button proVideoButton;
	private Button buildIndexButton;
	
	private Button chooseFileButton;//ѡ�񱣴�·��
	private Text text;//�������ı���
	private Button startButton;//
	private Button stopButton;
	private ProgressBar progressBar;
	private Text consoleText;//���������Ϣ���ı���

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
		shell.setText("ȫ����Ƶ����");
		shell.setLayout(new GridLayout());
		
		//����ѡ��ť��
		Group group1 = new Group(shell,SWT.NONE);
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group1.setLayout(new GridLayout(3,false));
		addVideoButton = new Button(group1,SWT.PUSH);
		addVideoButton.setText("�����Ƶ");
		/*
		 *  �����Ƶ��ť�Ķ���
		 * ��FileDialog����
		 * */
		addVideoButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				startButton.setEnabled(true);
				FileDialog dlg = new FileDialog(shell,SWT.OPEN|SWT.MULTI);
				dlg.setFilterNames(new String[]{"��Ƶ�ļ�(*.mp4)","��Ƶ�ļ�(*.flv)","��Ƶ�ļ�(*.wmv)"});
				dlg.setFilterExtensions(new String[]{"*.mp4","*.flv","*.wmv","."});
				@SuppressWarnings("unused")
				String fileName = dlg.open();
				String[] fileNames = dlg.getFileNames();
//				System.out.println(fileName == null?"":fileName);
				files = new String[fileNames.length];
				for(int i=0;i<fileNames.length;i++)
				{
					//��Ƶ�ļ���������ȫ�ֱ�����
					System.out.println(dlg.getFilterPath()+"\\"+fileNames[i]);
					files[i] = dlg.getFilterPath()+"\\"+fileNames[i];
				}
					
					
			}
		});
		
		//TODO:������������Ļ�����ô��һ��û�д�������أ���ô��ѽ��
		proVideoButton = new Button(group1,SWT.PUSH);
		proVideoButton.setText("������Ƶ");
		proVideoButton.setEnabled(false);
		/*
		 *TODO�� ������Ƶ��ť�Ķ���
		 * */
		buildIndexButton = new Button(group1,SWT.PUSH);
		buildIndexButton.setText("��������");
		buildIndexButton.setEnabled(false);
		/*
		 * TODO������������ť�Ķ���
		 * */
		
		/*
		 * ����ִ��������
		 * */
		consoleText = new Text(shell,SWT.MULTI|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		consoleText.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		progressBar = new ProgressBar(shell,SWT.NONE);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		�����·��ѡ��
		Group group = new Group(shell, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(5,false));
				
		new Label(group, SWT.NONE).setText("���Ŀ¼��");
		text = new Text(group, SWT.BORDER);
		text.setText("C:/");
		text.setLayoutData(new GridData(100,-1));
//	TODO��·��У��	
		chooseFileButton = new Button(group,SWT.PUSH);
		chooseFileButton.setText("...");
		// ·��ѡ����
		chooseFileButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setText("ѡ���ļ���");
				dlg.setMessage("��ѡ�񱣴�λ�ã�");
				dlg.setFilterPath("c:/");
				String dir = dlg.open();
				if(dir!= null)
					text.setText(dir);
			}
			
//			
			
			
		});
		
		startButton = new Button(group, SWT.PUSH);
		startButton.setText("\u6267\u884C");
		startButton.setEnabled(false);
		/*
		 * ��ʼ��ť
		 * TODO��У��
		 * ����text��������ݺ��ʵ�ʱ���ٿ�ʼ
		 * */
		startButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setButtonState(false);
				final String outpath = text.getText();
				final int taskCount = files.length;
				progressBar.setMaximum(taskCount-1);//���ý������ĸ���
				new Thread(){//Ϊ��̨����һ�����߳�
					public void run(){
						task.start(files,outpath,taskCount);
					}
				}.start();
			}
		});
		//ֹͣ��ť
		stopButton = new Button(group, SWT.NONE);
		stopButton.setText("\u505C\u6B62");
		stopButton.setEnabled(false);
		stopButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				task.stop();//��̨����ֹͣ
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
	/*Ϊ��̨��ȡ��������ļ���get����*/
	public Shell getShell(){
		return shell;
	}
//	TODO����Text��һ��������
	public Text getConsoleText(){
		return consoleText;
	}
	public ProgressBar getProgressBar(){
		return progressBar;
	}
}
