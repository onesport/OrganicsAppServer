package com.onesport.organicappserver.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fileutils {


    public static synchronized String writeImageToTemp(MultipartFile image, String filename,String path)throws IOException {
        File file = generateFile(filename,path);
        file.createNewFile();
        image.transferTo(file);
        return file.getName();
    }

    public static File generateFile(String filename,String path){
        File file=new File(path+filename);
        if(file.exists()){
            file.delete();
        }
        return file;
    }

    public static byte[] getFile(String id ,String path){
        try {
            File file=ResourceUtils.getFile(path+id);
            if(file.getAbsoluteFile().exists()) {
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
