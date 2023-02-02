package com.gao.hadoop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author 11526
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static Boolean checkFileIsExist(String filePath){
        try {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        } catch (Exception e) {
            logger.error("Get file [{}] is failed." ,filePath, e);
        }
        return false;
    }

    public static Boolean checkDirectoryIsExist(String filePath){
        try {
            File file = new File(filePath);
            return file.exists() && file.isDirectory();
        } catch (Exception e) {
            logger.error("Get directory [{}] is failed." ,filePath, e);
        }
        return false;
    }
}
