package fr.uga.l3miage.library.data.repo;

import com.github.javafaker.Faker;
import fr.uga.l3miage.library.data.domain.*;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Fixtures {

    private static final Faker FAKER = Faker.instance(new Random(42));

    public static Author newAuthor() {
        Author author = new Author();
        author.setFullName(FAKER.name().fullName());
        return author;
    }

    public static Book newBook() {
        Book book = new Book();
        book.setPublisher(FAKER.book().publisher());
        book.setTitle(FAKER.book().title());
        book.setIsbn(Long.parseLong(FAKER.number().digits(13)));
        book.setLanguage(Book.Language.ENGLISH);
        book.setYear(Short.parseShort(FAKER.number().digits(4)));
        return book;
    }

    public static Librarian newLibrarian() {
        Librarian librarian = new Librarian();
        setPersonProps(librarian);
        return librarian;
    }

    public static User newUser() {
        User user = new User();
        setPersonProps(user);
        user.setRegistered(FAKER.date().past(5*365, TimeUnit.DAYS));
        user.setLateRatio((float) FAKER.number().randomDouble(2,0,1));
        return user;
    }

    public static Borrow newBorrow(User user, Librarian librarian, Book... books) {
        Borrow borrow = new Borrow();
        borrow.setBorrower(user);
        borrow.setLibrarian(librarian);
        borrow.setBooks(List.of(books));
        borrow.setStart(FAKER.date().past(3 * 30, TimeUnit.DAYS));
        borrow.setRequestedReturn(Date.from(borrow.getStart().toInstant().plus(30, ChronoUnit.DAYS)));
        return borrow;
    }

    private static void setPersonProps(Person person) {
        person.setGender(Person.Gender.values()[FAKER.number().numberBetween(0, 2)]);
        person.setFirstName(FAKER.name().firstName());
        person.setLastName(FAKER.name().lastName());
        person.setBirth(FAKER.date().birthday(20, 60));
    }

}
