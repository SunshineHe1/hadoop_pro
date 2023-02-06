package com.gao.hadoop.artifact;

import com.gao.hadoop.utils.HadoopUtils;
import com.gao.hadoop.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author 11526
 */
public class ArtifactTransport {
    private static final Logger logger = LoggerFactory.getLogger(ArtifactTransport.class);

    /**
     * 本地文件上传到hadoop
     *
     * @param artifactPath 本地文件地址
     * @param targetPath   hadoop文件地址
     * @return boolean
     */
    public boolean local2Hdfs(HadoopUtils instance, String artifactPath, String targetPath) {
        if (StringUtils.isEmpty(artifactPath) || StringUtils.isEmpty(targetPath)) {
            logger.error("The local path [{}] or target path [{}] is be empty.", artifactPath, targetPath);
            return false;
        }
        try {
            File file = new File(artifactPath);
            if (file.isFile() || file.isDirectory()) {
                if (!instance.exists(targetPath)) {
                    instance.mkdirs(targetPath);
                }
                return instance.copyFromLocalFile(artifactPath, targetPath);
            } else {
                logger.warn("local path [" + artifactPath + "] is not exist.");
            }
        } catch (Exception e) {
            logger.error("upload artifact from [{}] to [{}] is failed.", artifactPath, targetPath, e);
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
    public boolean hdfs2local(HadoopUtils instance, String hdfsPath, String targetPath) {
        return false;
    }
}
