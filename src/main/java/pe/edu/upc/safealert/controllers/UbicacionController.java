package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.UbicacionDTO;
import pe.edu.upc.safealert.dtos.UsuariosAltoRiesgoDTO;
import pe.edu.upc.safealert.entities.Ubicacion;
import pe.edu.upc.safealert.servicesinterfaces.IUbicacionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ubicaciones")
@Slf4j
public class UbicacionController {

    @Autowired
    private IUbicacionService uS;

    @GetMapping("/list")
    public List<UbicacionDTO> listarUbicaciones() {
        log.info("Solicitud GET para listar todas las ubicaciones");
        return uS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, UbicacionDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody UbicacionDTO uDTO) {
        log.info("Solicitud POST para insertar una nueva ubicación: {}", uDTO);
        ModelMapper modelMapper = new ModelMapper();
        Ubicacion u = modelMapper.map(uDTO, Ubicacion.class);
        uS.insert(u);
        log.debug("Ubicación insertada exitosamente");
    }

    @GetMapping("/list/{idUbicacion}")
    public UbicacionDTO listarId(@PathVariable("idUbicacion") int idUbicacion) {
        log.info("Solicitud GET para obtener ubicación con ID: {}", idUbicacion);
        ModelMapper m = new ModelMapper();
        return m.map(uS.listarId(idUbicacion), UbicacionDTO.class);
    }

    @DeleteMapping("/delete/{idUbicacion}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable("idUbicacion") int idUbicacion) {
        log.warn("Solicitud DELETE para eliminar ubicación con ID: {}", idUbicacion);
        uS.delete(idUbicacion);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificar(@RequestBody UbicacionDTO uDTO) {
        log.info("Solicitud PUT para modificar ubicación: {}", uDTO);
        ModelMapper m = new ModelMapper();
        Ubicacion u = m.map(uDTO, Ubicacion.class);
        uS.update(u);
        log.debug("Ubicación modificada correctamente");
    }

    @GetMapping("/list/ListaUsuariosPorZonasAltoRiesgo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UsuariosAltoRiesgoDTO> ListarUsuariosEnZonasDeAltoRiesgo() {
        log.info("GET request: listar usuarios en zonas de alto riesgo");
        List<String[]> data = uS.findUsuariosEnZonasDeAltoRiesgo();
        List<UsuariosAltoRiesgoDTO> dtos = new ArrayList<>();

        for (String[] columna : data) {
            UsuariosAltoRiesgoDTO dto = new UsuariosAltoRiesgoDTO();
            dto.setUsername(columna[0]);
            dto.setTelefono(columna[1]);
            dto.setCorreo(columna[2]);
            dto.setCiudad(columna[3]);
            dto.setLatitud(Double.parseDouble(columna[4]));
            dto.setLongitud(Double.parseDouble(columna[5]));
            dto.setAltitud(Double.parseDouble(columna[6]));
            dto.setPais(columna[7]);
            dtos.add(dto);
        }
        log.debug("Usuarios en zonas de alto riesgo encontrados: {}", dtos.size());
        return dtos;
    }
}
