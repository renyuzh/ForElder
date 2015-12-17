package com.heart.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.heart.util.CONSTANTS;

@WebServlet(name = "adminHomeServlet", urlPatterns = {"/adminHome"})
public class AdminHomeServlet extends HttpServlet {

	private static final long serialVersionUID = -6905135793486660874L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		if(request.getSession().getAttribute(CONSTANTS.SESSION_ADMIN_LOGIN) == null
				 || !(boolean)(request.getSession().getAttribute(CONSTANTS.SESSION_ADMIN_LOGIN)))
		{
			
			response.sendRedirect(CONSTANTS.ADMIN_LOGIN_PAGE);
			return;
		}
		
		
		HttpSession session = request.getSession();
		AdminService adminService = new AdminService();
		
		session.setAttribute(CONSTANTS.TODAY_MSG, adminService.getTodayMsg());
		session.setAttribute(CONSTANTS.TODAY_FAN, adminService.getTodayFan());
		session.setAttribute(CONSTANTS.ALL_FAN, adminService.getAllFan());
		RequestDispatcher dispatcher = request.getRequestDispatcher(CONSTANTS.ADMIN_MANAGE_PAGE);
		
		dispatcher.forward(request, response);
		
	}

	
}
