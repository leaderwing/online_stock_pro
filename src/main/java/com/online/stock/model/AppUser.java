package com.online.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Model class for application user
 *
 * @author Hendi Santika
 *
 */
@Data
@Entity
@Table(name = "AFMAST")
public class AppUser implements UserDetails {
	@Id
	@Column(name = "CUSTID")
	private String custId;
	@Column(name = "ACCTNO", unique = true)
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "PIN")
	private String password;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "ISSTAFT")
	private int isStaft;

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		List<String> roles = new ArrayList<>();
		if(isStaft != 3) {
			roles.add("ROLE_USER");
		} else {
			roles.add("ROLE_ADMIN");
		}
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
