package com.swtdesigner;
/*
 * MultiTaskDrive
 * MultiTaskGUI
 * Task
 * */
import org.eclipse.swt.widgets.Display;

public class MultiTaskDrive {
	public static void main(String args[])
	{
		Display display = Display.getDefault();
		MultiTaskGUI multiTask = new MultiTaskGUI();
		multiTask.getShell().open();
		while(!multiTask.getShell().isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
//		ImageFactory.dispose();
		display.dispose();
	}

}
