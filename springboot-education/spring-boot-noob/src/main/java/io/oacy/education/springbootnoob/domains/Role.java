package io.oacy.education.springbootnoob.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String descr;

}
