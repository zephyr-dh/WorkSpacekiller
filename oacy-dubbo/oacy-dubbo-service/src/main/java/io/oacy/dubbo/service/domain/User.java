package io.oacy.dubbo.service.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String username;
	private String password;
	private String mobile;
	private String email;
}
