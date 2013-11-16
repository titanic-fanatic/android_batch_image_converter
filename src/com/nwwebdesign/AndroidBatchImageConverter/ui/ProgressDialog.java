package com.nwwebdesign.AndroidBatchImageConverter.ui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.nwwebdesign.AndroidBatchImageConverter.ui.MainFrame.SaveTask;

public class ProgressDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDialog progressDialog;
	private JLabel currentFileLabel;
	private JProgressBar progressBar;
	private JFrame frame;
	private SaveTask saveTask;
	
	public ProgressDialog(JFrame frame, final SaveTask saveTask){
		this.frame = frame;
		this.saveTask = saveTask;
		progressDialog = this;
		progressDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		progressDialog.setPreferredSize(new Dimension(400, 150));
		progressDialog.setSize(new Dimension(400, 150));
		progressDialog.setTitle("Conversion Progress");
		progressDialog.add(getPanel(), BorderLayout.CENTER);
		progressDialog.setLocationRelativeTo(frame);
		progressDialog.setVisible(true);
		frame.setEnabled(false);
	}

	public void setProgress(int percentage){
		if (percentage >= 100){
			progressBar.setValue(100);
			frame.setEnabled(true);
			this.dispose();
		}
		else{
			progressBar.setValue(percentage);
		}
	}
	
	public int getProgress(){
		return progressBar.getValue();
	}
	
	public void setCurrentFile(String filename){
		currentFileLabel.setText("Current File: " + filename);
	}
	
	public String getCurrentFile(){
		return currentFileLabel.getText().replace("Current File: ", "");
	}
	
	private JPanel getPanel(){
    	double progressPanelSize[][] = {{7.0, TableLayout.FILL, 7.0}, // COLUMNS
                                        {7.0, 20.0, 7.0, 20.0, 25.0, 7.0, 25.0, 7.0}}; // ROWS
    	TableLayout progressPanelLayout = new TableLayout(progressPanelSize);
    	progressPanelLayout.setHGap(0);
    	progressPanelLayout.setVGap(0);
    	
    	JPanel progressPanel = new JPanel();
    	progressPanel.setLayout(progressPanelLayout);
    	
    	JLabel dialogTitleLabel = new JLabel();
    	dialogTitleLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
    	dialogTitleLabel.setText("Image conversion is in progress...");
    	progressPanel.add(dialogTitleLabel, "1, 1, l, c");
    	
    	currentFileLabel = new JLabel();
    	dialogTitleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
    	currentFileLabel.setText("Current File: ");
    	progressPanel.add(currentFileLabel, "1, 3, l, c");
    	
    	progressBar = new JProgressBar();
    	progressBar.setValue(0);
    	progressPanel.add(progressBar, "1, 4, f, f");
    	
    	JButton cancelButton = new JButton();
    	cancelButton.setText("Cancel");
    	cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveTask.setCancel(true);
			}
		});
    	progressPanel.add(cancelButton, "1, 6, c, f");
    	
    	return progressPanel;
    }

}
