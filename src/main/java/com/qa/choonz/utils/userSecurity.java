package com.qa.choonz.utils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class userSecurity {

	public static @NotNull @Size(max = 100) String getSalt() {
		// TODO Auto-generated method stub
		return null;
	}

	public static @NotNull @Size(max = 100) String encrypt(@NotNull @Size(max = 100) String saltedPassword,
			@NotNull @Size(max = 100) String salt) {
		// TODO Auto-generated method stub
		return null;
	}


}
