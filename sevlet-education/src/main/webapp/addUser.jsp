<%@ page pageEncoding="utf-8" 
contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>用户管理</title>
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
						添加用户:
					</h1>
					<form action="add.do" 
						method="post">
						<table cellpadding="0" cellspacing="0" border="0"
							class="form_table">
							<tr>
								<td valign="middle" align="right">
									用户名:
								</td>
								<td valign="middle" align="left">
									<input type="text" 
									class="inputgri" name="uname" />
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">
									密码
								</td>
								<td valign="middle" align="left">
									<input type="password" 
									class="inputgri" name="pwd" />
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">
									电话:
								</td>
								<td valign="middle" align="left">
									<input type="text" class="inputgri" 
									name="phone" />
								</td>
							</tr>
						</table>
						<p>
							<input type="submit" class="button" 
							value="确定" />
						</p>
					</form>
				</div>
			</div>
			<%@ include file="footer.jsp" %>
		</div>
	</body>
</html>
