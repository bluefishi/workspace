package com.swtdesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Task extends Thread{
	private Table table = null;
	private boolean done = false;
	private ProgressBar bar = null;
	private int min = 0;
	private int max = 100;
	public Task(Table table)
	{
		this.table =table;
	}
	public void createTableItem(){
		TableItem item = new TableItem(table,SWT.NONE);
		item.setText(this.getName());
//		item.setImage(ImageFactory.loadImage(table.getDisplay(),ImageFactory.PROGRESS_TASK));
		
		bar = new ProgressBar(table,SWT.NONE);
		bar.setMaximum(min);
		bar.setMaximum(max);
		
		TableEditor editor = new TableEditor(table);
		editor.grabHorizontal= true;
		editor.grabVertical = true;
		
		editor.setEditor(bar,item,1);
		
		editor = new TableEditor(table);
		editor.grabHorizontal=true;
		editor.grabVertical= true;
		
		Button stop = new Button(table,SWT.NONE);
		stop.setText("stop");
		editor.setEditor(stop,item,2);
		stop.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				if(!isDone())
					setDone(true);
			}
		});
	}
	public void run(){
		for(int i=min;i<max;i++)
		{
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			table.getDisplay().asyncExec(new Runnable(){
				public void run(){
					if(bar.isDisposed())
						return;
					bar.setSelection(bar.getSelection()+1);
				}
			});
			if(isDone()){
				break;
			}
		}
	}
	public Table getTable(){
		return table;
	}
	public void setTable(Table table){
		this.table = table;
	}
	public boolean isDone(){
		return done;
	}
	public void setDone(boolean done)
	{
		this.done = done;
	}
}
