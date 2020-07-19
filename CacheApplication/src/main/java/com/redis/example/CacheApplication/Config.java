package com.redis.example.CacheApplication;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "CONFIG")
public class Config implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6659917993585027255L;
	@Id
	@Column(name = "KEY")
	private String key;
	@Column(name = "VALUE")
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
