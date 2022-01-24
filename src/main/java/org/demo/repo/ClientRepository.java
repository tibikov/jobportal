package org.demo.repo;

import org.demo.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

	Client findByEmail(String email);

	Client findByApiKey(String apiKey);

}
