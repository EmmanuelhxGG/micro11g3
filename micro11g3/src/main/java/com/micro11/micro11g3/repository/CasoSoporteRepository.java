package com.micro11.micro11g3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.micro11.micro11g3.model.CasoSoporte;

@Repository
public interface CasoSoporteRepository extends JpaRepository<CasoSoporte, Integer> {
}
