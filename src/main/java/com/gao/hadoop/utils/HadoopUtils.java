package com.gao.hadoop.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
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

    private HadoopUtils() {
    }

    public void createFileSystem(Configuration configuration) {
        try {
            fs = FileSystem.get(configuration);
        } catch (IOException e) {
            logger.error("create fileSystem is failed.", e);
        }
    }

    public Path[] listFiles(String path) {
        logger.info("start to read path [{}].", path);
        Path[] paths = null;
        try {
            FileStatus[] fileStatuses = fs.listStatus(new Path(path));
            paths = FileUtil.stat2Paths(fileStatuses);
        } catch (IOException e) {
            logger.error("read hdfs file path [{}] is failed.", path, e);
        }
        return paths;
    }


    public void mkdirs(String path) {
        logger.info("start create hadoop dir [{}].", path);
        try {
            fs.mkdirs(new Path(path), FsPermission.createImmutable((short) 777));
        } catch (IOException e) {
            logger.error("create hadoop dir [{}] is failed.", path, e);
        }
    }

    public boolean copyFromLocalFile(String localPath, String targetPath) {
        logger.info("start copy from  [{}] to [{}]. ", localPath, targetPath);
        try {
            fs.copyFromLocalFile(new Path(localPath), new Path(targetPath));
            return exists(targetPath);
        } catch (IOException e) {
            logger.error("copy from  [{}] to [{}] is failed.", localPath, targetPath);
        }
        return false;
    }

    public boolean exists(String path) {
        try {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            logger.error("query hadoop path [{}] is failed.", path);
        }
        return false;
    }

    public static HadoopUtils getInstance() {
        if (hadoopUtils == null) {
            synchronized (HadoopUtils.class) {
                if (hadoopUtils == null) {
                    hadoopUtils = new HadoopUtils();
                }
            }
        }
        return hadoopUtils;
    }

    public void close() {
        if (fs != null) {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error("close hadoop client is failed.");
            }
        }
    }
}
