package repository;

import models.Categoria;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CategoriaRepository {

    private final JPAApi jpa;

    @Inject
    public CategoriaRepository(JPAApi jpa) {
        this.jpa = jpa;
    }

    public List<Categoria> findAll() {
        return jpa.withTransaction(entityManager -> {
            TypedQuery<Categoria> query = entityManager
                    .createQuery("SELECT c FROM Categoria c", Categoria.class);
            return query.getResultList();
        });
    }

    public Categoria findById(Long id) {
        return jpa.withTransaction(entityManager -> {
            return entityManager.find(Categoria.class, id);
        });
    }

    public List<Categoria> findWithFilter(String descricao, int page, int size) {
        return jpa.withTransaction(entityManager -> {

            String jpql = "SELECT c FROM Categoria c WHERE 1=1";

            if (descricao != null && !descricao.isBlank()) {
                jpql += " AND LOWER(c.descricao) LIKE LOWER(:descricao)";
            }

            TypedQuery<Categoria> query = entityManager.createQuery(jpql, Categoria.class);

            if (descricao != null && !descricao.isBlank()) {
                query.setParameter("descricao", "%" + descricao + "%");
            }

            query.setFirstResult(page * size);
            query.setMaxResults(size);

            return query.getResultList();
        });
    }

    public long countWithFilter(String descricao) {
        return jpa.withTransaction(entityManager -> {

            String jpql = "SELECT COUNT(c) FROM Categoria c WHERE 1=1";

            if (descricao != null && !descricao.isBlank()) {
                jpql += " AND LOWER(c.descricao) LIKE LOWER(:descricao)";
            }

            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

            if (descricao != null && !descricao.isBlank()) {
                query.setParameter("descricao", "%" + descricao + "%");
            }

            return query.getSingleResult();
        });
    }


    public Categoria create(Categoria categoria) {
        return jpa.withTransaction(entityManager -> {
            entityManager.persist(categoria);
            return categoria;
        });
    }

    public Categoria update(Categoria categoria) {
        return jpa.withTransaction(entityManager -> {
            return entityManager.merge(categoria);
        });
    }

    public void delete(Long id) {
        jpa.withTransaction(entityManager -> {
            Categoria categoria = entityManager.find(Categoria.class, id);
            if (categoria != null) {
                entityManager.remove(categoria);
            }
            return null;
        });
    }
}
