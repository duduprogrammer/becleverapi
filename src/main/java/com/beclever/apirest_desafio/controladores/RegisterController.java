package com.beclever.apirest_desafio.controladores;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beclever.apirest_desafio.modelos.Register;
import com.beclever.apirest_desafio.modelos.Sucursal;
import com.beclever.apirest_desafio.repositorios.RegisterRepository;
import com.beclever.apirest_desafio.repositorios.SucursalRepository;
import com.beclever.apirest_desafio.servicios.RegisterService;


@Controller
@RequestMapping
public class RegisterController {
	@Autowired
	private RegisterRepository repository;
	@Autowired
    private SucursalRepository branchRepository;
	@PersistenceContext
    private EntityManager entityManager;
 	private final RegisterService empleadoService;

    public RegisterController(RegisterService empleadoService) {
        this.empleadoService = empleadoService;
    }
    
    //=================================================MOSTRAR=================================================
    
    @GetMapping
    public String mostrarEmpleado(Model model)
    {
    	
    	List<Register> empleado = repository.findAll();
        model.addAttribute("empleado", empleado);
        return "tabla-empleados";
    }
    //=================================================EDITAR=================================================
    
    @GetMapping("/registers/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Register empleado = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Empleado inválido"));
        model.addAttribute("empleado", empleado);
        
        LocalDate fecha = LocalDate.now();
        model.addAttribute("date", fecha.toString()); // Convertir LocalDate a String
        empleado.getBranch().getNombre();
    
        return "edit-employee";
    }
    
    @PostMapping("/registers/edit/{id}")
    public String guardarCambios(@PathVariable("id") Long id, @ModelAttribute("empleado") Register empleado, BindingResult result, Model model) {
        if (result.hasErrors()) {
            empleado.setId(id);
            return "edit-employee";
        }
        LocalDate fecha = LocalDate.now();
        empleado.getBranch().getNombre();
        model.addAttribute("date", fecha.toString()); // Convertir LocalDate a String
        repository.save(empleado);
        return "redirect:/";
    }
    //=================================================BUSCAR=================================================
    


    @GetMapping("/registers/search")
    
    
	public String search(
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  dateFrom,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  dateTo,
		    @RequestParam(required = false) String descriptionFilter,
		    @RequestParam(required = false) String businessLocation
		, Model model) {
    	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Register> query = builder.createQuery(Register.class);
    	Root<Register> register = query.from(Register.class);
    	Subquery<String> subquery = query.subquery(String.class);
    	Root<Sucursal> sucursal = subquery.from(Sucursal.class);
    	subquery.select(sucursal.get("nombre")).where(builder.equal(sucursal.get("id"), register.get("branch")));
    	@SuppressWarnings("rawtypes")
    	List<Predicate> predicates = new ArrayList<>();

    	if (dateFrom != null && dateTo != null) {
    	    Predicate datePredicate = builder.between(register.get("date"), dateFrom, dateTo);
    	    predicates.add(datePredicate);
    	}

    	if (descriptionFilter != null) {
    	    Predicate descriptionPredicate = builder.like(register.get("descriptionFilter"), "%" + descriptionFilter + "%");
    	    predicates.add(descriptionPredicate);
    	}

    	if (businessLocation != null) {
    	    Predicate businessPredicate = builder.like(subquery.getSelection(), "%" + businessLocation + "%");
    	    predicates.add(businessPredicate);
    	    
    	    
    	}

    	query.where(predicates.toArray(new Predicate[predicates.size()]));

    	List<Register> registers = entityManager.createQuery(query).getResultList();

    	model.addAttribute("registers", registers);
    	return "search";

}
    

