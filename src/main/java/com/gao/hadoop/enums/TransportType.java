package com.gao.hadoop.enums;

/**
 * @author 11526
 */

public enum TransportType {

    /**
     * 下载
     */
    DOWNLOAD("download"),
    /**
     * 上传
     */
    UPLOAD("upload");

    private String type;

    TransportType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
