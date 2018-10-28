package io.oacy.education.newbie.springbootnewbie.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDomain<T> {

    private int code;
    private String message;
    private T data;

}
