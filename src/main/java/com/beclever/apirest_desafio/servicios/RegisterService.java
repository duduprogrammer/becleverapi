package com.beclever.apirest_desafio.servicios;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beclever.apirest_desafio.modelos.Register;
import com.beclever.apirest_desafio.modelos.Sucursal;
import com.beclever.apirest_desafio.repositorios.RegisterRepository;
import com.beclever.apirest_desafio.repositorios.SucursalRepository;




@Service
public class RegisterService {
    
	@Autowired
	private RegisterRepository repository;
	@Autowired
	private SucursalRepository sucrepository;
	
	 public List<Register> getUsuariosPorSucursal(Long sucursalId) {
	        List<Register> usuarios = repository.findByBusinessLocation(sucursalId);
	        for (Register usuario : usuarios) {
	            Sucursal sucursal = sucrepository.findById(usuario.getBranch().getId()).orElse(null);
	            usuario.setBranch(sucursal);
	        }
	        return usuarios;
	    }

}
