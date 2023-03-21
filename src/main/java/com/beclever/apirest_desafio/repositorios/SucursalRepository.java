package com.beclever.apirest_desafio.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beclever.apirest_desafio.modelos.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

}