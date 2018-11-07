package io.oacy.education.xunwu.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "subway_station")
@Data
public class SubwayStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subway_id")
    private Long subwayId;

    private String name;
}
