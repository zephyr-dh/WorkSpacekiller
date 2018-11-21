package io.oacy.education.xunwu.repository;


import io.oacy.education.xunwu.domain.Subway;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubwayRepository extends CrudRepository<Subway, Long> {
    List<Subway> findAllByCityEnName(String cityEnName);
}
