package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Librarian;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LibrarianRepository implements CRUDRepository<String, Librarian> {

    private final EntityManager entityManager;

    @Autowired
    public LibrarianRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Librarian save(Librarian entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Librarian get(String id) {
        return entityManager.find(Librarian.class, id);
    }

    @Override
    public void delete(Librarian entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<Librarian> all() {
        return entityManager.createQuery("from Librarian", Librarian.class).getResultList();
    }

    /**
     * Récupere les bibliothéquaires ayant enregistré le plus de prêts
     * @return les bibliothéquaires les plus actif
     */
    public List<Librarian> top3WorkingLibrarians() {
        List<Librarian> lib = entityManager.createQuery("SELECT bl FROM Borrow b JOIN b.librarian bl GROUP BY bl ORDER BY COUNT(b) DESC",
                                                        Librarian.class)
                                            .setFirstResult(0)
                                            .setMaxResults(3)
                                            .getResultList();
        return lib;
    }

}
