/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.test.media.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ingimar
 */
public class FilePropertiesHelper {

    public static String getImagesFilePath() {
//
//        String filePath = "";
//        Properties properties = new Properties();
//
//        try {
//            InputStream iStream = getInputStream();
//            properties.load(iStream);
//            filePath = properties.getProperty("filepath.prefix.images");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "/opt/data/mediaserver/newmedia/";
    }

    private static InputStream getInputStream() {
        InputStream iStream
                = FilePropertiesHelper.class.getClassLoader().getResourceAsStream("config.properties");
        return iStream;
    }
}