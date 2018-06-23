package com.example.sacrew.numericov4.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class FunctionStorage implements Serializable {
    public List<String> functions = new LinkedList<>();

    public void updateStorage(File temp){
        FileOutputStream bridge = null;
        try {
            bridge = new FileOutputStream(temp);
            ObjectOutputStream object = new ObjectOutputStream(bridge);
            object.writeObject(this);
            object.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
