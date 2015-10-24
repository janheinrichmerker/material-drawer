package com.heinrichreimersoftware.materialdrawer.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by serayuzgur on 24/10/15.
 */
public class ParcelUtil {

    private ParcelUtil() {
    }

    public static String toParcelString(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(obj);
            } catch (IOException e) {
                //Don't be sorry
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    public static Object fromParcelString(String ps) {
        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(ps, Base64.DEFAULT));
        ObjectInput in = null;
        Object obj = null;
        try {
            try {
                in = new ObjectInputStream(bis);
                obj = in.readObject();
            } catch (IOException e) {
                //Don't be sorry
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return obj;
    }
}
