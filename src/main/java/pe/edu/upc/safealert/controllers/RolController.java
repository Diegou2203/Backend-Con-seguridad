package pe.edu.upc.safealert.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.RolDTO;
import pe.edu.upc.safealert.entities.Rol;
import pe.edu.upc.safealert.servicesinterfaces.IRolService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rol")
@Slf4j
public class RolController {
//SOLO ACTUA ADMIN
    @Autowired
    private IRolService rS;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<RolDTO> listarRol() {
        log.info("Solicitud GET para listar todos los roles");
        return rS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, RolDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertarRol(@RequestBody RolDTO RDto) {
        log.info("Solicitud POST para insertar un nuevo rol: {}", RDto);
        ModelMapper modelMapper = new ModelMapper();
        Rol r = modelMapper.map(RDto, Rol.class);
        rS.insert(r);
        log.debug("Rol insertado correctamente");
    }

    @GetMapping("/list/{idRol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RolDTO listarId(@PathVariable("idRol") int idRol) {
        log.info("Solicitud GET para obtener rol por ID: {}", idRol);
        ModelMapper m = new ModelMapper();
        return m.map(rS.listarId(idRol), RolDTO.class);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificarRol(@RequestBody RolDTO RDto) {
        log.info("Solicitud PUT para modificar un rol: {}", RDto);
        ModelMapper m = new ModelMapper();
        Rol r = m.map(RDto, Rol.class);
        rS.update(r);
        log.debug("Rol modificado correctamente");
    }

    @DeleteMapping("/delete/{idRol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarRol(@PathVariable("idRol") int idRol) {
        log.info("Solicitud DELETE para eliminar un rol: {}", idRol);
        try {
            rS.delete(idRol);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar el rol porque está en uso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
        }
    }


}
