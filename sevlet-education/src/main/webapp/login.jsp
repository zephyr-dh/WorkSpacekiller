<%@ page pageEncoding="utf-8" 
contentType="text/html; charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css"
			href="css/style.css" />
	</head>

	<body>
		<div id="wrap">
			<div id="top_content">
				<%@ include file="header.jsp" %>
				<div id="content">
					<p id="whereami">
					</p>
					<h1>
						登录
					</h1>
					<form action="login.do" method="post">
						<table cellpadding="0" cellspacing="0" border="0"
							class="form_table">
							<tr>
								<td valign="middle" align="right">
									用户名:
								</td>
								<td valign="middle" align="left">
									<input type="text" 
									class="inputgri" name="uname" />
									<%
										String msg = 
											(String)request.getAttribute(
													"login_failed");
									%>
									<span style="color:red;font-size:24px;">
										<%=msg == null ? "" : msg%>
									</span>
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">
									密码:
								</td>
								<td valign="middle" align="left">
									<input type="password" 
									class="inputgri" name="pwd" />
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">
									验证码:
								</td>
								<td valign="middle" align="left">
									<input type="text" 
									class="inputgri" name="number" />
									<img src="checkcode" border="1" 
		onclick="this.src='checkcode?' + Math.random()"/>
									<%
										String msg2 = 
										(String)request.getAttribute(
												"number_error");
									%>
									<span style="color:red;font-size:24px;">
										<%=msg2 == null ? "" : msg2 %>
									</span>
								</td>
							</tr>
						</table>
						<p>
							<input type="submit" class="button" 
							value="确定 &raquo;" /> 
						</p>
					</form>
				</div>
			</div>
			<%@ include file="footer.jsp" %>
		</div>
	</body>
</html>
