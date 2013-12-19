<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" type="text/css" rel="stylesheet" media="screen" />
    <link href="<c:url value="/resources/css/bootstrap-responsive.min.css" />" type="text/css" rel="stylesheet" media="screen" />
</head>
<body>

    <div class="container" style="padding-top: 15px;">
        <div class="row">
            <form:form method="GET" action="/search" cssClass="form-search">
                <form:input cssClass="span10" path="name" placeholder="What movie are you looking for?" />
                <input class="span2 btn btn-primary" type="submit" value="Search" />
            </form:form>
        </div>

        <c:if test="${fn:length(results) eq 0}">
            <div class="row">No results found</div>
        </c:if>
        <c:if test="${fn:length(results) gt 0}">
            <div class="row">Results for ${command.name}</div>
            <c:forEach items="${results}" var="result" >
                <div class="row" style="border-bottom: 1px solid #CCCCCC; padding-bottom: 10px; padding-top: 10px;">
                    <div about="${result.node}" xmlns:v="http://schema.org/#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" typeof="v:Movie">
                        <div>
                            <strong><span property="v:name">${result.name}</span></strong>
                            (<c:forEach items="${result.years}" var="year">
                                <span property="v:copyrightYear">${year}</span>
                            </c:forEach>)
                            <c:if test="${result.url != null}">
                                <a about="${result.node}" property="rdfs:seeAlso" content="${result.url}" href="${result.url}">
                                    <c:choose>
                                        <c:when test="${result.urlType == 'DVD'}">
                                            dvd
                                        </c:when>
                                        <c:otherwise>
                                            imdb
                                        </c:otherwise>
                                    </c:choose>
                                    <i class="icon-share"></i>
                                </a>
                            </c:if>
                            <c:if test="${result.dbpediaUrl != null}">
                                <a about="${result.node}" property="owl:sameAs" content="${result.dbpediaUrl}" href="${result.dbpediaUrl}">
                                    <i>dbpedia</i> <i class="icon-share"></i>
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
                </div>
            </c:forEach>
        </c:if>
    </div>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
