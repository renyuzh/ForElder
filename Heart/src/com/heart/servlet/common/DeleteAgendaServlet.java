package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.dao.AgendaDao;

@WebServlet(name = "deleteAgendaServlet", urlPatterns = {"/deleteAgenda"})
public class DeleteAgendaServlet extends HttpServlet {


	private static final long serialVersionUID = 2690279133855301172L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String oldmanphone = request.getParameter("oldmanphone");
		int clientMsgID = Integer.valueOf(request.getParameter("clientMsgID"));
		String msgID = request.getParameter("_id");
		
		new AgendaDao().deleteAgenda(msgID, oldmanphone, clientMsgID);
		
		response.getWriter().write("1");
	}

	
}
