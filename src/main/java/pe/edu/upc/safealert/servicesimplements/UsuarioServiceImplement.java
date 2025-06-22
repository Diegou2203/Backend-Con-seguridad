package pe.edu.upc.safealert.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.safealert.entities.Usuario;
import pe.edu.upc.safealert.repositories.IUsuarioRepository;
import pe.edu.upc.safealert.servicesinterfaces.IUsuarioService;

import java.util.List;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private IUsuarioRepository uR;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> list() {return uR.findAll();}

    @Override
    public void insert(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        uR.save(u);
    }

    @Override
    public Usuario listarId(int idUsuario) {return uR.findById(idUsuario).orElse(new Usuario());}

    @Override
    public void delete(int idUsuario) {
        Usuario usuario = uR.findById(idUsuario).orElse(null);
        if (usuario != null) {
            if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                throw new IllegalStateException("No se puede eliminar el usuario porque tiene roles asociados. Elimine primero los roles.");
            }
            uR.deleteById(idUsuario);
        }
    }

    @Override
    public void update(Usuario u) {
        Usuario usuarioExistente = uR.findById(u.getIdUsuario()).orElse(null);
        if (usuarioExistente != null) {
            // Preservar los roles existentes
            u.setRoles(usuarioExistente.getRoles());

            // Codificar nuevamente la contraseña
            u.setPassword(passwordEncoder.encode(u.getPassword()));

            uR.save(u);
        }
    }

}