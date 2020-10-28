package com.mycompany.myapp.repository;

import com.mycompany.myapp.GeneratedByJHipster;
import com.mycompany.myapp.domain.Authority;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
@GeneratedByJHipster
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {}
