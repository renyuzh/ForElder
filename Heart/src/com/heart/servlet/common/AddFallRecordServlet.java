package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.bean.FallRecordBean;
import com.heart.dao.FallDao;


@WebServlet(name = "addFallRecordServlet", urlPatterns = {"/addFallRecord"})
public class AddFallRecordServlet extends HttpServlet {

	private static final long serialVersionUID = -5062752642087328798L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String oldmanPhone = request.getParameter("oldman_phone");

		long date = Long.valueOf(request.getParameter("date"));
		
	    FallRecordBean fallRecordBean =  new FallRecordBean();
	    fallRecordBean.setDate(date);
	    fallRecordBean.setOldmanPhone(oldmanPhone);
	    
	    new FallDao().saveFallRecord(fallRecordBean);
		
	}

	
}
