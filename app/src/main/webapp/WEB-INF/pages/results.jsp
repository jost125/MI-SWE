<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
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
            <div about="${result.node}" xmlns:v="http://schema.org/#" xmlns:owl="http://www.w3.org/2002/07/owl#" typeof="v:Movie">
                <div>
                    <span property="v:name">${result.name}</span>
                    (<c:forEach items="${result.years}" var="year">
                        <span property="v:copyrightYear">${year}</span>
                    </c:forEach>)
                    <c:if test="${result.url != null}">
                        <a href="${result.url}">
                            <i>
                                <c:choose>
                                    <c:when test="${result.urlType == 'DVD'}">
                                        dvd
                                    </c:when>
                                    <c:otherwise>
                                        imdb
                                    </c:otherwise>
                                </c:choose>
                            </i>
                        </a>
                    </c:if>
                    <c:if test="${result.dbpediaUrl != null}">
                        <a about="${result.node}" property="owl:sameAs" content="${result.dbpediaUrl}" href="${result.dbpediaUrl}">
                            <i>dbpedia</i>
                        </a>
                    </c:if>
                </div>
                <c:if test="${result.rating != null}">
                    <div property="v:rating" typeof="v:AggregateRating">
                        Rating:
                        <span property="v:ratingValue">${result.rating}</span>/<span property="v:bestRating">10</span>
                    </div>
                </c:if>
                <c:if test="${fn:length(result.genres) gt 0}">
                    <div>
                        Genres:
                        <c:forEach items="${result.genres}" var="genre">
                            <a href="/genre/${genre}" property="v:genre">${genre}</a>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${result.company != null}">
                    <div>
                        From company:
                        <a href="/productionCompany/${result.company}" property="v:productionCompany">${result.company}</a>
                    </div>
                </c:if>
            </div>
            <hr />
        </c:forEach>
    </c:if>
</body>
</html>
