package com.gao.hadoop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gao.hadoop.artifact.ArtifactTransport;
import com.gao.hadoop.enums.TransportType;
import com.gao.hadoop.utils.FileUtils;
import com.gao.hadoop.utils.HadoopUtils;
import com.gao.hadoop.utils.KerberosUtils;
import com.gao.hadoop.utils.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * @author 11526
 */
public class RunApp {
    private static final Logger logger = LoggerFactory.getLogger(RunApp.class);
    private static final ArtifactTransport ARTIFACT_TRANSPORT = new ArtifactTransport();

    public void runApp(Map<String, String> map) {
        String hadoopConfDir = System.getenv().get("HADOOP_CONF_DIR");
        if (StringUtils.isEmpty(hadoopConfDir) || !FileUtils.checkDirectoryIsExist(hadoopConfDir)) {
            logger.error("The HADOOP_CONF_DIR is be empty.");
            System.exit(-1);
        }
        Configuration config = new Configuration();

        KerberosUtils.loginKerberos(config, map);

        HadoopUtils instance = HadoopUtils.getInstance();
        instance.createFileSystem(config);

        execute(instance, map);
    }

    public void execute(HadoopUtils instance, Map<String, String> map) {
        String artifactsJson = map.get("artifact-json");
        if (StringUtils.isEmpty(artifactsJson)) {
            logger.error("please enter artifactPath, targetPath and transportType");
            System.exit(-1);
        }
        try {
            JSONArray jsonArray = JSONArray.parseArray(artifactsJson);

            jsonArray.parallelStream().forEach(item -> {
                try {
                    JSONObject artifactJsonObject = (JSONObject) item;
                    String artifactPath = artifactJsonObject.getString("artifactPath");
                    String targetPath = artifactJsonObject.getString("targetPath");
                    String transportType = artifactJsonObject.getString("transportType");
                    boolean aBoolean = false;
                    if (TransportType.UPLOAD.getType().equals(transportType.toLowerCase(Locale.ROOT))){
                        aBoolean = ARTIFACT_TRANSPORT.local2Hdfs(instance, artifactPath, targetPath);
                    }else if (TransportType.DOWNLOAD.getType().equals(transportType.toLowerCase(Locale.ROOT))){
                        aBoolean = ARTIFACT_TRANSPORT.hdfs2local(instance, artifactPath, targetPath);
                    }else {
                        logger.warn("The transportType value [{}] input error or empty. please re-enter [{}] json", transportType, artifactPath);
                    }
                    if (aBoolean){
                        logger.info("transport artifact [{}] is success. target path list {}.", artifactJsonObject, Arrays.toString(instance.listFiles(targetPath)));
                    }else {
                        logger.warn("transport artifact [{}] is failed.", artifactJsonObject);
                    }
                } catch (Exception e) {
                    logger.error("transport artifact is failed. [{}]", item, e);
                }
            });
        } catch (Exception e) {
            logger.error("", e);
        }finally {
            instance.close();
        }

    }

    public static void main(String[] args) {
        Map<String, String> map = StringUtils.argsToMap(args);
        RunApp runApp = new RunApp();
        runApp.runApp(map);

    }
}
