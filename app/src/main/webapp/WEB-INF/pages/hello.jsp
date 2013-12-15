<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>
	<h1>Number of distinct movie titles: ${moviesCount}</h1>
    <form:form method="GET" action="/search">
        <form:label path="name">What movie are you looking for?</form:label>
        <form:input path="name" />
        <input type="submit" value="Find" />
    </form:form>
</body>
</html>
