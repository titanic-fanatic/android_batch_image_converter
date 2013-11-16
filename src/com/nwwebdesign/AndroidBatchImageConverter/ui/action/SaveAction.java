package com.nwwebdesign.AndroidBatchImageConverter.ui.action;

import com.nwwebdesign.AndroidBatchImageConverter.ui.MainFrame;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class SaveAction extends BackgroundAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_NAME = "save";
    private MainFrame frame;
    private String filePath;
    private ArrayList<File> filePaths;
    private boolean recursive = false;

    public SaveAction(MainFrame frame, String filePath) {
    	//System.out.println("Save Action Reached!");
        this.frame = frame;
        this.filePath = filePath;
        this.recursive = false;
        putValue(NAME, "Convert");
        putValue(SHORT_DESCRIPTION, "Convert PNGs");
        putValue(LONG_DESCRIPTION, "Convert PNGs");
    }

    public SaveAction(MainFrame frame, ArrayList<File> filePaths) {
        this.frame = frame;
        this.filePaths = filePaths;
        this.recursive = true;
        putValue(NAME, "Convert");
        putValue(SHORT_DESCRIPTION, "Convert PNGs");
        putValue(LONG_DESCRIPTION, "Convert PNGs");
    }

    public void actionPerformed(ActionEvent e) {
		//System.out.println("actionPerformed Reached!");
    	if (!recursive){
    		//System.out.println("Execute filePath");
    		executeBackgroundTask(frame.save(filePath));
    	}
    	else{
    		//System.out.println("Execute filePaths");
    		executeBackgroundTask(frame.saveSelectFiles(filePaths));
    	}
    }
}