<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
    <div style="height: 60px;">
        <div style="float: left">
            <form:form method="GET" action="/search">
                <form:label path="name">What movie are you looking for?</form:label>
                <form:input path="name" />
                <input type="submit" value="Find" />
            </form:form>
        </div>
        <div style="float: right">
            Results: ${numberOfResults}
        </div>
    </div>

    <c:forEach items="${results}" var="result" >
        <div itemscope itemtype="http://schema.org/Movie">
            <div itemprop="name">
                ${result.name}
                (<c:forEach items="${result.years}" var="year">
                    ${year}
                </c:forEach>)
            </div>
            <div>
                <c:forEach items="${result.genres}" var="genre">
                    <a href="/genre/${genre}">${genre}</a>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</body>
</html>
