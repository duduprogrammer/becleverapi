package com.beclever.apirest_desafio.modelos;



import java.time.LocalDate;


import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "registers")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
    

    private Long idEmployee; //ID Empleado
  
    private String registerType; //Entró - Salio
  
    

    private String descriptionFilter; //Si deseas filtrar por Algun Nombre o Apellido
    

    


    private String gender;

    
	// Getters y setters
    private int entryCount;
    private int exitCount;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate date;

	
    
    @Column(name = "business_location")

	public String businessLocation; //Nombre Sucursal
    
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Sucursal branch;
    
   

    public int getEntryCount() {
        return this.entryCount;
    }

    public int getExitCount() {
        return this.exitCount;
    }
    
    
    public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public void setExitCount(int exitCount) {
		this.exitCount = exitCount;
	}



	public Sucursal getBranch() {
		return branch;
	}


	public void setBranch(Sucursal branch) {
		this.branch = branch;
	}


	public String getGender() {
  		return gender;
  	}


	public void setGender(String gender) {
  		this.gender = gender;
  	}
  	public String getDescriptionFilter() {
  		return descriptionFilter;
  	}
  	public void setDescriptionFilter(String descriptionFilter) {
  		this.descriptionFilter = descriptionFilter;
  	}


  	
  	
  	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdEmployee() {
		return idEmployee;
	}
	public void setIdEmployee(Long idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getBusinessLocation() {
		return businessLocation;
	}
	public void setBusinessLocation(String businessLocation) {
		this.businessLocation = businessLocation;
	}




    
    
}
