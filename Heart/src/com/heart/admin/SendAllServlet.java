package com.heart.admin;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heart.bean.HeartNews;
import com.heart.dao.NewsDao;
import com.heart.util.CONSTANTS;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

@WebServlet(name="sendAllServlet", urlPatterns = {"/sendAllAction"})
public class SendAllServlet extends HttpServlet {

	private static final long serialVersionUID = 4855724900289706773L;

	public static final String FANTAG = "Fantag";
	public static final String CONTENT = "content";
	
	public static final String DIAN = ".";
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		SmartUpload smartUpload = new SmartUpload();
		smartUpload.initialize(this.getServletConfig(), request, response);
		smartUpload.setDenyPhysicalPath(true);
		smartUpload.setMaxFileSize(1024 * 1024 * 2);// 设置允许上传文件最大为2M
		

	  //  int tag = 2;
		String content = "";
		
		HeartNews heartNews = new HeartNews();
		
		try {
			smartUpload.upload();

			
			Request suReq = smartUpload.getRequest();
			
			
		//	tag = Integer.valueOf(suReq.getParameter(FANTAG));
			content = suReq.getParameter(CONTENT);
			System.out.println(content);
			heartNews.setNewsContent(content);
			
			String newsTitle = suReq.getParameter("newstitle");
			heartNews.setTitle(newsTitle);
		
			heartNews.setNewsAbstract(suReq.getParameter("abstract"));
			
			for(int i = 0; i < smartUpload.getFiles().getCount();i++){
				
				com.jspsmart.upload.File file =
						smartUpload.getFiles().getFile(i);
				
				if(file.isMissing())
				{
					continue;
				}
				
				String filePath = "/heartUpload/";
				
				filePath += UUID.randomUUID().toString() + DIAN 
						    + file.getFileExt();
				
			
			    file.saveAs(filePath, SmartUpload.SAVE_VIRTUAL);
			    
			    heartNews.setNewsImg(filePath);
			    
			    break;
			    
	
			}
			
		} catch (NumberFormatException | SmartUploadException e) {
			
			e.printStackTrace();
		}
		
		
		// 写入数据库
		
	    new NewsDao().saveNews(heartNews);	
		
		
		/*String[] tags = null;

		switch(tag){
		
		  case 1:
			
			  tags = new String[]{CONSTANTS.OLDMAN,CONSTANTS.GUARDIAN};
			  break;
		  case 2:
			  tags = new String[]{CONSTANTS.GUARDIAN};
			  break;
		  case 3:
			  tags = new String[]{CONSTANTS.OLDMAN};
			  break;			  
		}
		
		
		new AdminService().sendAll(content, tags);
		*/
		//response.sendRedirect(CONSTANTS.ADMIN_HOME);
	    
	    response.sendRedirect("sendAll.jsp");
	    
	}

	
}
