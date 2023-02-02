package com.gao.hadoop.artifact;

import com.gao.hadoop.utils.HadoopUtils;
import com.gao.hadoop.utils.StringUtils;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author 11526
 */
public class UploadArtifact {
    private static final Logger logger = LoggerFactory.getLogger(UploadArtifact.class);

    /**
     * 本地文件上传到hadoop
     *
     * @param localPath  本地文件地址
     * @param targetPath hadoop文件地址
     * @return boolean
     */
    public boolean local2Hdfs(HadoopUtils instance ,String localPath, String targetPath) {
        if (StringUtils.isEmpty(localPath) || StringUtils.isEmpty(targetPath)) {
            logger.error("The local path [{}] or target path [{}] is be empty.", localPath, targetPath);
            return false;
        }
        try {
            File file = new File(localPath);
            if (file.isFile() || file.isDirectory()) {
                if (!instance.exists(targetPath)){
                    instance.mkdirs(targetPath);
                }
                return instance.copyFromLocalFile(localPath, targetPath);
            }else {
                logger.warn("local path [" + localPath + "] is not exist.");
            }
        } catch (Exception e) {
            logger.error("upload artifact from [{}] to [{}] is failed.", localPath, targetPath, e);
        }
        return false;
    }

    /**
     * hadoop下载到本地
     *
     * @param hdfsPath   hadoop文件地址
     * @param targetPath 本地文件地址
     * @return boolean
     */
    public boolean hdfs2local(String hdfsPath, String targetPath) {
        return false;
    }
}
