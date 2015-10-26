package com.cyt.ieasy.tools;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class FileUtil {

    private Context context;
    public FileUtil(Context context) {
        this.context=context;
    }

    public void save(String fileName, Object o) throws Exception {

        String path = context.getFilesDir()+"/" ;

        File dir = new File(path);
        dir.mkdirs();

        File f = new File(dir, fileName);

        if (f.exists()) {
            f.delete();
        }
        FileOutputStream os = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
        os.close();
    }

    public Object readObject(String fileName) throws Exception {
        String path = context.getFilesDir()+"/" ;

        File dir = new File(path);
        dir.mkdirs();
        File file = new File(dir, fileName);
        InputStream is = new FileInputStream(file);

        ObjectInputStream objectInputStream = new ObjectInputStream(is);

        Object o = objectInputStream.readObject();

        return o;

    }
}
