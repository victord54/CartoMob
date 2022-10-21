package fr.victord54.cartomob.tools;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Save {
    private static Save INSTANCE = null;
    private Save() {}

    public static Save getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Save();
        return INSTANCE;
    }

    public void saveToStorage(Context context, Object obj) {
        try {
            FileOutputStream fos = context.openFileOutput("cartoMob.data", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(obj);
            os.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object loadFromStorage(Context context) {
        try {
            FileInputStream fis = context.openFileInput("cartoMob.data");
            ObjectInputStream is = new ObjectInputStream(fis);
            Object o = is.readObject();
            is.close();
            fis.close();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
