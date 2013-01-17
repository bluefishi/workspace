package com.swtdesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MultiTaskGUI {
	private Shell shell = null;
	private Table table = null;
	public MultiTaskGUI(){
		init();
	}
	public void init(){
		shell = new Shell();
		shell.setLayout(new GridLayout());
		shell.setText("多线程");
		Button bt = new Button(shell,SWT.NONE);
		bt.setText("开始一个任务");
		
		table = new Table(shell,SWT.BORDER);
		table.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		String[] header = new String[]{"任务","进度","操作"};
		for(int i =0;i<3;i++)
		{
			TableColumn col = new TableColumn(table,SWT.NONE);
			col.setText(header[i]);
		}
		table.getColumn(0).setWidth(80);
		table.getColumn(1).setWidth(150);
		table.getColumn(2).setWidth(80);
		shell.pack();
		
		bt.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				Task task = new Task(table);
				task.createTableItem();
				task.start();
			}
		});
	}
	public Shell getShell()
	{
		return shell;
	}
	public void setShell(Shell shell){
		this.shell = shell;
	}
	public Table getTable(){
		return table;
	}
	public void setTable(Table table ){
		this.table = table;
	}

}
