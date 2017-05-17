package skyplane.service;

import java.io.UnsupportedEncodingException;

/**
 * Created by Vladimir Kovalenko on 22.04.17
 */
public class Util {
    public static String FixRussianString(String string){
        try {
            return new String(string.getBytes("CP1251"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }
}