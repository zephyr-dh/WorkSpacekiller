package io.oacy.education.xunwu.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "house_tag")
public class HouseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_id")
    private Long houseId;

    private String name;
}
