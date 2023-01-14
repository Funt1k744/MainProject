package com.example.MainProject.services;

import com.example.MainProject.models.Person;
import com.example.MainProject.repositories.PeopleRepository;
import com.example.MainProject.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService{
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Person> person = peopleRepository.findByUsername(username);
       if (person.isEmpty()) {
           throw new UsernameNotFoundException("Netu");
       }

       return new PersonDetails(person.get());
    }
}
