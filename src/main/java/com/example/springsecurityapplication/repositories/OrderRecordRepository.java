package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.OrderRecord;
import com.example.springsecurityapplication.models.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface OrderRecordRepository extends JpaRepository<OrderRecord, Integer> {

    List<OrderRecord> findByPerson(Person person);

    @Query(value = "select * from order_person where person_id = ?1", nativeQuery = true)
    List<OrderRecord> findOrderByPersonId(int id);

    @Query(value = "select * from order_person where id = ?1", nativeQuery = true)
    OrderRecord findOrderById(int id);

    @Modifying
    @Query(value = "delete from order_person where id=?1", nativeQuery = true)
    void deletePersonOrderById(int id);

    List<OrderRecord> findByNumberEndingWithIgnoreCase(String endingWith);

}
