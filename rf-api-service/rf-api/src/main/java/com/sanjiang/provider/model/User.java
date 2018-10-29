package com.sanjiang.provider.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by byinbo on 2018/6/27.
 */
@Data
@ToString
public class User implements Serializable{

    private String username;

    private String password;

    private String scbh;

    private String ip;

    private String sessionId;
}
