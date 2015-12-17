package com.heart.servlet.guardian;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.heart.service.GuardianService;

@WebServlet(name = "guardianRegisterServlet", urlPatterns = { "/guardianRegister" })
public class RegisterServlet extends HttpServlet {

	
	private static final long serialVersionUID = -8502452428888801329L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        PrintWriter writer = response.getWriter();
		
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		
		if(new GuardianService().register(phone, pwd, name))
		{
			writer.write("ok");
		}
		else {
			writer.write("err");
		}
		
		writer.close();
	}

	
}
