package io.oacy.education.ssh.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-20 10:57 PM
 */

public interface DomainRepository<T, PK extends Serializable> {
    T load(PK id);

    T get(PK id);

    List<T> findAll();

    void persist(T entity);

    Long save(T entity);

    void saveOrUpdate(T entity);

    void delete(PK id);

    void flush();
}
