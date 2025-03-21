package com.fausto.users.users.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fausto.users.users.entities.UserData;
import com.fausto.users.users.repositories.UsersRepository;
import com.fausto.users.users.services.Kafka.KafkaProducerService;

import jakarta.validation.Valid;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepository;

	@Autowired 
	KafkaProducerService kafkaProducer;

    public ResponseEntity<UserData> setUserDetails(Integer userId){
        Optional<UserData> optionalUser = usersRepository.findById(userId);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
		UserData user = optionalUser.get();
		kafkaProducer.sendMessage(user);
        return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
    }

    public UserData setUserDetails(@Valid @RequestBody UserData user){
		kafkaProducer.sendMessage(user);
        return usersRepository.save(user);
    }

    public ResponseEntity<UserData> updateUser(@Valid @RequestBody UserData user){
        Optional<UserData> oldUser = usersRepository.findById(user.getId());
        if(oldUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        usersRepository.save(user);
		kafkaProducer.sendMessage(user);
        return ResponseEntity.status(HttpStatus.OK).body(oldUser.get());
    }

}
