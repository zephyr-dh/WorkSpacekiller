package io.oacy.education.ssh.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-20 10:55 PM
 */

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created")
    private Long created = System.currentTimeMillis();

    @Column(name = "username")
    private String username;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "remark")
    private String remark;
}
