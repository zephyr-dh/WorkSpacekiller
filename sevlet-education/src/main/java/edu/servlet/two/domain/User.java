package edu.servlet.two.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
	private int id;
	private String uname;
	private String pwd;
	private String phone;
}
