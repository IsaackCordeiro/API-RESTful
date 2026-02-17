package repository;

import jakarta.persistence.TypedQuery;
import models.PagedResult;
import models.Usuario;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    private final JPAApi jpaApi;

    @Inject
    public UsuarioRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    /**
     * 1. LISTAR (Com Paginação e Busca)
     */
    public PagedResult<Usuario> findList(int page, int pageSize, String termo) {
        return jpaApi.withTransaction(em -> {
            String hql = "FROM Usuario u WHERE 1=1";
            if (termo != null && !termo.trim().isEmpty()) {
                hql += " AND (lower(u.nome) LIKE :termo OR lower(u.email) LIKE :termo OR lower(u.role) LIKE :termo)";
            }
            hql += " ORDER BY u.id DESC";

            // Query de Dados
            TypedQuery<Usuario> query = em.createQuery(hql, Usuario.class);
            if (termo != null && !termo.trim().isEmpty()) {
                query.setParameter("termo", "%" + termo.toLowerCase() + "%");
            }

            // Paginação
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Usuario> data = query.getResultList();

            String countHql = "SELECT count(u) FROM Usuario u WHERE 1=1";
            if (termo != null && !termo.trim().isEmpty()) {
                countHql += " AND (lower(u.nome) LIKE :termo OR lower(u.email) LIKE :termo OR lower(u.role) LIKE :termo)";
            }
            TypedQuery<Long> countQuery = em.createQuery(countHql, Long.class);
            if (termo != null && !termo.trim().isEmpty()) {
                countQuery.setParameter("termo", "%" + termo.toLowerCase() + "%");
            }
            Long total = countQuery.getSingleResult();

            return new PagedResult<>(data, total, page, pageSize);
        });
    }

    /**
     * 2. BUSCAR POR ID
     */
    public Usuario findById(Long id) {
        return jpaApi.withTransaction(em -> {
            return em.find(Usuario.class, id);
        });
    }

    /**
     * 3. CRIAR (Create)
     */

    public Usuario create(Usuario usuario) {
        return jpaApi.withTransaction(em -> {
            usuario.setId(null);
            em.persist(usuario);
            return usuario;
        });
    }


    /**
     * 4. ATUALIZAR (Update)
     */
    public Usuario update(Usuario usuario) {
        return jpaApi.withTransaction(em -> {
            return em.merge(usuario);
        });
    }

    /**
     * 5. DELETAR (Delete)
     */
    public boolean delete(Long id) {
        return jpaApi.withTransaction(em -> {
            Usuario u = em.find(Usuario.class, id);
            if (u != null) {
                em.remove(u);
                return true;
            }
            return false;
        });
    }

    /**
     * 2. BUSCAR POR EMAIL
     */
    public Optional<Usuario> findByEmail(String email) {
        return jpaApi.withTransaction(em -> {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :pEmail";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("pEmail", email);

            return query.getResultStream().findFirst();
        });
    }

}