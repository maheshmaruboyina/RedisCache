package com.redis.example.CacheApplication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
	@Autowired
	RedisUtil<String> redisUtil;
	
	@Autowired
	ConfigRepo configRepo;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@PostMapping("/postDataToCache")
	public String postDataToCache(@RequestBody Config config) {
		redisUtil.putMap("appConfig", config.getKey(), config.getValue());
		configRepo.save(config);
		return "posted successfully;";
	}
	
	@PostMapping("/updateData")
	public String updateData(@RequestBody Config config) {
		String Query = "update config set value = ? where key = ?";
		jdbcTemplate.update(Query,config.getKey(), config.getValue());
		redisUtil.putMap("appConfig", config.getKey(), config.getValue());
		return "posted success fully;";
	}
	
	@GetMapping("/getId/{name}")
	public Config getId(@PathVariable String name) {
		Config std = new Config();
		 if(redisUtil.getMapAsSingleEntry("appConfig", name).isEmpty()) {
			 try {
				 System.out.println("Fetching from database");
				 std = (configRepo.findById(name).isPresent())?configRepo.findById(name).get():null;
			 }catch (Exception e) {
				
			}
		 }else {
			 System.out.println("Fetching from redis Config");
			 std.setKey(name);
			 std.setValue(redisUtil.getMapAsSingleEntry("appConfig", name));
		 }
		 return std;
	}
	
	@GetMapping("/getAllConfig")
	public Map<String,String> getAllConfig() {
		Map<String,String> hm = new HashMap<String,String>();
		if(redisUtil.getMapAsAll("appConfig").isEmpty()) {
			System.out.println("Fetching from database");
			for(Config config : configRepo.findAll()) {
				redisUtil.putMap("appConfig", config.getKey(), config.getValue());
				hm.put(config.getKey(), config.getValue());
			}
		}else {
			System.out.println("Fetching from redis Config");
			hm = redisUtil.getMapAsAll("appConfig");
		}
		return hm;
	}
}
