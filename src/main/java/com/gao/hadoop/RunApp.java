package com.gao.hadoop;

import com.gao.hadoop.utils.FileUtils;
import com.gao.hadoop.utils.HadoopUtils;
import com.gao.hadoop.utils.KerberosUtils;
import com.gao.hadoop.utils.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 11526
 */
public class RunApp {
    private static final Logger logger = LoggerFactory.getLogger(RunApp.class);

    public void runApp(Map<String, String> map) {
        String krb5Path = map.get("krb5Path");
        String keytabPath = map.get("keytabPath");
        String principal = map.get("principal");
        String hadoopConfDir = System.getenv().get("HADOOP_CONF_DIR");

        if (StringUtils.isEmpty(krb5Path) || !FileUtils.checkFileIsExist(krb5Path)) {
            logger.error("The krb5.conf path [{}] is not exist.", krb5Path);
            System.exit(-1);
        }
        if (StringUtils.isEmpty(keytabPath) || !FileUtils.checkFileIsExist(keytabPath)) {
            logger.error("The keytab path [{}] is not exist.", keytabPath);
            System.exit(-1);
        }
        if (StringUtils.isEmpty(principal)) {
            logger.error("The principal is be empty.");
            System.exit(-1);
        }
        if (StringUtils.isEmpty(hadoopConfDir) || !FileUtils.checkDirectoryIsExist(hadoopConfDir)){
            logger.error("The HADOOP_CONF_DIR is be empty.");
            System.exit(-1);
        }
        Configuration config = new Configuration();
        KerberosUtils.tryKerberosLogin(config, keytabPath, krb5Path, principal);
        HadoopUtils instance = HadoopUtils.getInstance();
        instance.createFileSystem(config);


        Path[] paths = instance.listFiles("");
        System.out.println(Arrays.toString(paths));
    }

    public static void main(String[] args) {
        Map<String, String> map = StringUtils.argsToMap(args);
        RunApp runApp = new RunApp();
        runApp.runApp(map);
    }
}
