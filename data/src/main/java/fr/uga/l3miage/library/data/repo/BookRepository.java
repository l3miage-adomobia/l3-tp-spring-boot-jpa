package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository implements CRUDRepository<Long, Book> {

    private final EntityManager entityManager;

    @Autowired
    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Book save(Book author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Book get(Long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public void delete(Book author) {
        entityManager.remove(author);
    }

    /**
     * Renvoie tous les auteurs par ordre alphabétique
     * 
     * @return une liste de livres
     */
    public List<Book> all() {
        String requeteS = "SELECT b FROM Book b ORDER BY b.title";
        TypedQuery<Book> requete = this.entityManager.createQuery(requeteS, Book.class);
        List<Book> orderedBooks = requete.getResultList();
        return orderedBooks;
    }

    /**
     * Trouve les livres dont le titre contient la chaine passée (non sensible à la
     * casse)
     * 
     * @param titlePart tout ou partie du titre
     * @return une liste de livres
     */
    public List<Book> findByContainingTitle(String titlePart) {
        String queryString = "SELECT b FROM Book b WHERE LOWER(b.title) LIKE :titlePart ORDER BY b.title";
        TypedQuery<Book> query = this.entityManager.createQuery(queryString, Book.class);
        query.setParameter("titlePart", '%' + titlePart.toLowerCase() + '%');
        List<Book> matchingBooks = query.getResultList();
        return matchingBooks;
    }

    /**
     * Trouve les livres d'un auteur donnée dont le titre contient la chaine passée
     * (non sensible à la casse)
     * 
     * @param authorId  id de l'auteur
     * @param titlePart tout ou partie d'un titre de livré
     * @return une liste de livres
     */
    public List<Book> findByAuthorIdAndContainingTitle(Long authorId, String titlePart) {
        String queryString = "SELECT b FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE :titlePart AND a.id = :authorId ORDER BY b.title";
        TypedQuery<Book> query = this.entityManager.createQuery(queryString, Book.class);
        query.setParameter("titlePart", '%' + titlePart.toLowerCase() + '%');
        query.setParameter("authorId", authorId);
        List<Book> matchingBooks = query.getResultList();
        return matchingBooks;
    }

    /**
     * Recherche des livres dont le nom de l'auteur contient la chaine passée (non
     * sensible à la casse)
     * 
     * @param namePart tout ou partie du nom
     * @return une liste de livres
     */
    public List<Book> findBooksByAuthorContainingName(String namePart) {
        String queryString = "SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.fullName) LIKE :authorName ORDER BY b.title";
        TypedQuery<Book> query = entityManager.createQuery(queryString, Book.class);
        query.setParameter("authorName", '%' + namePart.toLowerCase() + '%');
        List<Book> books = query.getResultList();
        return books;

    }

    /**
     * Trouve des livres avec un nombre d'auteurs supérieur au compte donné
     * 
     * @param count le compte minimum d'auteurs
     * @return une liste de livres
     */
    public List<Book> findBooksHavingAuthorCountGreaterThan(int count) {
        String queryString = "SELECT b FROM Book b WHERE SIZE(b.authors) > :count ORDER BY b.title";
        TypedQuery<Book> query = entityManager.createQuery(queryString, Book.class);
        query.setParameter("count", count);
        List<Book> books = query.getResultList();
        return books;
    }

}
