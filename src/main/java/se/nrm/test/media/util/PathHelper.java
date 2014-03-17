/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.test.media.util;

import java.io.File;

/**
 *
 * @author ingimar
 */
public class PathHelper {

    /**
     * Creates the directory and saves the file.
     *
     * @param uuid
     * @return
     */
    public static String getDynamicUUUIDFile(String uuid) {
        String pathen = getPath(uuid);
        File directory = new File(pathen);
        boolean isDir = false;
        isDir = directory.mkdirs();
        return pathen.concat(uuid);
    }

    public static String getDyanmicPathToFile(String uuid) {
        String pathen = getPath(uuid);
        return pathen;
    }

    private static String getPath(String uuid) {
        final String IMAGE_PATH = getAbsPath();
        StringBuilder tmpPath = new StringBuilder(IMAGE_PATH);
        tmpPath.append(uuid.charAt(0)).append("/").append(uuid.charAt(1)).append("/").append(uuid.charAt(2)).append("/");
        String pathen = tmpPath.toString();
        return pathen;
    }

    private static String getAbsPath() {
        final String IMAGE_PATH = FilePropertiesHelper.getImagesFilePath();
        System.out.println("filepath "+ IMAGE_PATH);
        return IMAGE_PATH;
    }
}
