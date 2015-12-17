package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.bean.RegularPosition;
import com.heart.dao.OldManDao;
import com.heart.dao.RegularPositionDao;
import com.heart.service.RegularPositionService;

@WebServlet(name = "addPositionRecordServlet", urlPatterns = {"/addPositionRecord"})
public class AddPositionRecordServlet extends HttpServlet {

	private static final long serialVersionUID = -6896954242081256283L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String oldmanPhone = request.getParameter("oldmanPhone");
		
		String oldmanPwd = request.getParameter("oldmanPwd");
		
		double longtitude = Double.valueOf(request.getParameter("longtitude"));
		
		double latitude = Double.valueOf(request.getParameter("latitude"));
		
		String area = request.getParameter("area");
		
		String string = new String(area.getBytes("ISO-8859-1"),"utf-8");
	 

		System.out.println(string);
		
		long date = Long.valueOf(request.getParameter("date"));
				
		if(new OldManDao().login(oldmanPhone, oldmanPwd))
		{
			RegularPosition regularPosition = new RegularPosition();
			
			regularPosition.setArea(string);
			regularPosition.setLatitude(latitude);
			regularPosition.setLongtitude(longtitude);
			regularPosition.setOldmanPhone(oldmanPhone);
			regularPosition.setDate(date);
			
			if(new RegularPositionService().getPositionWithWeather(regularPosition))
			{
				new RegularPositionDao().savePosition(regularPosition);
				System.out.println("upload position ok...");
			}
			
		}
		
	}

	
}
