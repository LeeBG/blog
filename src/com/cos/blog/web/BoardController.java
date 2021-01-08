package com.cos.blog.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.domain.board.Board;
import com.cos.blog.domain.board.dto.SaveReqDto;
import com.cos.blog.domain.user.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.util.Script;

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
		}
	}

}
