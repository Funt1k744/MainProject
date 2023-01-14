package com.example.MainProject.services;

import com.example.MainProject.models.Book;
import com.example.MainProject.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        book.setTook(0);
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book updateBook, int id) {
        Book book = booksRepository.findById(id).orElse(null);
        updateBook.setBookId(id);
        updateBook.setTook(Objects.requireNonNull(book).getTook());
        if (updateBook.getReleaseDate() == null)
            updateBook.setReleaseDate(Objects.requireNonNull(book).getReleaseDate());
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public boolean takenTest(int id) {
        return !Objects.requireNonNull(booksRepository.findById(id).orElse(null)).getTransactionList().isEmpty();
    }
}
