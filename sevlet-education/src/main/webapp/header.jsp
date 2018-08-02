<%@ page import="java.util.*,java.text.*"%>
<div id="header">
	<div id="rightheader">
		<p>
			<%
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			%>
			<%=sdf.format(date)%>
			<br />
		</p>
	</div>
	<div id="topheader">
		<h1 id="title">
			<a href="#">main</a>
		</h1>
	</div>
	<div id="navigation"></div>
</div>