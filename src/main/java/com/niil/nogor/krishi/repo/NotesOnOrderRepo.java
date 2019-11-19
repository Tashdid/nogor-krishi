package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.DeliveryPerson;
import com.niil.nogor.krishi.entity.NotesOnOrder;


@Repository
public interface NotesOnOrderRepo extends JpaRepository<NotesOnOrder, Long> {

}
