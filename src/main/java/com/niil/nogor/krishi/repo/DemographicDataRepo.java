package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.DemographicData;

@Repository
public interface DemographicDataRepo extends JpaRepository<DemographicData, Long>{

	List<DemographicData> findAllByOrderBySequenceAsc();

	List<DemographicData> findAllByParentIdIsNullOrderByNameAsc();
	
	List<DemographicData> findAllByParentIdIsNotNull();
	
	List<DemographicData> findAllByParentIdOrderByNameAsc(Long parentId);
	
	List<DemographicData> findAllByTypeOrderByNameAsc(Byte type);
	
	DemographicData findTopByOrderBySequenceDesc();

}
