<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
	<div>
		<c:forEach var="movie" items="${list}">
			<div style="display:inline-block">
				<div align="center" style="width:100%">${movie.movieNm }</div><br>
				<img style="width:213px;" src="${ movie.posterUrl }"/>
			</div>
		</c:forEach>
	</div>
</body>
</html>