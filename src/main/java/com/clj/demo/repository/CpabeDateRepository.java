package com.clj.demo.repository;

import com.clj.demo.entity.CpabeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface  CpabeDateRepository extends JpaRepository<CpabeData, Integer> {

    @Query(value = "select * from cpabe_data order by timestamp desc",nativeQuery = true)
    Iterable<CpabeData> findAllOderByDesc();

    @Query(value = "select * from cpabe_data where cpabe_data.user_id=?1",nativeQuery = true)
    Iterable<CpabeData> findCpabeDataByUid(Integer user_id);

    @Query(value = "select * from cpabe_data where cpabe_data.tile=?1",nativeQuery = true)
    Iterable<CpabeData> findCpabeDataByTile(String tile);
}