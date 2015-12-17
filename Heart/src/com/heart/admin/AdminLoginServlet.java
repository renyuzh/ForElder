package com.heart.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.util.CONSTANTS;


@WebServlet(name = "adminLoinServlet", urlPatterns = {"/adminLogin"})
public class AdminLoginServlet extends HttpServlet {

	private static final long serialVersionUID = -2519921798786443111L;

	private static final String PWD = "pwd";
	private static final String NAME = "name";
	
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String name = request.getParameter(NAME);
		String pwd = request.getParameter(PWD);
		
		System.out.println(name + ": " + pwd);
		if(new AdminDao().adminLogin(pwd, name))
		{
			request.getSession().setAttribute(CONSTANTS.SESSION_ADMIN_LOGIN, true);
		
			response.sendRedirect(CONSTANTS.ADMIN_HOME);
		}
		else {
			request.getSession().setAttribute(CONSTANTS.SESSION_ADMIN_LOGIN, false);
			response.sendRedirect(CONSTANTS.ADMIN_LOGIN_PAGE);
		}
	}

	
}
