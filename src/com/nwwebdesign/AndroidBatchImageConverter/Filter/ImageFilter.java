package com.nwwebdesign.AndroidBatchImageConverter.Filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ImageFilter implements FileFilter {

    private String imageType;

	@Override
    public boolean accept(File pathname) {
        ArrayList<String> suffix = new ArrayList<String>();
        Properties prop = new Properties();
        
        try {
			prop.load(new FileInputStream("options.properties"));
			
			this.imageType = prop.getProperty("ImageType");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
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
	        if( pathname.getName().toLowerCase().endsWith(suffix.get(i)) ) {
	            return true;
	        }
        }
        
        return false;
    }

}
