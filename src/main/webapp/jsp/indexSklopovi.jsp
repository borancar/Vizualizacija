<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %>
<%
	// Koristi se crtač za sklopovski prikaz
	
	String izraz = (String) request.getParameter("izraz");
	String notacija = (String) request.getParameter("notacija");	
	String submitted = (String) request.getParameter("submitted");
	boolean ispis = (submitted != null) && (submitted.equals("1")) ? true : false;
	
	
	if (ispis) {
		if ((izraz != null) && !izraz.equals("")) {
			// Enkodiramo izraz
			izraz = URLEncoder.encode(izraz, "UTF-8");
		} else {
			izraz = "";
			ispis = false;
		}
	}

	String crtacSklopovi = !ispis ? "#" :
			"prikazi?izraz=" + izraz + "&notacija=" + notacija + "&tip=logicki-sklopovi";
	String sakrij = !ispis ? "none" : "inline";
	
	// Dekodiranje
	if (ispis) izraz = URLDecoder.decode(izraz, "UTF-8");
%>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<meta name="author" content="JavaTečaj tim 5"/>
	<title>Vizualizacija logičkih izraza</title>
</head>

<body>
	<h1>Vizualizacija logičkih izraza</h1>
	<p>
		<form action="indexSklopovi.jsp" method="post" enctype="x-www-form-urlencoded">
			<DL>
				<DT>Logički izraz:
					<DD><input type="text" name="izraz" id="izraz" value="<%= ispis ? izraz : "" %>" size="100" />
				<DT>Notacija:
				  <DD><input type="radio" name="notacija" id="notacija" value="infix" <%= !ispis ? "checked" : (notacija.equals("infix") ? "checked" : "") %> />Infix
				  <DD><input type="radio" name="notacija" id="notacija" value="prefix" <%= ispis && notacija.equals("prefix") ? "checked" : "" %> />Prefix
			</DL>
			<input type="hidden" name="submitted" value="1" />
			<input type="submit" value="Crtaj" />
		</form>
	</p>
	
	<div style="display:<%= sakrij %>;">
		<h1><%= izraz %></h1>
	</div>
	
	<div style="display:<%= sakrij %>;">
		<h2>Sklopovski prikaz</h2>
		<img src="<%= crtacSklopovi %>" id="crtacSklopovi" style="display:<%= sakrij %>;" />
	</div>
</body>
</html>
