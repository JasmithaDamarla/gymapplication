package com.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gym.model.EmailNoti;

public interface EmailNotificationRepository extends MongoRepository<EmailNoti, String>{

}
