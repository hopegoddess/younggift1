<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>show result</title>
</head>
<body>
<div id="whole" align="center">
	<table>
	<tr><td>
		<div id="result1" align="left">
			<%
				String str[] = (String[])request.getAttribute("result1");
				int len = str.length;
					//out.println(len);
				for(int i=0;str[i]!=null;i++)
					out.println(str[i]);
				//String str=request.getAttribute("result1").toString();
				//out.println(str);
			%>
		</div>
		<div id="result2" align="left">
			<%
				String str1=request.getAttribute("result2").toString();
				out.print("=");
				out.println(str1);
			%>
		</div>
	</td></tr>
	</table>
</div>
</body>
</html>