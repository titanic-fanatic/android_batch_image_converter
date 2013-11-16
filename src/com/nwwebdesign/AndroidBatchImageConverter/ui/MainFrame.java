package com.nwwebdesign.AndroidBatchImageConverter.ui;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingworker.SwingWorker;

import com.nwwebdesign.AndroidBatchImageConverter.Filter.ImageFilter;
import com.nwwebdesign.AndroidBatchImageConverter.Graphics.GraphicsUtilities;
import com.nwwebdesign.AndroidBatchImageConverter.ui.action.SaveAction;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;
	private Container contentPane;
	private JButton button;
	private Properties prop;
	private File propertiesFile;
	private String selectionType;
	private boolean recursive;
	private String location;
	private String path;
	private String outputDirectory;
	private String outputDirectoryPath;
	private boolean confirmOverwrite;
	private String imageType;

	/**
	 * @param args
	 */
	/**
	 * 
	 */
	public MainFrame(){
		prop = new Properties();
		propertiesFile = new File("options.properties");
		
		if (!propertiesFile.exists()){
			try {
				propertiesFile.createNewFile();
				
				prop.setProperty("SelectionType", "FILE");
				prop.setProperty("Recursive", "FALSE");
				prop.setProperty("Location", "HOME");
				prop.setProperty("Path", "");
				prop.setProperty("OutputDirectory", "SAME");
				prop.setProperty("OutputDirectoryPath", "");
				prop.setProperty("ConfirmOverwrite", "TRUE");
				prop.setProperty("ImageType", "PNG");
				
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
			this.imageType = prop.getProperty("ImageType");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		frame = this;
		contentPane = frame.getContentPane();
		
		setTitle("Android Batch Image Converter 1.2");
		
		buildMenuBar();
		
		double contentPanelSize[][] = {{7.0, 100.0, 7.0, TableLayout.FILL, 7.0, 100.0, 7.0}, // COLUMNS
		                              {7.0, TableLayout.FILL, 7.0, 25.0, 14.0, 25.0, 7.0}}; // ROWS
		TableLayout contentPanelLayout = new TableLayout(contentPanelSize);
		contentPanelLayout.setHGap(0);
		contentPanelLayout.setVGap(0);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(contentPanelLayout);
		contentPane.add(contentPanel);
		
		double informationPanelSize[][] = {{7.0, TableLayout.FILL, 7.0}, // COLUMNS
		                                   {7.0, TableLayout.FILL, 7.0}}; // ROWS
		TableLayout informationPanelLayout = new TableLayout(informationPanelSize);
		informationPanelLayout.setHGap(0);
		informationPanelLayout.setVGap(0);
		
		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(informationPanelLayout);
		informationPanel.setBorder(new TitledBorder(new EtchedBorder(), "Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Times New Roman", Font.BOLD, 14), new Color(0, 0, 0, 255)));
		contentPanel.add(informationPanel, "1, 1, 5, 1");
		
		String bannerImageURL = getClass().getResource("/images/banner.png").toString();
		int optionsPanelRed = UIManager.getColor("OptionPane.background").getRed();
		int optionsPanelGreen = UIManager.getColor("OptionPane.background").getGreen();
		int optionsPanelBlue = UIManager.getColor("OptionPane.background").getBlue();
		JEditorPane textArea = new JEditorPane();
		textArea.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		textArea.setSize(informationPanel.getWidth(), informationPanel.getHeight());
		textArea.setBackground(UIManager.getColor("OptionsPane.background"));
		textArea.setSelectionColor(new Color(0, 0, 0, 0));
		textArea.setSelectedTextColor(Color.BLACK);
		textArea.setDisabledTextColor(Color.BLACK);
		textArea.setEditable(false);
		textArea.setFont(new Font("Times New Roman", Font.BOLD, 14));
		textArea.setText("<html><body style=\"margin: 0; padding: 0; background-color: #" + Integer.toHexString(optionsPanelRed) + Integer.toHexString(optionsPanelGreen) + Integer.toHexString(optionsPanelBlue) + ";\"><table width=\"100%\"><tr><td align=\"center\" valign=\"middle\"><img src=\"" + bannerImageURL + "\"></td></tr></table>Welcome to the Android Batch Image Converter. This program will help you quickly convert large quantities of optimized png's so that you may work with them in your graphics editor of choice.<br><br>This version brings you finer control over your workflow. Allowing you to select custom output directories, recurse through directories and more. Check the change log for more details on the new features.</body></html>");
		informationPanel.add(textArea, "1, 1, f, f");
		
		JLabel fieldLabel = new JLabel();
		fieldLabel.setText("Directory:");
		contentPanel.add(fieldLabel, "1, 3, r, c");
		
		final JTextField textField = new JTextField();
		textField.setBorder(new EtchedBorder());
		textField.setEditable(false);
		contentPanel.add(textField, "3, 3");
		
		JButton browseButton = new JButton();
		browseButton.setText("Browse");
		browseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(((location.equals("CUSTOM") || location.equals("LAST")) && path != null && !path.equals("")) ? path : null);
				
				if (selectionType.equals("DIR")){
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setFileFilter(new FileFilter(){

						@Override
						public boolean accept(File pathname) {
							if( pathname.isDirectory() ) {
					            return true;
					        }
					        return false;
						}

						@Override
						public String getDescription() {
							return "Directory";
						}
					});
				}
				else{
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(true);
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setFileFilter(new FileFilter(){

						@Override
						public boolean accept(File pathname) {
							ArrayList<String> suffix = new ArrayList<String>();
							
							if (imageType.equals("PNG")){
								suffix.add(".png");
							}
							else if (imageType.equals("JPG")){
								suffix.add(".jpg");
								suffix.add(".jpeg");
							}
							else {
								suffix.add(".png");
								suffix.add(".jpg");
								suffix.add(".jpeg");
							}
							
							for (int i = 0; i < suffix.size(); i++){
						        if( pathname.getName().toLowerCase().endsWith(suffix.get(i)) || pathname.isDirectory() ) {
						            return true;
						        }
							}
					        return false;
						}

						@Override
						public String getDescription() {
							if (imageType.equals("PNG")){
								return "PNG Images (*.png)";
							}
							else if (imageType.equals("JPG")){
								return "JPG Images (*.jpg,*.jpeg)";
							}
							else{
								return "PNG/JPG Images (*.png,*.jpg,*.jpeg)";
							}
						}
					});
				}
				
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					if (location.equals("LAST")){
						path = fileChooser.getSelectedFile().toString();
						prop.setProperty("Path", path);
						try {
							prop.store(new FileOutputStream("options.properties"), null);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					textField.setText(selectionType.equals("DIR") ? fileChooser.getSelectedFile().toString() : "Multiple File Selections");
					if (selectionType.equals("DIR")){
						//System.out.println("Second set SaveAction");
						button.setAction(new SaveAction(frame, textField.getText()));
					}
					else if (selectionType.equals("FILE")){
						File[] selectedFiles = fileChooser.getSelectedFiles();
						ArrayList<File> selectedFilesList = new ArrayList<File>();
						for (int i = 0; i < selectedFiles.length; i++){
							selectedFilesList.add(selectedFiles[i]);
						}
						//System.out.println("Third set SaveAction");
						button.setAction(new SaveAction(frame, selectedFilesList));
					}
					button.setEnabled(true);
			    }
			    else {
			    	//System.out.println("No Selection ");
			    }
				
			}
			
		});
		contentPanel.add(browseButton, "5, 3");
		
		button = new JButton();
		button.setText("Convert");
		//System.out.println("First set SaveAction");
		button.setAction(new SaveAction(frame, textField.getText()));
		button.setEnabled(false);
		contentPanel.add(button, "5, 5");
		
		setSize(640, 480);
	}
	
	private void buildMenuBar() {
	    JMenu fileMenu = new JMenu("File");
	    JMenu helpMenu = new JMenu("Help");
	    
	    JMenuItem optionsMenuItem = new JMenuItem();
	    optionsMenuItem.setText("Options");
	    optionsMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new OptionsDialog(frame);
			}
		});
	    fileMenu.add(optionsMenuItem);
	    
	    JMenuItem exitMenuItem = new JMenuItem();
	    exitMenuItem.setText("Exit");
	    exitMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
	    fileMenu.add(exitMenuItem);
	    
	    JMenuItem aboutMenuItem = new JMenuItem();
	    aboutMenuItem.setText("About");
	    aboutMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				double aboutPanelSize[][] = {{7.0, TableLayout.FILL, 7.0}, // COLUMNS
				                             {7.0, TableLayout.FILL, 7.0}}; // ROWS
				TableLayout aboutPanelLayout = new TableLayout(aboutPanelSize);
				aboutPanelLayout.setHGap(0);
				aboutPanelLayout.setVGap(0);
				
				JPanel aboutPanel = new JPanel();
				aboutPanel.setBorder(new TitledBorder(new EtchedBorder(), "About Android Batch Image Converter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Times New Roman", Font.BOLD, 14), new Color(0, 0, 0, 255)));
				
				
				String paypalImageURL = getClass().getResource("/images/btn_paypal_donate_cc.gif").toString();
				String bannerImageURL = getClass().getResource("/images/banner.png").toString();
				
				JEditorPane jEditorPane = new JEditorPane();
				jEditorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
				jEditorPane.setBackground(UIManager.getColor("OptionPane.background"));
				int optionsPanelRed = UIManager.getColor("OptionPane.background").getRed();
				int optionsPanelGreen = UIManager.getColor("OptionPane.background").getGreen();
				int optionsPanelBlue = UIManager.getColor("OptionPane.background").getBlue();
				jEditorPane.setSelectionColor(new Color(0, 0, 0, 0));
				jEditorPane.setSelectedTextColor(Color.BLACK);
				jEditorPane.setEditable(false);
				jEditorPane.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
				jEditorPane.setText("<html><body style=\"margin: 0; padding: 0; background-color: #" + Integer.toHexString(optionsPanelRed) + Integer.toHexString(optionsPanelGreen) + Integer.toHexString(optionsPanelBlue) + ";\"><img src=\"" + bannerImageURL + "\"><br><br><b>Application:</b> Android Batch Image Converter<br><b>Created:</b> March 31, 2013<br><b>Last Updated:</b> October 27, 2013<br><b>Current Version:</b> 1.2<br><b>Created By:</b> Tyler Janzen<br><b>Lisence:</b> Freeware<br><br>Don't forget to donate if you found this program useful.<br><br><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" valign=\"middle\"><a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=YA6RQ3AXTQAFU\"><img src=\"" + paypalImageURL + "\" border=\"0\" name=\"submit\" alt=\"PayPal - The safer, easier way to pay online!\"></a><br></td></tr></table></body></html>");
				jEditorPane.addHyperlinkListener(new HyperlinkListener() {
				    public void hyperlinkUpdate(HyperlinkEvent e) {
				        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				        	if(Desktop.isDesktopSupported()) {
				        	    try {
									Desktop.getDesktop().browse(e.getURL().toURI());
								} catch (IOException e1) {
									e1.printStackTrace();
								} catch (URISyntaxException e1) {
									e1.printStackTrace();
								}
				        	}
				        }
				    }
				});
				aboutPanel.add(jEditorPane, "1, 1, f, f");
				try {
					JOptionPane.showMessageDialog(frame, aboutPanel, "About Android Batch Image Converter", JOptionPane.PLAIN_MESSAGE);
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				}
			}
		});
	    helpMenu.add(aboutMenuItem);
	    
	    JMenuItem donateMenuItem = new JMenuItem();
	    donateMenuItem.setText("Donate");
	    donateMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Desktop.isDesktopSupported()) {
	        	    try {
						Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=YA6RQ3AXTQAFU"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
	        	}
				
			}
		});
	    helpMenu.add(donateMenuItem);
	
	    JMenuBar localJMenuBar = new JMenuBar();
	    localJMenuBar.add(fileMenu);
	    localJMenuBar.add(helpMenu);
	    setJMenuBar(localJMenuBar);
	}
	
	public SwingWorker<?, ?> save(String folderPath) {
		//System.out.println("save(String folderPath) Reached!");
		return folderPath != null ? new SaveTask(folderPath, recursive) : null;
    }
	
	public SwingWorker<?, ?> saveSelectFiles(ArrayList<File> filePaths) {
		//System.out.println("save(ArrayList<File> filePaths) Reached!");
		return filePaths != null ? new SaveTask(filePaths, false) : null;
    }
	
	class SaveTask extends SwingWorker<Boolean, Void>{
        private String folderPath = null;
        private ArrayList<File> filePaths = null;
        private boolean isRecursive = false;
		private ArrayList<File> rFilesList = new ArrayList<File>();
		private boolean cancelTask = false;

        SaveTask(String folderPath, boolean isRecursive){
    		//System.out.println("SaveTask(String folderPath, boolean isRecursive) Reached!");
            this.folderPath = folderPath;
            this.isRecursive = isRecursive;
        }

        SaveTask(ArrayList<File> filePaths, boolean isRecursive){
    		//System.out.println("SaveTask(ArrayList<File> filePaths, boolean isRecursive) Reached!");
            this.filePaths = filePaths;
            this.isRecursive = isRecursive;
        }

        protected Boolean doInBackground() throws Exception{
    		//System.out.println("doInBackground Started!");
        	BufferedImage bufferedImage;
        	int progress = 0;
        	int loopCount = 0;
        	//boolean cancelTask = false;
        	
        	JProgressBar progressBar = new JProgressBar();
        	progressBar.setValue(0);
        	
        	if (!(folderPath == null) && !folderPath.equals("") && new File(folderPath).exists()){
        		//System.out.println("(folderPath == null) && !folderPath.equals(\"\") && new File(folderPath).exists(): true");
        		if (!isRecursive){
        			//System.out.println("!isRecursive: true");
					File dir = new File(folderPath);
					ImageFilter fileFilter = new ImageFilter();
					
					File[] fileList = dir.listFiles(fileFilter);
					int fileCount = fileList.length;
					boolean sConfirmOverwrite = false;
					String outputType = null;
					
					sConfirmOverwrite = confirmOverwrite;
	
					Double filePercentage = (double) (100.0 / Double.parseDouble(fileCount + ".0"));
					Double progressCount = 0.0;
	
					ProgressDialog progressDialog = new ProgressDialog(frame, this);
					
					for (File file : fileList){
						if (!cancelTask){
							progressCount = progressCount + filePercentage;
							progress = (int) Math.round(progressCount);
							progressDialog.setCurrentFile(file.getName());
							outputType = returnOutputType(file.getName());
							Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName(outputType);
							ImageWriter imageWriter = imageWriterIterator.next();
							ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
							iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
							iwp.setCompressionQuality(1f);
							if (file.isFile() && checkFileType(file.getName())){
								bufferedImage = GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
				    			
				    			if (outputDirectory.equals("CUSTOM") && !outputDirectoryPath.equals("")){
									
									File newOutputFile;
									ImageOutputStream newImageOutputStream;
									if (sConfirmOverwrite && file.getPath().equals(outputDirectoryPath + File.separatorChar + file.getName())){
										String cFilename = file.getName();
										String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
									    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
								    	case 0:
								    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
								    		
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		break;
								    	case 1:
								    		break;
								    	case 2:
								    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
								    		
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		sConfirmOverwrite = false;
								    		break;
								    	case 3:
								    		cancelTask = true;
								    		break;
									    }
									}
									else{
								    	newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
								    	
								    	if (!newOutputFile.exists()){
								    		newOutputFile.createNewFile();
								    	}
								    	
										if (outputType.equals("PNG")){
											ImageIO.write(bufferedImage,  outputType,  newOutputFile);
										}
										else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
										}
									}
				    			}
				    			else{
									ImageOutputStream newImageOutputStream;
									if (sConfirmOverwrite){
										String cFilename = file.getName();
										String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
									    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
									    	case 0:
									    		if (outputType.equals("PNG")){
									    			ImageIO.write(bufferedImage, outputType, file);
									    		}
									    		else{
										    		newImageOutputStream = ImageIO.createImageOutputStream(file);
										    		imageWriter.setOutput(newImageOutputStream);
										    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
										    		imageWriter.dispose();
									    		}
									    		
									    		break;
									    	case 1:
									    		break;
									    	case 2:
									    		if (outputType.equals("PNG")){
									    			ImageIO.write(bufferedImage, outputType, file);
									    		}
									    		else{
										    		newImageOutputStream = ImageIO.createImageOutputStream(file);
										    		imageWriter.setOutput(newImageOutputStream);
										    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
										    		imageWriter.dispose();
									    		}
									    		
									    		sConfirmOverwrite = false;
									    		break;
									    	case 3:
									    		cancelTask = true;
									    		break;
									    }
									}
									else{
										if (outputType.equals("PNG")){
											ImageIO.write(bufferedImage, outputType, file);
										}
										else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(file);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
										}
									}
				    			}
				    			
				    			progressDialog.setProgress(progress);
						
								loopCount++;
								
								if (loopCount == fileCount){
									progressDialog.setProgress(100);
									JOptionPane.showMessageDialog(frame, "Conversion is complete!", "Information", JOptionPane.INFORMATION_MESSAGE);
									return true;
								}
							}
						}
						else{
							progressDialog.setProgress(100);
							JOptionPane.showMessageDialog(frame, "Conversion Cancelled!", "Information", JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
					}
        		}
        		else if (isRecursive){
        			//System.out.println("isRecursive: true");
        			File dir = new File(folderPath);
        			File[] rFilePaths = dir.listFiles();
        			rFilesList = new ArrayList<File>();
            		//System.out.println("Starting rFilesList for loop!");
        			//System.out.println("rFilePaths.length: " + rFilePaths.length);
        			for (int i = 0; i < rFilePaths.length; i++){
            			//System.out.println("Entered rFilesList for loop!");
        				File cFile = rFilePaths[i];
        				if (cFile.isDirectory()){
        					recurseDirectory(cFile);
        				}
        				else if (cFile.isFile()){
        					rFilesList.add(cFile);
        				}
        			}
        			
        			//System.out.println("rFilesList.size(): " + rFilesList.size());
        			
        			int fileCount = rFilesList.size();
					boolean sConfirmOverwrite = false;
					String outputType = null;
					
					sConfirmOverwrite = confirmOverwrite;
	
					Double filePercentage = (double) (100.0 / Double.parseDouble(fileCount + ".0"));
					Double progressCount = 0.0;
	
					ProgressDialog progressDialog = new ProgressDialog(frame, this);

        			//System.out.println("Starting for loop!");
					
					for (File file : rFilesList){
	        			//System.out.println("Entered for loop");
						if (!cancelTask){
							progressCount = progressCount + filePercentage;
							progress = (int) Math.round(progressCount);
							progressDialog.setCurrentFile(file.getName());
							outputType = returnOutputType(file.getName());
							if (file.isFile() && checkFileType(file.getName())){
								bufferedImage = GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
								Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName(outputType);
								ImageWriter imageWriter = imageWriterIterator.next();
								ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
								iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
								iwp.setCompressionQuality(1f);
				    			
				    			if (outputDirectory.equals("CUSTOM") && !outputDirectoryPath.equals("")){
				    				String nFilename = file.getPath().replace(folderPath + File.separatorChar, "");
				    				String newOutputFilePath = outputDirectoryPath + File.separatorChar + nFilename;
									
									File newOutputFile;
									ImageOutputStream newImageOutputStream;
									if (sConfirmOverwrite && file.getPath().equals(newOutputFilePath)){
										String cFilename = file.getName();
										String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
									    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
								    	case 0:
								    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + nFilename);
								    		
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		break;
								    	case 1:
								    		break;
								    	case 2:
								    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + nFilename);
								    		
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		sConfirmOverwrite = false;
								    		break;
								    	case 3:
								    		cancelTask = true;
								    		break;
									    }
									}
									else{
								    	newOutputFile = new File(outputDirectoryPath + File.separatorChar + nFilename);
										//File newOutputDir = new File(newOutputFile.getPath());
								    	
										if (!newOutputFile.getParentFile().exists()){
											newOutputFile.getParentFile().mkdirs();
										}
										
								    	//if (!newOutputFile.exists()){
								    	//	newOutputFile.createNewFile();
								    	//}
								    	
								    	//ImageIO.setUseCache(false);
										
										if (outputType.equals("PNG")){
											ImageIO.write(bufferedImage,  outputType,  newOutputFile);
										}
										else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
										}
									}
				    			}
				    			else{
									ImageOutputStream newImageOutputStream;
									if (sConfirmOverwrite){
										String cFilename = file.getName();
										String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
									    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
									    	case 0:
									    		if (outputType.equals("PNG")){
									    			ImageIO.write(bufferedImage, outputType, file);
									    		}
									    		else{
										    		newImageOutputStream = ImageIO.createImageOutputStream(file);
										    		imageWriter.setOutput(newImageOutputStream);
										    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
										    		imageWriter.dispose();
									    		}
									    		
									    		break;
									    	case 1:
									    		break;
									    	case 2:
									    		if (outputType.equals("PNG")){
									    			ImageIO.write(bufferedImage, outputType, file);
									    		}
									    		else{
										    		newImageOutputStream = ImageIO.createImageOutputStream(file);
										    		imageWriter.setOutput(newImageOutputStream);
										    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
										    		imageWriter.dispose();
									    		}
									    		
									    		sConfirmOverwrite = false;
									    		break;
									    	case 3:
									    		cancelTask = true;
									    		break;
									    }
									}
									else{
										if (outputType.equals("PNG")){
											ImageIO.write(bufferedImage, outputType, file);
										}
										else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(file);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
										}
									}
				    			}
				    			
				    			progressDialog.setProgress(progress);
						
								loopCount++;
								
								if (loopCount == fileCount){
									progressDialog.setProgress(100);
									JOptionPane.showMessageDialog(frame, "Conversion is complete!", "Information", JOptionPane.INFORMATION_MESSAGE);
									return true;
								}
							}
						}
						else{
							progressDialog.setProgress(100);
							JOptionPane.showMessageDialog(frame, "Conversion Cancelled!", "Information", JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
					}
        		}
			}
        	else if (filePaths != null && filePaths.size() > 0){
        		int fileCount = filePaths.size();
				boolean sConfirmOverwrite = false;
				String outputType = null;
				
				sConfirmOverwrite = confirmOverwrite;

				Double filePercentage = (double) (100.0 / Double.parseDouble(fileCount + ".0"));
				Double progressCount = 0.0;

				ProgressDialog progressDialog = new ProgressDialog(frame, this);
				
				for (File file : filePaths){
					if (!cancelTask){
						progressCount = progressCount + filePercentage;
						progress = (int) Math.round(progressCount);
						progressDialog.setCurrentFile(file.getName());
						outputType = returnOutputType(file.getName());
						if (file.isFile() && checkFileType(file.getName())){
							bufferedImage = GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
							Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName(outputType);
							ImageWriter imageWriter = imageWriterIterator.next();
							ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
							iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
							iwp.setCompressionQuality(1f);
			    			
			    			if (outputDirectory.equals("CUSTOM") && !outputDirectoryPath.equals("")){
								
								File newOutputFile;
								ImageOutputStream newImageOutputStream;
								if (sConfirmOverwrite && file.getPath().equals(outputDirectoryPath + File.separatorChar + file.getName())){
									String cFilename = file.getName();
									String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
								    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
							    	case 0:
							    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
							    		
							    		if (outputType.equals("PNG")){
							    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
							    		}
							    		else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
							    		}
							    		
							    		break;
							    	case 1:
							    		break;
							    	case 2:
							    		newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
							    		
							    		if (outputType.equals("PNG")){
							    			ImageIO.write(bufferedImage,  outputType,  newOutputFile);
							    		}
							    		else{
								    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
								    		imageWriter.setOutput(newImageOutputStream);
								    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
								    		imageWriter.dispose();
							    		}
							    		
							    		sConfirmOverwrite = false;
							    		break;
							    	case 3:
							    		cancelTask = true;
							    		break;
								    }
								}
								else{
							    	newOutputFile = new File(outputDirectoryPath + File.separatorChar + file.getName());
							    	
							    	if (!newOutputFile.exists()){
							    		newOutputFile.createNewFile();
							    	}
							    	
									if (outputType.equals("PNG")){
										ImageIO.write(bufferedImage,  outputType,  newOutputFile);
									}
									else{
							    		newImageOutputStream = ImageIO.createImageOutputStream(newOutputFile);
							    		imageWriter.setOutput(newImageOutputStream);
							    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
							    		imageWriter.dispose();
									}
								}
			    			}
			    			else{
			    				ImageOutputStream newImageOutputStream;
								if (sConfirmOverwrite){
									String cFilename = file.getName();
									String[] options = new String[] {"Overwrite", "Skip", "Overwrite All", "Cancel"};
								    switch (JOptionPane.showOptionDialog(progressDialog, "Do you wish to overwrite the following file: " + cFilename, "Confirm Overwrite", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])){
								    	case 0:
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage, outputType, file);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(file);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		break;
								    	case 1:
								    		break;
								    	case 2:
								    		if (outputType.equals("PNG")){
								    			ImageIO.write(bufferedImage, outputType, file);
								    		}
								    		else{
									    		newImageOutputStream = ImageIO.createImageOutputStream(file);
									    		imageWriter.setOutput(newImageOutputStream);
									    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
									    		imageWriter.dispose();
								    		}
								    		
								    		sConfirmOverwrite = false;
								    		break;
								    	case 3:
								    		cancelTask = true;
								    		break;
								    }
								}
								else{
									if (outputType.equals("PNG")){
										ImageIO.write(bufferedImage, outputType, file);
									}
									else{
							    		newImageOutputStream = ImageIO.createImageOutputStream(file);
							    		imageWriter.setOutput(newImageOutputStream);
							    		imageWriter.write(null, new IIOImage(bufferedImage,null,null),iwp);
							    		imageWriter.dispose();
									}
								}
			    			}
			    			
			    			progressDialog.setProgress(progress);
					
							loopCount++;
							
							if (loopCount == fileCount){
								progressDialog.setProgress(100);
								JOptionPane.showMessageDialog(frame, "Conversion is complete!", "Information", JOptionPane.INFORMATION_MESSAGE);
								return true;
							}
						}
					}
					else{
						progressDialog.setProgress(100);
						JOptionPane.showMessageDialog(frame, "Conversion Cancelled!", "Information", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				}
        	}
			else{
				JOptionPane.showMessageDialog(frame, "There is nothing to convert in this directory!", "Warning", JOptionPane.WARNING_MESSAGE);
				return false;
			}
        	return false;
        }

		private void recurseDirectory(File directory) {
			File[] fList = directory.listFiles();
			
			for (int i = 0; i < fList.length; i++){
				File cFile = fList[i];
				if (cFile.isDirectory()){
					recurseDirectory(cFile);
				}
				else if (cFile.isFile() && cFile.getName().toLowerCase().endsWith(".png")){
					rFilesList.add(cFile);
				}
			}
		}
		
		public void setCancel(boolean isCanceled){
			System.out.println("cancelTask set to: " + isCanceled);
			cancelTask = isCanceled;
		}
    }
	
	private boolean checkFileType(String filePath){
		ArrayList<String> suffix = new ArrayList<String>();
		
		if (imageType.equals("PNG")){
			suffix.add(".png");
		}
		else if (imageType.equals("JPG")){
			suffix.add(".jpg");
			suffix.add(".jpeg");
		}
		else{
			suffix.add(".png");
			suffix.add(".jpg");
			suffix.add(".jpeg");
		}
		
		for (int i = 0; i < suffix.size(); i++){
			if (filePath.endsWith(suffix.get(i))){
				return true;
			}
		}
		
		return false;
	}
	
	private String returnOutputType(String filePath){
		if (filePath.substring(filePath.lastIndexOf(".") + 1).equals(".png")){
			return "PNG";
		}
		else{
			return "JPG";
		}
	}
	
	public void setOptionVars(String selectionType, boolean recursive, String location, String path, String outputDirectory, String outputDirectoryPath, boolean confirmOverwrite){
		this.selectionType = selectionType;
		this.recursive = recursive;
		this.location = location;
		this.path = path;
		this.outputDirectory = outputDirectory;
		this.outputDirectoryPath = outputDirectoryPath;
		this.confirmOverwrite = confirmOverwrite;
		
		try {
			prop.load(new FileInputStream("options.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void SaveTask(File file){
//		BufferedImage bufferedImage;
//		try {
//			bufferedImage = GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
//			ImageIO.write(bufferedImage, "PNG", file);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception localException) {
//            localException.printStackTrace();
//        }
//	}

}
