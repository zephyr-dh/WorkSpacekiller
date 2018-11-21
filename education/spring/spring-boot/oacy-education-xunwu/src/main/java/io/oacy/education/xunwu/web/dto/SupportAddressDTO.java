package io.oacy.education.xunwu.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupportAddressDTO {

    private Long id;
    @JsonProperty(value = "belong_to")
    private String belongTo;

    @JsonProperty(value = "en_name")
    private String enName;

    @JsonProperty(value = "cn_name")
    private String cnName;

    private String level;

    @JsonProperty(value = "baidu_map_lng")
    private double baiduMapLongitude;

    @JsonProperty(value = "baidu_map_lat")
    private double baiduMapLatitude;
}
