package com.redis.example.CacheApplication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepo extends CrudRepository<Config, String>{

}
