package com.cmbc.hadoop.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 11526
 */
public class HadoopUtils {
    private static final Logger logger = LoggerFactory.getLogger(HadoopUtils.class);
    private FileSystem fs;
    private static HadoopUtils hadoopUtils = null;

    private HadoopUtils(){}

    public void createFileSystem(Configuration configuration){
        try {
            fs = FileSystem.get(configuration);
        } catch (IOException e) {
            logger.error("create fileSystem is failed.", e);
        }
    }

    public Path[] listFiles(String path){
        logger.info("start to read path [{}].", path);
        Path[] paths = null;
        try {
            FileStatus[] fileStatuses = fs.listStatus(new Path(path));
            paths = FileUtil.stat2Paths(fileStatuses);
        } catch (IOException e) {
            logger.error("read hdfs file path [{}] is failed.", path);
        }
        return paths;
    }

    public static HadoopUtils getInstance(){
        if (hadoopUtils == null){
            synchronized (HadoopUtils.class){
                if (hadoopUtils == null){
                    hadoopUtils = new HadoopUtils();
                }
            }
        }
        return hadoopUtils;
    }

    public void close(){
        if (fs != null){
            try {
                fs.close();
            } catch (IOException e) {
                logger.error("close hadoop client is failed.");
            }
        }
    }
}
