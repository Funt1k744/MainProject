package com.example.MainProject.services;

import com.example.MainProject.models.Person;
import com.example.MainProject.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Person findByUsername(String username) {
        Optional<Person> foundPerson = peopleRepository.findByUsername(username);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setRole("ROLE_USER");
        person.setDateOfCreation(LocalDateTime.now());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatePerson, int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        updatePerson.setDateOfCreation(Objects.requireNonNull(person).getDateOfCreation());
        updatePerson.setUsername(person.getUsername());
        updatePerson.setPassword(person.getPassword());
        updatePerson.setPersonId(id);
        if (updatePerson.getDateOfBirth() == null)
            updatePerson.setDateOfBirth(person.getDateOfBirth());
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}