package com.gao.hadoop.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author 11526
 */
public class KerberosUtils {
    private static final Logger logger = LoggerFactory.getLogger(KerberosUtils.class);

    public static void tryKerberosLogin(Configuration configuration,
                                        String keytabPath,
                                        String krb5Path,
                                        String principal){
        logger.info("start kerberos authentication.");

        try {
            System.setProperty("java.security.krb5.conf", krb5Path);
            UserGroupInformation.setConfiguration(configuration);
            // 登录名和keytab文件
            UserGroupInformation.loginUserFromKeytab(principal,keytabPath);
        } catch (IOException e) {
            logger.error("login kerberos user [{}] is failed.", principal, e);
            System.exit(-1);
        }
    }

    public static void loginKerberos(Configuration conf, Map<String, String> map){
        String krb5Path = map.get("krb5Path");
        String keytabPath = map.get("keytabPath");
        String principal = map.get("principal");

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
        KerberosUtils.tryKerberosLogin(conf, keytabPath, krb5Path, principal);
    }
}
