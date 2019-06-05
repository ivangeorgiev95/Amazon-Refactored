package com.amazon.util;

import com.amazon.domain.User;
import com.amazon.exceptions.EmptyBasketException;
import com.amazon.exceptions.NotAdminException;
import com.amazon.exceptions.NotLoggedInException;
import com.amazon.exceptions.UserException;

import javax.servlet.http.HttpServletRequest;

public class UserValidator {

	private static final String USER_ATTRIBUTE = "user";

	public static void validateLogIn(HttpServletRequest request) throws NotLoggedInException {
		if(request.getSession().getAttribute(USER_ATTRIBUTE) == null) {
			throw new NotLoggedInException("Please log in!");
		}
	}
	
	public static void validateAdminUser(HttpServletRequest request) throws NotLoggedInException, NotAdminException {
		UserValidator.validateLogIn(request);
		if(!((User)request.getSession().getAttribute(USER_ATTRIBUTE)).isAdmin()) {
			throw new NotAdminException("You are not admin!");
		}
	}
	
	public static void validateUserPassword(String password, String reEnteredPassword) throws UserException {
		if(!password.equals(reEnteredPassword)) {
			throw new UserException("Invalid re-entered password !!!");
		}
	}
	
}
