package io.oacy.education.xunwu.web.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QiniuPutRet {
    public String key;
    public String hash;
    public String bucket;
    public int width;
    public int height;
}
