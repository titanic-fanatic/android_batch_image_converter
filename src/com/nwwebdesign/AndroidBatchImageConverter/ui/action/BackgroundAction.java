package com.nwwebdesign.AndroidBatchImageConverter.ui.action;

import org.jdesktop.swingworker.SwingWorker;

import javax.swing.AbstractAction;

public abstract class BackgroundAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void executeBackgroundTask(SwingWorker<?, ?> worker) {
        if (worker != null) {
            worker.execute();
        }
    }
}