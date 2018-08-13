package application.fileutils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Fileutils {

    public static final String TEMPFILEPATH="/Users/jenison-3631/Desktop/jeyorganics/data/temp/";
    public static synchronized String writeImageToTemp(MultipartFile image,String filename)throws IOException {
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
