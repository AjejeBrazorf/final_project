package com.javasampleapproach.security.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.javasampleapproach.security.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, String>{
	
	@Query(value = "SELECT * FROM message m  WHERE m.topic = ?1 ORDER BY (m.data) DESC LIMIT 10", nativeQuery = true)
	List<Message> findByTopic(String topic);
	
	@Query(value = "SELECT * FROM message m WHERE m.topic = ?3 ORDER BY (m.data) DESC LIMIT ?1 OFFSET ?2", nativeQuery = true)
	List<Message> findMessagesByOffset(int limit, int offset, String topic);
	
	@Query(value = "insert into message(username, message, data, topic)"
			+ " VALUES (?1, ?2, ?3, ?4)"
			+ " RETURNING id", nativeQuery = true)
	String insertMessage(String username, String message, Date data, String topic);
}
