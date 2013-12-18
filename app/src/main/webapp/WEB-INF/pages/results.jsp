<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<body>
    <div style="height: 60px;">
        <form:form method="GET" action="/search">
            <form:label path="name">What movie are you looking for?</form:label>
            <form:input path="name" />
            <input type="submit" value="Find" />
        </form:form>
    </div>

    <c:if test="${fn:length(results) eq 0}">
        No results found
    </c:if>

    <c:if test="${fn:length(results) gt 0}">
        <c:forEach items="${results}" var="result" >
            <div itemscope itemtype="http://schema.org/Movie">
                <div>
                    <c:choose>
                        <c:when test="${result.url != null}">
                            <a href="${result.url}" itemprop="name">${result.name}</a>
                        </c:when>
                        <c:otherwise>
                            <span itemprop="name">${result.name}</span>
                        </c:otherwise>
                    </c:choose>
                    (<c:forEach items="${result.years}" var="year">
                        <span itemprop="copyrightYear">${year}</span>
                    </c:forEach>)
                </div>
                <c:if test="${fn:length(result.genres) gt 0}">
                    <div>
                        Genres:
                        <c:forEach items="${result.genres}" var="genre">
                            <a href="/genre/${genre}" itemprop="genre">${genre}</a>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${result.company != null}">
                    <div>
                        From company:
                        <a href="/productionCompany/${result.company}" itemprop="productionCompany">${result.company}</a>
                    </div>
                </c:if>
            </div>
            <hr />
        </c:forEach>
    </c:if>
</body>
</html>
