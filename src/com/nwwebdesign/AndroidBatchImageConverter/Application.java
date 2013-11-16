package com.nwwebdesign.AndroidBatchImageConverter;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.nwwebdesign.AndroidBatchImageConverter.ui.MainFrame;

public class Application {
	private static void initUserInterface() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Android Batch Image Convertor");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		initUserInterface();
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainFrame frame = new MainFrame();
            	frame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/icon16x16.png")).getImage());
                frame.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

	}

}
