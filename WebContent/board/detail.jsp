<!-- View 의 역할은 단순하게 사용자에게 보여주기 위함으로 위와같은 코드는 분리시켜줘야한다. -->
<!-- 해당 페이지로 직집URL(자원에 직접 파일.확장자) 접근을 하게 되면 또 파일 내부에서 세션체크를 해야함 -->
<!-- 필터로 .jsp로 접근하는 모든 접근을 막아버리면 된다 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
<!--
	<form action="/blog/board?cmd=delete" method="POST">
		<input type="text" name="id" value="${dto.id}"/>
		<button>삭제</button>
	</form> 얘 안씀 - DELETE요청:삭제 FORM태그로 할 수 없음  삭제할 때는 항상 -->
	<!-- POST,GET,DELETE,PUT => POST,GET-->
	<!-- 인증 + 권한이 필요함 -->
	<c:if test="${sessionScope.principal.id == dto.userId}">
		<a href="/blog/board?cmd=updateForm&id=${dto.id}" class="btn btn-warning" >수정</a>
		<button class="btn btn-danger" onClick="deleteById(${dto.id})">삭제</button>
	</c:if>
	
	<script>
		function deleteById(boardId){
			//요청과 응답 을 json
			var data = {
					boardId: boardId
			}
			
			$.ajax({
				type: "post",
				url: "/blog/board?cmd=delete",
				data: JSON.stringify(data),
				contentType: "application/json; charset=utf-8",
				dataType:"json"
			}).done(function(result){
				console.log(result);
				if(result.status =="ok"){
					location.href="index.jsp";
				}else{
					alert("삭제에 실패하였습니다.");
				}
			});
		}
	</script>

	<br />
	<br />
	<h6 class="m-2">
		작성자 : <i>${dto.username}</i> 조회수 : <i>${dto.readCount}</i>
	</h6>
	<br />
	<h3 class="m-2">
		<b>${dto.title}</b>
	</h3>
	<hr />
	<div class="form-group">
		<div class="m-2">${dto.content}</div>
	</div>

	<hr />
	
	<!-- 댓글 박스 -->
	<div class="row bootstrap snippets">
		<div class="col-md-12">
			<div class="comment-wrapper">
				<div class="panel panel-info">
					<div class="panel-heading m-2"><b>Comment</b></div>
					<div class="panel-body">
						<textarea id="reply__write__form" class="form-control" placeholder="write a comment..." rows="2"></textarea>
						<br>
						<button onclick="#" class="btn btn-primary pull-right">댓글쓰기</button>
						<div class="clearfix"></div>
						<hr />
						
						<!-- 댓글 리스트 시작-->
						<ul id="reply__list" class="media-list">
						
								<!-- 댓글 아이템 -->
								<li id="reply-1" class="media">		
									<div class="media-body">
										<strong class="text-primary">홍길동</strong>
										<p>
											댓글입니다.
										</p>
									</div>
									<div class="m-2">
		
										<i onclick="#" class="material-icons">delete</i>

									</div>
								</li>
							
						</ul>
						<!-- 댓글 리스트 끝-->
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- 댓글 박스 끝 -->
</div>

</body>
</html>

    