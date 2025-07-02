package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.BusquedaComentarioPorTemaDTO;
import pe.edu.upc.safealert.dtos.CantidadRespuestaxComentarioDTO;
import pe.edu.upc.safealert.dtos.ComentarioConsultaDTO;
import pe.edu.upc.safealert.entities.ComentarioConsulta;
import pe.edu.upc.safealert.servicesinterfaces.IComentarioConsultaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//AMBOS ROLES HACEN ALL EXCEPTO QUE EL USUARIO ES EL UNICO QUE SUBE UN COMENTARIO

@RestController
@RequestMapping("/comentario")
@Slf4j // Lombok
public class ComentarioConsultaController {

    @Autowired
    private IComentarioConsultaService coS;
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    @GetMapping("/list")
    public List<ComentarioConsultaDTO> listarComentario() {
        log.info("Solicitud GET para listar todos los comentarios");
        return coS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, ComentarioConsultaDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('USUARIO')")
    public void insertar(@RequestBody ComentarioConsultaDTO coDto) {
        log.info("Solicitud POST para insertar un nuevo comentario: {}", coDto);
        ModelMapper modelMapper = new ModelMapper();
        ComentarioConsulta co = modelMapper.map(coDto, ComentarioConsulta.class);
        coS.insert(co);
        log.debug("Comentario insertado con éxito");
    }

    // GET: obtener comentario por ID
    @GetMapping("/list/{idComentario}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    public ComentarioConsultaDTO listarId(@PathVariable("idComentario") int idComentario) {
        log.info("Solicitud GET para obtener comentario con ID: {}", idComentario);
        ModelMapper m = new ModelMapper();
        ComentarioConsultaDTO coDTO = m.map(coS.listarId(idComentario), ComentarioConsultaDTO.class);
        return coDTO;
    }


    @DeleteMapping("/delete/{idComentario}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    public void eliminar(@PathVariable("idComentario") int idComentario) {
        log.warn("Solicitud DELETE para eliminar comentario con ID: {}", idComentario);
        coS.delete(idComentario);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    public void modificar(@RequestBody ComentarioConsultaDTO coDTO) {
        log.info("Solicitud PUT para modificar comentario: {}", coDTO);
        ModelMapper m = new ModelMapper();
        ComentarioConsulta co = m.map(coDTO, ComentarioConsulta.class);
        coS.update(co);
        log.debug("Comentario modificado con éxito");
    }

    @GetMapping("/list/ComentarioPorTema")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    public List<BusquedaComentarioPorTemaDTO> cantidadPorComentario(@RequestParam String tema) {
        log.info("Solicitud GET para obtener informacion de un comentario filtrando por tema");
        List<BusquedaComentarioPorTemaDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = coS.BusquedaPorTema(tema);
        for (String[] columna : filaLista) {
            BusquedaComentarioPorTemaDTO dto = new BusquedaComentarioPorTemaDTO();
            dto.setUsername(columna[0]);
            dto.setContenido(columna[1]);
            dto.setFechaComentario(LocalDate.parse(columna[2]));
            dtoLista.add(dto);
        }
        log.debug("Comentario filtrado por tema");
        return dtoLista;
    }

    @GetMapping("/list/CantidadRespuestasPorComentario")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USUARIO')")
    public List<CantidadRespuestaxComentarioDTO> cantidadRespuestas() {
        log.info("Solicitud GET para contar respuestas por comentario");
        List<CantidadRespuestaxComentarioDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = coS.cantidadRespuestasPorComentario();
        for (String[] columna : filaLista) {
            CantidadRespuestaxComentarioDTO dto = new CantidadRespuestaxComentarioDTO();
            dto.setContenido(columna[0]);
            dto.setEstado(columna[1]);
            dto.setCantidad(Integer.parseInt(columna[2]));
            dtoLista.add(dto);
        }
        return dtoLista;
    }
}
