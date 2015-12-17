package com.heart.servlet.guardian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.GuardianDao;

@WebServlet(name = "changePwdServlet", urlPatterns = {"/changePwd"})
public class ChangePwdServlet extends HttpServlet {

	private static final long serialVersionUID = 3448880198618216213L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("newPwd");
		
		try {
			new GuardianDao().changepwd(phone, pwd);
			System.out.println("has changed successfully...");
			response.getWriter().write("0");
			
		} catch (Exception e) {
			
			response.getWriter().write("1");
			
			e.printStackTrace();
		}
	}
	
	
	

}
