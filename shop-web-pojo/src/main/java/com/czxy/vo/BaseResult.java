package com.czxy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult {

    private Integer errno;
    private String errmsg;
    private Object data;

    public BaseResult(Integer errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }
}