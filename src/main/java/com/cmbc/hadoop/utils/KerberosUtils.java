package com.cmbc.hadoop.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
}
