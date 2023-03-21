package com.beclever.apirest_desafio.modelos;

import javax.persistence.*;

@Entity
@Table(name = "sucursales")
public class Sucursal {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    
    private Long id;
 
    @Column(name = "nombre")
    private String nombre;

    // otros atributos y métodos
    
    
    
    
    
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
 
    
    
    
    
    
    
    
    
    
    
}