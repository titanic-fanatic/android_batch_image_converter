package com.nwwebdesign.AndroidBatchImageConverter.ui;

import info.clearthought.layout.TableLayout;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ErrorDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorDialog dialog;

	ErrorDialog(JFrame frame, StackTraceElement[] stackTraceElements){
		this.dialog = this;
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(frame);
		dialog.setTitle("Fatal Error");
		
		double errorDialogSize[][] = {{7.0, 626.0, 7.0}, // COLUMNS
		                                {7.0, 50.0, 7.0, 300.0, 7.0, 25.0, 7.0}}; // ROWS
		TableLayout errorDialogLayout = new TableLayout(errorDialogSize);
		errorDialogLayout.setHGap(0);
		errorDialogLayout.setVGap(0);
		
		JPanel errorDialogPanel = new JPanel();
		errorDialogPanel.setLayout(errorDialogLayout);
		dialog.add(errorDialogPanel);
		
		JLabel infoLabel = new JLabel();
		infoLabel.setText("<html><body>Please click on the \"Copy to Clipboard\" button and paste the stacktrace below into an email addressed to<br>tylor.janzen@gmail.com</body></html>");
		errorDialogPanel.add(infoLabel, "1, 1, l, c");
		
		JScrollPane errorDialogScrollPane = new JScrollPane();
		errorDialogScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		errorDialogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JTextArea errorDialogTextArea = new JTextArea();
		final StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : stackTraceElements) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    errorDialogTextArea.setText(sb.toString());
		errorDialogScrollPane.add(errorDialogTextArea);
		errorDialogPanel.add(errorDialogScrollPane, "1, 3, f, f");
		
		double buttonsPanelSize[][] = {{TableLayout.FILL, TableLayout.FILL}, // COLUMNS
		                              {25.0}}; // ROWS
		TableLayout buttonsPanelLayout = new TableLayout(buttonsPanelSize);
		buttonsPanelLayout.setHGap(0);
		buttonsPanelLayout.setVGap(0);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(buttonsPanelLayout);
		errorDialogPanel.add(buttonsPanel, "1, 5, f, f");
		
		JButton copyButton = new JButton();
		copyButton.setText("Copy to Clipboard");
		copyButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection (sb.toString());
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		buttonsPanel.add(copyButton, "0, 0, c, f");
		
		JButton doneButton = new JButton();
		doneButton.setText("Done");
		doneButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		buttonsPanel.add(doneButton, "1, 0, c, f");
		
		dialog.pack();
		dialog.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Properties prop = new Properties();
            	try {
        			prop.load(new FileInputStream("options1.properties"));
        		} catch (IOException e) {
        			new ErrorDialog(null, e.getStackTrace());
        		}
            }
        });

	}

}
