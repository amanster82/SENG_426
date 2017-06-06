package com.acme.service.dto;

import com.acme.domain.ACMEPass;
import com.acme.domain.User;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ACMEPassDTO {

	private Long id;

	@NotNull
	@Size(min = 3)
	private String site;

	@NotNull
	private String login;

	@NotNull
	private String password;

	private ZonedDateTime createdDate;

	private ZonedDateTime lastModifiedDate;

	private User user;

	public ACMEPassDTO() {
	}

	public ACMEPassDTO(ACMEPass entity) {
		assert entity != null;

		this.id = entity.getId();
		this.site = entity.getSite();
		this.login = entity.getLogin();
		this.password = entity.getPassword();
		this.createdDate = entity.getCreatedDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
		this.user = entity.getUser();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}
	
	public User getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
