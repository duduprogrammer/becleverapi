package com.beclever.apirest_desafio.repositorios;




import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beclever.apirest_desafio.modelos.Register;


@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> 
{
	  
	List<Register> findByBranchIdAndDateBetween(long Branch, LocalDate startDate, LocalDate endDate);
	
	 List<Register> findByBusinessLocation(Long businessLocation);
	 
	 
	 
	@Query("SELECT r FROM Register r WHERE "
            + "(:dateFrom IS NULL OR r.date >= :dateFrom) AND "
            + "(:dateTo IS NULL OR r.date <= :dateTo) AND "
            + "(:descriptionFilter IS NULL OR (LOWER(r.descriptionFilter) LIKE CONCAT('%', LOWER(:descriptionFilter), '%'))) AND "
            + "(:businessLocation IS NULL OR (LOWER(r.businessLocation) LIKE CONCAT('%', LOWER(:businessLocation), '%'))) "
            + "ORDER BY r.date DESC")


	 
	    List<Register> findBySearchCriteria(
	            @Param("dateFrom") LocalDate dateFrom, 
	            @Param("dateTo") LocalDate dateTo, 
	            @Param("descriptionFilter") String descriptionFilter, 
	            @Param("businessLocation") String businessLocation
	    );
}