//=======================================AGREGA=================================================
    @GetMapping("/registers/add")
    public String nuevoUsuario(Model model) {
        model.addAttribute("empleado", new Register());
        
        
        return "add";
    }
    @PostMapping("/registers/add")
    public String agregarUsuario(@ModelAttribute("empleado") Register empleado, BindingResult result, Model model) {
        if (result.hasErrors()) 
        {
        	model.addAttribute("errors", result.getAllErrors());
            return "add";
        }
    
        repository.save(empleado);
        return "redirect:/";
    }
    //=================================================ELIMINAR=================================================
    @GetMapping("registers/delete/{id}")
    public String showDelete(@PathVariable("id") Long id, Model model) {
        Optional<Register> employeeOptional = repository.findById(id);
        if (employeeOptional.isPresent()) {
           
            repository.deleteById(id);
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "El empleado con el id " + id + " no existe.");
            return "error";
        }
    }
    
    @GetMapping("registers/promedios")
    public String mostrarPromedios(Model model)
    {
    	
    	List<Register> empleado = repository.findAll();
        model.addAttribute("empleado", empleado);
        return "promedios";
    }
    //===============================A PARTIR DE ACÁ ABAJO, TRABAJAMOS CON LAS APIS BECLEVER=========================================================
   
    @GetMapping("api/average")
    @ResponseBody
    public Map<String, Map<String, Double>> getAverageEntryExitByDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Map<String, Double>> result = new HashMap<>();

        List<Sucursal> branches = branchRepository.findAll();
        for (Sucursal branch : branches) {
            List<Register> entryExits = repository.findByBranchIdAndDateBetween(branch.getId(), startDate, endDate);
            int maleEnterCount = 0;
            int femaleEnterCount = 0;
            int maleExitCount = 0;
            int femaleExitCount = 0;

            for (Register entryExit : entryExits)
            {
            
				String gender = entryExit.getGender();
                if (gender.equals("M")) {
                    maleEnterCount += entryExit.getEntryCount();
                    maleExitCount += entryExit.getExitCount();
                } 
                else if (gender.equals("F")) 
                {
                    femaleEnterCount += entryExit.getEntryCount();
                    femaleExitCount += entryExit.getExitCount();
                }
            }

            Map<String, Double> branchData = new HashMap<>();
            branchData.put("averageEntryMen", calculateAverage(maleEnterCount, entryExits.size()));
            branchData.put("averageExitMen", calculateAverage(maleExitCount, entryExits.size()));
            branchData.put("averageEntryWomen", calculateAverage(femaleEnterCount, entryExits.size()));
            branchData.put("averageExitWomen", calculateAverage(femaleExitCount, entryExits.size()));

            result.put(branch.getNombre(), branchData);
        }

        return result;
    }
 
    private double calculateAverage(int count, int total) {
        return total == 0 ? 0 : (double) count / total;
    }
    @ResponseBody
    @GetMapping("api/search")
    
	public List<Register> buscarEmpleados(
		    @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  dateFrom,
		    @RequestParam("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  dateTo,
		    @RequestParam(required = false) String descriptionFilter,
		    @RequestParam(required = false) String businessLocation
		) {
    	
       	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Register> query = builder.createQuery(Register.class);
    	Root<Register> register = query.from(Register.class);
    	Subquery<String> subquery = query.subquery(String.class);
    	Root<Sucursal> sucursal = subquery.from(Sucursal.class);
    	subquery.select(sucursal.get("nombre")).where(builder.equal(sucursal.get("id"), register.get("branch")));
    	@SuppressWarnings("rawtypes")
    	List<Predicate> predicates = new ArrayList<>();

    	if (dateFrom != null && dateTo != null) {
    	    Predicate datePredicate = builder.between(register.get("date"), dateFrom, dateTo);
    	    predicates.add(datePredicate);
    	}

    	if (descriptionFilter != null) {
    	    Predicate descriptionPredicate = builder.like(register.get("descriptionFilter"), "%" + descriptionFilter + "%");
    	    predicates.add(descriptionPredicate);
    	}

    	if (businessLocation != null) {
    	    Predicate businessPredicate = builder.like(subquery.getSelection(), "%" + businessLocation + "%");
    	    predicates.add(businessPredicate);
    	}

    	query.where(predicates.toArray(new Predicate[predicates.size()]));

    	List<Register> registers = entityManager.createQuery(query).getResultList();


    return registers;
}


	//=================================== 	SERVICES 1 		===============================================//

    @ResponseBody
	@GetMapping("api/registers")
	public List<Register> allPersons(){
		return repository.findAll();
	}
	

    @ResponseBody
	@PostMapping("api/register")
	public Register createRegister(@RequestBody Register regist) {
		return repository.save(regist);
	}
	@GetMapping("/register/{id}")
	public Register obtenerPorId(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
	 
	
	
	@ResponseBody
	@PutMapping("api/register/{id}")
	
	public Register actualizar(@PathVariable Long id, @RequestBody Register personalActualizado) {
		Register personal = repository.findById(id).orElse(null);
        if (personal != null) {
            personal.setIdEmployee(personalActualizado.getIdEmployee());
          
            personal.setRegisterType(personalActualizado.getRegisterType());
            personal.setBusinessLocation(personalActualizado.getBusinessLocation());
            return repository.save(personal);
        } else {
            return null;
        }
    }
	
	@ResponseBody
	@DeleteMapping("api/register/{id}")
	public void deleteRegister(@PathVariable("id") Long id) {
		repository.deleteById(id);
	}
	//===================================FIN DE SERVICES 1================================================//

}







