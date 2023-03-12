package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PersonRepositoryTest extends Base {

    @Autowired
    LibrarianRepository librarianRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void top3WorkingLibrarians() {

        Librarian l1 = Fixtures.newLibrarian();
        Librarian l2 = Fixtures.newLibrarian();
        Librarian l3 = Fixtures.newLibrarian();
        Librarian l4 = Fixtures.newLibrarian();
        Librarian l5 = Fixtures.newLibrarian();

        entityManager.persist(l1);
        entityManager.persist(l2);
        entityManager.persist(l3);
        entityManager.persist(l4);
        entityManager.persist(l5);

        User u1 = Fixtures.newUser();

        Book b1 = Fixtures.newBook();
        Author a1 = Fixtures.newAuthor();
        a1.addBook(b1);
        entityManager.persist(a1);
        entityManager.persist(u1);
        b1.addAuthor(a1);
        entityManager.persist(b1);

        Borrow br1 = Fixtures.newBorrow(u1, l1, b1);
        Borrow br2 = Fixtures.newBorrow(u1, l1, b1);
        Borrow br3 = Fixtures.newBorrow(u1, l1, b1);
        Borrow br4 = Fixtures.newBorrow(u1, l1, b1);
        Borrow br5 = Fixtures.newBorrow(u1, l1, b1);
        Borrow br6 = Fixtures.newBorrow(u1, l1, b1);
        entityManager.persist(br1);
        entityManager.persist(br2);
        entityManager.persist(br3);
        entityManager.persist(br4);
        entityManager.persist(br5);
        entityManager.persist(br6);

        Borrow br7 = Fixtures.newBorrow(u1, l2, b1);
        Borrow br8 = Fixtures.newBorrow(u1, l2, b1);
        Borrow br9 = Fixtures.newBorrow(u1, l2, b1);
        Borrow br10 = Fixtures.newBorrow(u1, l2, b1);
        Borrow br11 = Fixtures.newBorrow(u1, l2, b1);
        entityManager.persist(br7);
        entityManager.persist(br8);
        entityManager.persist(br9);
        entityManager.persist(br10);
        entityManager.persist(br11);

        Borrow br12 = Fixtures.newBorrow(u1, l3, b1);
        Borrow br13 = Fixtures.newBorrow(u1, l3, b1);
        Borrow br14 = Fixtures.newBorrow(u1, l3, b1);
        Borrow br15 = Fixtures.newBorrow(u1, l3, b1);
        entityManager.persist(br12);
        entityManager.persist(br13);
        entityManager.persist(br14);
        entityManager.persist(br15);

        Borrow br16 = Fixtures.newBorrow(u1, l4, b1);
        Borrow br17 = Fixtures.newBorrow(u1, l4, b1);
        entityManager.persist(br16);
        entityManager.persist(br17);

        Borrow br18 = Fixtures.newBorrow(u1, l5, b1);
        Borrow br19 = Fixtures.newBorrow(u1, l5, b1);
        entityManager.persist(br18);
        entityManager.persist(br19);

        entityManager.flush();

        List<Librarian> librarians = librarianRepository.top3WorkingLibrarians();
        assertThat(librarians)
                .hasSize(3)
                .extracting("firstName")
                .containsExactlyInAnyOrder(l1.getFirstName(), l2.getFirstName(), l3.getFirstName());

    }

    @Test
    void findAllOlderThan() {

        User u1 = Fixtures.newUser();
        u1.setBirth(Date.from(ZonedDateTime.now().minus(20, ChronoUnit.YEARS).toInstant()));
        User u2 = Fixtures.newUser();
        u2.setBirth(Date.from(ZonedDateTime.now().minus(40, ChronoUnit.YEARS).toInstant()));
        User u3 = Fixtures.newUser();
        u3.setBirth(Date.from(ZonedDateTime.now().minus(45, ChronoUnit.YEARS).toInstant()));

        entityManager.persist(u1);
        entityManager.persist(u2);
        entityManager.persist(u3);

        entityManager.flush();

        List<User> allOlderThan = userRepository.findAllOlderThan(30);
        assertThat(allOlderThan).containsExactlyInAnyOrder(u2, u3);

    }
}
