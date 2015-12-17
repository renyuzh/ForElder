package com.heart.servlet.oldman;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.OldManDao;

@WebServlet(name = "oldManLoginServlet", urlPatterns = { "/oldManLogin" })
public class Login extends HttpServlet {

	
	private static final long serialVersionUID = -9199589423266427507L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String pwd = request.getParameter("pwd");
		String phone = request.getParameter("phone");
		
		if(new OldManDao().login(phone, pwd))
		{
			response.getWriter().write("ok");
		}
		else {
			response.getWriter().write("err");
		}
		
	}

	
}
