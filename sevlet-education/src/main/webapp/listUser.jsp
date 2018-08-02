<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ page import="edu.servlet.two.domain.*,java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<div id="wrap">
		<div id="top_content">
			<%@ include file="header.jsp"%>
			<div id="content">
				<p id="whereami"></p>
				<h1>用户列表</h1>
				<table class="table">
					<tr class="table_header">
						<td>ID</td>
						<td>用户名</td>
						<td>密码</td>
						<td>电话</td>
						<td>操作</td>
					</tr>
					<%
						List<User> users = (List<User>) request.getAttribute("users");
						for (int i = 0; i < users.size(); i++) {
							User user = users.get(i);
					%>
					<tr class="row<%=i % 2 + 1%>">
						<td><%=user.getId()%></td>
						<td><%=user.getUname()%></td>
						<td><%=user.getPwd()%></td>
						<td><%=user.getPhone()%></td>
						<td><a href="del.do?id=<%=user.getId()%>"
							onclick="return confirm('确定删除<%=user.getUname()%>吗?');">删除</a>&nbsp;
						</td>
					</tr>
					<%
						}
					%>
				</table>
				<p>
					<input type="button" class="button" value="添加用户"
						onclick="location='addUser.jsp'" />
				</p>
			</div>
		</div>
		<%@ include file="footer.jsp"%>
	</div>
</body>
</html>



