package com.nwwebdesign.AndroidBatchImageConverter.ui;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class OptionsDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDialog optionsDialog;
	private String selectionType;
	private boolean recursive;
	private String location;
	private String path;
	private String outputDirectory;
	private String outputDirectoryPath;
	private boolean confirmOverwrite;
	private JLabel recursiveLabel;
	private JCheckBox recursiveCheckBox;
	private JLabel customStartDirectoryLabel;
	private JTextField customStartDirectoryTextField;
	private JButton startBrowseButton;
	private JLabel customOutputDirectoryLabel;
	private JButton outputBrowseButton;
	private JTextField customOutputDirectoryTextField;
	private String imageType;
	
	public OptionsDialog(final JFrame frame){
		final Properties prop = new Properties();
		File propertiesFile = new File("options.properties");
		
		if (!propertiesFile.exists()){
			try {
				propertiesFile.createNewFile();
				
				prop.setProperty("SelectionType", "FILE");
				prop.setProperty("Recursive", "FALSE");
				prop.setProperty("Location", "HOME");
				prop.setProperty("Path", "");
				prop.setProperty("OutputDirectory", "HOME");
				prop.setProperty("OutputDirectoryPath", "");
				prop.setProperty("ConfirmOverwrite", "TRUE");
				prop.setProperty("ImageType", "BOTH");
				
				prop.store(new FileOutputStream("options.properties"), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			prop.load(new FileInputStream("options.properties"));
			
			this.selectionType = prop.getProperty("SelectionType");
			this.recursive = prop.getProperty("Recursive").equals("TRUE") ? true : false;
			this.location = prop.getProperty("Location");
			this.path = prop.getProperty("Path");
			this.outputDirectory = prop.getProperty("OutputDirectory");
			this.outputDirectoryPath = prop.getProperty("OutputDirectoryPath");
			this.confirmOverwrite = prop.getProperty("ConfirmOverwrite").equals("TRUE") ? true : false;
			this.imageType = prop.getProperty("ImageType") == null ? "" : prop.getProperty("ImageType");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double optionsDialogSize[][] = {{7.0, TableLayout.FILL, 7.0}, // COLUMNS
		                                {7.0, TableLayout.FILL, 7.0, 25.0, 7.0}}; // ROWS
		TableLayout optionsDialogLayout = new TableLayout(optionsDialogSize);
		optionsDialogLayout.setHGap(0);
		optionsDialogLayout.setVGap(0);
		
		optionsDialog = this;
		optionsDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		optionsDialog.setLayout(optionsDialogLayout);
		optionsDialog.setTitle("Options");
		
		double contentPanelSize[][] = {{7.0, TableLayout.FILL, 20.0, 100.0, 7.0}, // COLUMNS
		                               {7.0, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0, 25.0, 7.0, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0, 25.0, 7.0, TableLayout.FILL, 7.0, TableLayout.FILL, 7.0}}; // ROWS
		TableLayout contentPanelLayout = new TableLayout(contentPanelSize);
		contentPanelLayout.setHGap(0);
		contentPanelLayout.setVGap(0);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(contentPanelLayout);
		contentPanel.setBorder(new TitledBorder(new EtchedBorder(), "Options", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Times New Roman", Font.BOLD, 14), new Color(0, 0, 0, 255)));
		optionsDialog.add(contentPanel, "1, 1, f, f");
		
		JLabel selectionTypeLabel = new JLabel();
		selectionTypeLabel.setText("Make selection by file or by directory:");
		contentPanel.add(selectionTypeLabel, "1, 1, f, f");
		
		String[] selectionTypeList = { "File", "Directory" };

		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		JComboBox<?> selectionTypeComboBox = new JComboBox<Object>(selectionTypeList);
		selectionTypeComboBox.setSelectedIndex(this.selectionType.equals("FILE") ? 0 : 1);
		selectionTypeComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>)e.getSource();
				if (cb.getSelectedIndex() == 0){
					prop.setProperty("SelectionType", "FILE");
					selectionType = "FILE";
					recursiveLabel.setEnabled(false);
					recursiveCheckBox.setEnabled(false);
				}
				else{
					prop.setProperty("SelectionType", "DIR");
					selectionType = "DIR";
					recursiveLabel.setEnabled(true);
					recursiveCheckBox.setEnabled(true);
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		}); 
		contentPanel.add(selectionTypeComboBox, "3, 1, f, c");
		
		recursiveLabel = new JLabel();
		recursiveLabel.setEnabled(selectionTypeComboBox.getSelectedIndex() == 0 ? false : true);
		recursiveLabel.setText("Search through directories recursively:");
		contentPanel.add(recursiveLabel, "1, 3, f, f");
		
		recursiveCheckBox = new JCheckBox();
		recursiveCheckBox.setSelected(this.recursive);
		recursiveCheckBox.setEnabled(selectionTypeComboBox.getSelectedIndex() == 0 ? false : true);
		recursiveCheckBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				
				if (cb.isSelected()){
					prop.setProperty("Recursive", "TRUE");
					recursive = true;
				}
				else{
					prop.setProperty("Recursive", "FALSE");
					recursive = false;
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		});
		contentPanel.add(recursiveCheckBox, "3, 3, l, c");
		
		JLabel locationLabel = new JLabel();
		locationLabel.setText("Start file browser in:");
		contentPanel.add(locationLabel, "1, 5, f, f");
		
		String[] locationList = { "Home", "Last Opened", "Custom" };

		JComboBox<?> locationComboBox = new JComboBox<Object>(locationList);
		if (this.location.equals("HOME")){
			locationComboBox.setSelectedIndex(0);
		}
		else if (this.location.equals("LAST")){
			locationComboBox.setSelectedIndex(1);
		}
		else{
			locationComboBox.setSelectedIndex(2);
		}
		locationComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>)e.getSource();
				if (cb.getSelectedIndex() == 0){
					prop.setProperty("Location", "HOME");
					location = "HOME";
					customStartDirectoryLabel.setEnabled(false);
					startBrowseButton.setEnabled(false);
					customStartDirectoryTextField.setEnabled(false);
				}
				else if (cb.getSelectedIndex() == 1){
					prop.setProperty("Location", "LAST");
					location = "LAST";
					customStartDirectoryLabel.setEnabled(false);
					startBrowseButton.setEnabled(false);
					customStartDirectoryTextField.setEnabled(false);
				}
				else{
					prop.setProperty("Location", "CUSTOM");
					location = "CUSTOM";
					customStartDirectoryLabel.setEnabled(true);
					startBrowseButton.setEnabled(true);
					customStartDirectoryTextField.setEnabled(true);
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		}); 
		contentPanel.add(locationComboBox, "3, 5, f, c");
		
		customStartDirectoryLabel = new JLabel();
		customStartDirectoryLabel.setEnabled((this.location.equals("HOME") || this.location.equals("LAST")) ? false : true);
		customStartDirectoryLabel.setText("Custom Start Directory:");
		contentPanel.add(customStartDirectoryLabel, "1, 7, f, f");
		
		startBrowseButton = new JButton();
		startBrowseButton.setEnabled((this.location.equals("HOME") || this.location.equals("LAST")) ? false : true);
		startBrowseButton.setText("Browse");
		startBrowseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				if (fileChooser.showOpenDialog(optionsDialog) == JFileChooser.APPROVE_OPTION) {
					prop.setProperty("Path", fileChooser.getSelectedFile().toString());
					path = fileChooser.getSelectedFile().toString();
					customStartDirectoryTextField.setText(fileChooser.getSelectedFile().toString());
			    }
			    else {
			    	//System.out.println("No Selection ");
			    }
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
			
		});
		contentPanel.add(startBrowseButton, "3, 7, l, c");
		
		customStartDirectoryTextField = new JTextField();
		customStartDirectoryTextField.setEnabled(locationComboBox.getSelectedIndex() == 2 ? true : false);
		customStartDirectoryTextField.setBorder(new EtchedBorder());
		customStartDirectoryTextField.setEditable(false);
		customStartDirectoryTextField.setText(this.path);
		contentPanel.add(customStartDirectoryTextField, "1, 9, 3, 9, f, f");
		
		JLabel outputDirectoryLabel = new JLabel();
		outputDirectoryLabel.setText("Output Directory:");
		contentPanel.add(outputDirectoryLabel, "1, 11, f, f");
		
		String[] outputDirectoryList = { "Same Dir", "Custom" };

		JComboBox<?> outputDirectoryComboBox = new JComboBox<Object>(outputDirectoryList);
		if (this.outputDirectory.equals("SAME")){
			outputDirectoryComboBox.setSelectedIndex(0);
		}
		else if (this.outputDirectory.equals("CUSTOM")){
			outputDirectoryComboBox.setSelectedIndex(1);
		}
		outputDirectoryComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>)e.getSource();
				if (cb.getSelectedIndex() == 0){
					prop.setProperty("OutputDirectory", "SAME");
					outputDirectory = "SAME";
					customOutputDirectoryLabel.setEnabled(false);
					outputBrowseButton.setEnabled(false);
					customOutputDirectoryTextField.setEnabled(false);
				}
				else if (cb.getSelectedIndex() == 1){
					prop.setProperty("OutputDirectory", "CUSTOM");
					outputDirectory = "CUSTOM";
					customOutputDirectoryLabel.setEnabled(true);
					outputBrowseButton.setEnabled(true);
					customOutputDirectoryTextField.setEnabled(true);
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		}); 
		contentPanel.add(outputDirectoryComboBox, "3, 11, f, c");
		
		customOutputDirectoryLabel = new JLabel();
		customOutputDirectoryLabel.setEnabled(this.outputDirectory.equals("SAME") ? false : true);
		customOutputDirectoryLabel.setText("Custom Output Directory:");
		contentPanel.add(customOutputDirectoryLabel, "1, 13, f, f");
		
		outputBrowseButton = new JButton();
		outputBrowseButton.setEnabled(this.outputDirectory.equals("SAME") ? false : true);
		outputBrowseButton.setText("Browse");
		outputBrowseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				if (fileChooser.showOpenDialog(optionsDialog) == JFileChooser.APPROVE_OPTION) {
					prop.setProperty("OutputDirectoryPath", fileChooser.getSelectedFile().toString());
					outputDirectoryPath = fileChooser.getSelectedFile().toString();
					customOutputDirectoryTextField.setText(outputDirectoryPath);
			    }
			    else {
			    	//System.out.println("No Selection ");
			    }
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
			
		});
		contentPanel.add(outputBrowseButton, "3, 13, l, c");
		
		customOutputDirectoryTextField = new JTextField();
		customOutputDirectoryTextField.setEnabled(outputDirectoryComboBox.getSelectedIndex() == 1 ? true : false);
		customOutputDirectoryTextField.setText(outputDirectoryPath);
		customOutputDirectoryTextField.setEditable(false);
		customOutputDirectoryTextField.setBorder(new EtchedBorder());
		contentPanel.add(customOutputDirectoryTextField, "1, 15, 3, 15, f, f");
		
		JLabel confirmOverwriteLabel = new JLabel();
		confirmOverwriteLabel.setText("Confirm Overwrite File");
		contentPanel.add(confirmOverwriteLabel, "1, 17, f, f");
		
		JCheckBox confirmOverwriteCheckBox = new JCheckBox();
		confirmOverwriteCheckBox.setSelected(this.confirmOverwrite ? true : false);
		confirmOverwriteCheckBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				
				if (cb.isSelected()){
					prop.setProperty("ConfirmOverwrite", "TRUE");
					confirmOverwrite = true;
				}
				else{
					prop.setProperty("ConfirmOverwrite", "FALSE");
					confirmOverwrite = false;
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		});
		contentPanel.add(confirmOverwriteCheckBox, "3, 17, l, c");
		
		JLabel imageTypeLabel = new JLabel();
		imageTypeLabel.setText("Make selection by file or by directory:");
		contentPanel.add(imageTypeLabel, "1, 19, f, f");
		
		String[] imageTypeList = { "ALL", "PNG", "JPG" };

		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		JComboBox<?> imageTypeComboBox = new JComboBox<Object>(imageTypeList);
		imageTypeComboBox.setSelectedIndex((this.imageType.equals("ALL") || this.imageType.equals("") || this.imageType == null) ? 0 : this.imageType.equals("PNG") ? 1 : 2);
		imageTypeComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>)e.getSource();
				if (cb.getSelectedIndex() == 0){
					prop.setProperty("ImageType", "ALL");
					imageType = "FILE";
				}
				else if (cb.getSelectedIndex() == 1){
					prop.setProperty("ImageType", "PNG");
					imageType = "PNG";
				}
				else{
					prop.setProperty("ImageType", "JPG");
					selectionType = "JPG";
				}
				
				try {
					prop.store(new FileOutputStream("options.properties"), null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				setOptionVars((MainFrame) frame);
			}
		}); 
		contentPanel.add(imageTypeComboBox, "3, 19, f, c");
		
		JButton doneButton = new JButton();
		doneButton.setText("Done");
		doneButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setOptionVars((MainFrame) frame);
				
				ArrayList<String> errorMessages = new ArrayList<String>();
				if (location.equals("CUSTOM") && path.equals("")){
					errorMessages.add("You must specify a path for the default file chooser location.");
				}
				
				if (outputDirectory.equals("CUSTOM") && outputDirectoryPath.equals("")){
					errorMessages.add("You must specify a path for the output directory.");
				}
				
				if (errorMessages.size() > 0){
					
					String errorMessage = "<html><body>You must fix the following errors before continuing:<br><br>";
					
					for (int i = 0; i < errorMessages.size(); i++){
						errorMessage += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&gt;&nbsp;" + errorMessages.get(i) + "<br>";
					}
					
					errorMessage += "</body></html>";
					
					JOptionPane.showMessageDialog(optionsDialog, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					optionsDialog.dispose();
				}
			}
		});
		optionsDialog.add(doneButton, "1, 3, c, f");
		
		optionsDialog.pack();
		optionsDialog.setDefaultCloseOperation(MainFrame.DISPOSE_ON_CLOSE);
		optionsDialog.setLocationRelativeTo(frame);
		optionsDialog.setVisible(true);
		
	}
	
	private void setOptionVars(MainFrame frame){
		frame.setOptionVars(this.selectionType, this.recursive, this.location, this.path, this.outputDirectory, this.outputDirectoryPath, this.confirmOverwrite);
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	OptionsDialog frame = new OptionsDialog(null);
                frame.setDefaultCloseOperation(MainFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

	}

}
