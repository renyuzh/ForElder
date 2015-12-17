package com.heart.servlet.common;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.heart.bean.RegularPosition;
import com.heart.dao.RegularPositionDao;

@WebServlet(name = "fetchMainPositionServlet", urlPatterns = {"/fetchMainPosition"})
public class FetchMainPositionServlet extends HttpServlet {

	private static final long serialVersionUID = -402362466599557723L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		
		System.out.println("run fetchmainposition...");
		String[] phones = request.getParameterValues("oldmanPhone");
		

		try {
			
			ArrayList<RegularPosition> regularPositions =
					 new RegularPositionDao().getBindOldman20Position(phones);
			
			
			response.setContentType("text/xml;charset=UTF-8");  
			response.setHeader("Cache-Control", "no-cache");
				System.out.println(JSONArray.fromObject(regularPositions).toString());
			response.getWriter().write(JSONArray.fromObject(regularPositions).toString());
		} catch (Exception e) {
			
			System.out.println("error in get main position");
			
			response.getWriter().write("err");
			
		}
		
		
		
		
		
	}

	
}
