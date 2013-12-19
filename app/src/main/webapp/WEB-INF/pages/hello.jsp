<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" type="text/css" rel="stylesheet" media="screen" />
    <link href="<c:url value="/resources/css/bootstrap-responsive.min.css" />" type="text/css" rel="stylesheet" media="screen" />
</head>
<body>
	<h1>Number of distinct movie titles: ${moviesCount}</h1>
    <form:form method="GET" action="/search">
        <form:label path="name">What movie are you looking for?</form:label>
        <form:input path="name" />
        <input type="submit" value="Find" />
    </form:form>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
