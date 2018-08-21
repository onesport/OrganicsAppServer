package com.onesport.organicappserver.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Fileutils {


    public static final String TEMPFILEPATH="/Users/rakesh/Documents/Testing/organicappserver/src/main/java/com/onesport/organicappserver/temp/";
    public static synchronized String writeImageToTemp(MultipartFile image, String filename)throws IOException {
        File file = generateFile(filename);
        file.createNewFile();
        image.transferTo(file);
        return file.getName();
    }

    public static File generateFile(String filename){
        File file=new File(TEMPFILEPATH+filename);
        if(file.exists()){
            file.delete();
        }
        return file;
    }
}
