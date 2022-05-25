package com.xgw.serverFireWall.Vo;

import java.io.Serializable;

public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 1142898131697970519L;

    private Integer code;

    private String info;

    private T result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
