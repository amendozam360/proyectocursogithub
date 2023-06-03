package com.cursojava.curso.dao;

import com.cursojava.curso.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional

public class UsuarioDaoImp implements UsuarioDao{

    @PersistenceContext
    EntityManager entitymanager;

    @Override
    @Transactional
    public List<Usuario> getUsuarios() {
        String query="FROM Usuario";
        return entitymanager.createQuery(query).getResultList();

    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario= entitymanager.find(Usuario.class, id);
        entitymanager.remove(usuario);

    }
    @Override
    public void registrar(Usuario usuario) {
        entitymanager.merge(usuario);
    }
    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query="FROM Usuario WHERE email = :email";

      List<Usuario> lista = entitymanager.createQuery(query)

        .setParameter("email", usuario.getEmail())
                .getResultList();

      if(lista.isEmpty()){
          return null;
      }
      String passwordHashed = lista.get(0).getPassword();

        Argon2 argon2= Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
 if (argon2.verify(passwordHashed, usuario.getPassword())){
     return lista.get(0);
 }
return  null;
    }

}
