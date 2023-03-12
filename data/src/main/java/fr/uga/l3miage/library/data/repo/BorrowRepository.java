package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Borrow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class BorrowRepository implements CRUDRepository<String, Borrow> {

    private final EntityManager entityManager;

    @Autowired
    public BorrowRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Borrow save(Borrow entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Borrow get(String id) {
        return entityManager.find(Borrow.class, id);
    }

    @Override
    public void delete(Borrow entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<Borrow> all() {
        return entityManager.createQuery("from Borrow", Borrow.class).getResultList();
    }

    /**
     * Trouver des emprunts en cours pour un emprunteur donné
     *
     * @param userId l'id de l'emprunteur
     * @return la liste des emprunts en cours
     */
    public List<Borrow> findInProgressByUser(String userId) {
        TypedQuery<Borrow> query = entityManager.createQuery( "SELECT b FROM Borrow b WHERE b.borrower.id = :userId AND b.finished = false", Borrow.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Compte le nombre total de livres emprunté par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countBorrowedBooksByUser(String userId) {
        int nbBorrow = entityManager.createQuery("SELECT COUNT(bb) FROM Borrow b JOIN b.borrower bu JOIN b.books bb WHERE bu.id = :userId ",
                                                 Long.class)
                                                 .setParameter("userId", userId)
                                                 .getSingleResult()
                                                 .intValue();
        return nbBorrow;
    }

    /**
     * Compte le nombre total de livres non rendu par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countCurrentBorrowedBooksByUser(String userId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(DISTINCT book.id) FROM Borrow borrow JOIN borrow.books book WHERE borrow.borrower.id = :userId AND borrow.finished = false", 
            Long.class
            );
        query.setParameter("userId", userId);
        Long result = query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    /**
     * Recherche tous les emprunt en retard trié
     *
     * @return la liste des emprunt en retard
     */
    public List<Borrow> foundAllLateBorrow() {
        return entityManager.createQuery(
                "SELECT b FROM Borrow b WHERE b.requestedReturn < :now ORDER BY b.requestedReturn ASC",
                Borrow.class)
                .setParameter("now", new Date())
                .getResultList();
    }

    /**
     * Calcul les emprunts qui seront en retard entre maintenant et x jours.
     *
     * @param days le nombre de jour avant que l'emprunt soit en retard
     * @return les emprunt qui sont bientôt en retard
     */
    public List<Borrow> findAllBorrowThatWillLateWithin(int days) {
        Date currentDate = new Date();
        Date renduDate = new Date(currentDate.getTime() + days * 24 * 60 * 60 * 1000);
        List<Borrow> borrows = entityManager.createQuery("SELECT b FROM Borrow b WHERE b.finished = false AND b.requestedReturn <= :renduDate", Borrow.class)
            .setParameter("renduDate", new java.sql.Date(renduDate.getTime()), TemporalType.DATE)
            .getResultList();
        return borrows;
    }
}
