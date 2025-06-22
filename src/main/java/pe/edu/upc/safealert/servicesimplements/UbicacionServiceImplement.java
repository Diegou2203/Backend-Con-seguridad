package pe.edu.upc.safealert.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.safealert.entities.Ubicacion;
import pe.edu.upc.safealert.repositories.IUbicacionRepository;
import pe.edu.upc.safealert.servicesinterfaces.IUbicacionService;

import java.util.List;

@Service
public class UbicacionServiceImplement implements IUbicacionService {

    @Autowired
    private IUbicacionRepository uR;

    @Override
    public List<Ubicacion> list() {return uR.findAll();}

    @Override
    public void insert(Ubicacion u) {uR.save(u);}

    @Override
    public Ubicacion listarId(int idUbicacion) {return uR.findById(idUbicacion).orElse(new Ubicacion());}

    @Override
    public void delete(int idUbicacion) {uR.deleteById(idUbicacion);}

    @Override
    public void update(Ubicacion u) {uR.save(u);}

    @Override
    public List<String[]> findUsuariosEnZonasDeAltoRiesgo() {
        return uR.findUsuariosEnZonasDeAltoRiesgo();
    }
}
