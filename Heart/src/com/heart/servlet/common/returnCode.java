package com.heart.servlet.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.bean.CodeResult;
import com.heart.dao.OldManDao;
import com.heart.util.GenerateCode;
import com.heart.util.SendCodeMsg;

import net.sf.json.JSONObject;

@WebServlet(name = "returnCodeServlet", urlPatterns = { "/returnCode" })
public class returnCode extends HttpServlet {

	private static final long serialVersionUID = 601680363654895503L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		System.out.println("run return code..");
		String phone = request.getParameter("phone");
		
		CodeResult codeResult = new CodeResult();
		
		
		if(new OldManDao().hasSamePhone(phone))
		{
			codeResult.setValidValue(1);
			codeResult.setInfo("");
			
		}
		else {
			codeResult.setValidValue(2);
			String code = GenerateCode.involk();
			codeResult.setInfo(code);
			
			String sendResult = SendCodeMsg.sendMsg(phone, code);
			
			if(!sendResult.startsWith("100"))
			{
				codeResult.setValidValue(3);
				codeResult.setInfo("");
			}
			
		}
		JSONObject object = JSONObject.fromObject(codeResult);
		
		response.getWriter().write(object.toString());
		
	}

	
}
