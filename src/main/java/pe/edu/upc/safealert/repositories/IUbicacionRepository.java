package pe.edu.upc.safealert.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.safealert.entities.Ubicacion;

import java.util.List;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    @Query(value = "SELECT u.username, u.telefono, u.correo, ub.ciudad, ub.latitud, ub.longitud, ub.altitud, ub.pais\n" +
            "FROM usuario u\n" +
            "INNER JOIN ubicacion ub ON u.id_usuario = ub.id_usuario\n" +
            "INNER JOIN fenomeno_natural fn ON ub.id_ubicacion = fn.id_ubicacion\n" +
            "WHERE UPPER(fn.intensidad) = 'ALTA' AND fn.activo = true\n" +
            "GROUP BY u.username, u.telefono, u.correo, ub.ciudad, ub.latitud, ub.longitud, ub.altitud, ub.pais",
            nativeQuery = true)
    public List<String[]> findUsuariosEnZonasDeAltoRiesgo();
}
