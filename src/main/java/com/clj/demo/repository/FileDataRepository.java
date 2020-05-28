package com.clj.demo.repository;

import com.clj.demo.entity.FileData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Integer> {

    @Query(value = "select * from file_data order by timestamp desc",nativeQuery = true)
    Iterable<FileData> findAllOderByDesc();

    @Query(value = "select * from file_data where file_data.user_id=?1",nativeQuery = true)
    Iterable<FileData> findFilesDataByUid(Integer user_id);

    @Query(value = "select * from file_data where file_data.id=?1 AND file_data.user_id=?2",nativeQuery = true)
    Optional<FileData> findFilesDataByIdAndUid(Integer id, Integer user_id);

    @Query(value = "select * from file_data where file_data.tile=?1 AND file_data.user_id=?2",nativeQuery = true)
    Optional<FileData> findFilesDataByTileAndUid(String tile, Integer user_id);

    @Query(value = "select * from file_data where file_data.authorize_role LIKE :role%",nativeQuery = true)
    Iterable<FileData> findFilesDataByRole(@Param("role") String role);

}
