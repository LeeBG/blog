package com.cos.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.dto.DeleteReqDto;
import com.cos.blog.domain.board.dto.DeleteRespDto;
import com.cos.blog.domain.board.dto.DetailRespDto;
import com.cos.blog.domain.board.dto.SaveReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.util.Script;
import com.google.gson.Gson;

// http://localhost:8000/blog/board
@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public BoardController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		BoardService boardService = new BoardService();
		// http://localhost:8000/blog/board?cmd=saveForm
		HttpSession session = request.getSession();
		if(cmd.equals("saveForm")) {
			//로그인 한 사람만 글쓸수 있도록 인증이 필요함
			User principal = (User)session.getAttribute("principal");
			if(principal != null) {
				RequestDispatcher dis =
						request.getRequestDispatcher("board/saveForm.jsp");
				dis.forward(request, response);
			}else {
				RequestDispatcher dis =
						request.getRequestDispatcher("user/loginForm.jsp");
				dis.forward(request, response);
			}
			
			//친절을 위해서는  Script
			//Board/saveForm.jsp를 주소로 바로접근할 경우 = 컨트롤러 안타고옴(강제로 url요청)
		}else if(cmd.equals("save")) {
			int userId = Integer.parseInt(request.getParameter("userId"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			SaveReqDto dto = new SaveReqDto();
			dto.setUserId(userId);
			dto.setTitle(title);
			dto.setContent(content);
			int result = boardService.글쓰기(dto);
			if(result == 1) {
				response.sendRedirect("index.jsp");
			}else {
				Script.back(response,"글쓰기 실패" );
			}
		}else if(cmd.equals("list")) {
			int page = Integer.parseInt(request.getParameter("page"));// 최초 : 0 next :1 next : 2
			List<Board> boards = boardService.글목록보기(page);
			request.setAttribute("boards", boards);
			//계산 (전체 데이터수 한 페이지 몇개 - 총 몇페이지가 나와야하는지 계산)3page라면
			//MAX값이 2
			//page==2가 되는 순간 isEnd=true
			//request.setAttribute("isEnd", true);
			int boardCount = (boardService).글개수();
			int lastPage = (boardCount-1)/4;
			double currentPosition =(double)page/(lastPage)*100;
			
			request.setAttribute("currentPosition",currentPosition);
			request.setAttribute("lastPage",lastPage);
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
		}else if(cmd.equals("detail")) {
			int id = Integer.parseInt(request.getParameter("id"));
			DetailRespDto dto = boardService.글상세보기(id);//board테이블+user테이블 조인된 데이터가 필요
			if(dto == null) {
				Script.back(response, "상세보기 실패");
			}else {
				request.setAttribute("dto", dto);
				RequestDispatcher dis = request.getRequestDispatcher("board/detail.jsp");
				dis.forward(request, response);
			}
			
		}else if(cmd.equals("delete")) {
			// 1. 요청 받은 json 데이터를 자바 오브젝트로 파싱
			BufferedReader br = request.getReader();
			String data = br.readLine();

			Gson gson = new Gson();
			DeleteReqDto dto = gson.fromJson(data, DeleteReqDto.class);		//자바스크립트객체를 자바객체로

			// 2. DB에서 id값으로 글 삭제
			int result = boardService.글삭제(dto.getBoardId());

			// 3. 응답할 json 데이터를 생성
			DeleteRespDto respDto = new DeleteRespDto();
			if(result == 1) {
				respDto.setStatus("ok");
			}else {
				respDto.setStatus("fail");
			}
			String respData = gson.toJson(respDto);
			System.out.println("respData : "+respData);
			PrintWriter out = response.getWriter();
			out.print(respData);
			out.flush();
		}
	}

}
