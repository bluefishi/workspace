<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->


  </head>
	  <!-- <frameset cols="25%,50%,25%">
	  <frame src="./index.jsp">
	  <frame src="login.jsp">
	  <frame src="register.jsp">
	  </frameset>
	   -->
<body >

<button id="myButton" >��д</button>
<input type="text" id="mytext"/>
<div id="mydiv"></div>
<hr>
ѡ����ҷ��ظù��ҵĳ��У�<br>
<select id="mySelect" onChange="check();">
<option value="0" checked>��ѡ��</option>
<option value="1">�й�</option>
<option value="2">����</option>
<option value="3">�ձ�</option>
</select>
<div id="city"></div>
</body>
<script>
var xmlHTTP;
function check()
{
	createXMLHttpRequest();
	var id = document.getElementById('mySelect').value;
	//alert(id);
	url = "countrytest.jsp?id="+id;
	xmlHTTP.open("GET",url,true);
	xmlHTTP.onreadystatechange=myready;
	xmlHTTP.send(null); 	
	
}
	
function myready(){
        if(xmlHTTP.readyState == 4){
               if(xmlHTTP.status==200){
       var xmlobj=xmlHTTP.responseText;//���صĶ�����ʵ���кܶ����͵�
      // alert(xmlobj);
       document.getElementById('city').innerHTML=xmlobj;
      
}else{
       alert("�������!");
}
}	
}	

function createXMLHttpRequest()
{
	if(window.XMLHttpRequest)
	{
		xmlHTTP=new XMLHttpRequest();
	}else if(window.ActiveXObject)
	{
		try{
		xmlHTTP = new ActiveXObject("Microsoft.XMLHTTP");
		}catch(e)
		{
			try{xmlHTTP = new ActiveXObject("Msxml2.XMLHTTP");}
			catch(e)
			{alert("your browser doesn't support XMLHTTP");}
		}
		
	}
	
}

function whello()
{	
	document.getElementById('mytext').value="hello,you";
	document.getElementById('mydiv').innerHTML = "ԭ������input����Ҫдvalue��";
}
document.getElementById('myButton').onclick=whello;
</script>
</html>
