package com.example.MainProject.services;

import com.example.MainProject.models.Book;
import com.example.MainProject.models.Person;
import com.example.MainProject.models.Transaction;
import com.example.MainProject.repositories.BooksRepository;
import com.example.MainProject.repositories.PeopleRepository;
import com.example.MainProject.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final BooksRepository booksRepository;

    private final PeopleRepository peopleRepository;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository, BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.transactionsRepository = transactionsRepository;
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Transaction> findAll() {
        return transactionsRepository.findAll();
    }

    public Transaction findOne(int id) {
        Optional<Transaction> foundTransaction = transactionsRepository.findById(id);
        return foundTransaction.orElse(null);
    }

    @Transactional
    public void save(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        transactionsRepository.save(transaction);
    }

    @Transactional
    public void saveFromPerson(Transaction transaction, int id, String username) {
        Book book = booksRepository.findById(id).orElse(null);
        Objects.requireNonNull(book).setTook(book.getTook() + 1);
        transaction.setBook(book);
        transaction.setPerson(peopleRepository.findByUsername(username).get());
        transaction.setTransactionDate(LocalDateTime.now());
        transactionsRepository.save(transaction);
    }

    @Transactional
    public void update(Transaction updateTransaction, int id) {
        updateTransaction.setTransactionId(id);
        transactionsRepository.save(updateTransaction);
    }

    @Transactional
    public void delete(int id) {
        transactionsRepository.deleteById(id);
    }
}
