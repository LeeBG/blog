package com.cos.blog.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class Script {
	

	public static void back(HttpServletResponse response,String msg) throws ServletException, IOException {
		PrintWriter out;
		
		out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('"+msg+"');");
		out.println("history.go(-1);");//history.back()과 같음
		out.println("</script>");
		out.flush();	//버퍼 비우기
	}

}
