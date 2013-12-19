<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" type="text/css" rel="stylesheet" media="screen" />
    <link href="<c:url value="/resources/css/bootstrap-responsive.min.css" />" type="text/css" rel="stylesheet" media="screen" />
</head>
<body>

    <div class="container" style="padding-top: 30px;">
        <div class="row" style="text-align: center;">
            <h1>Movie seeker</h1>
            <div style="padding-bottom: 15px;">${moviesCount} movies in database</div>
        </div>
        <div class="row">
            <form:form method="GET" action="/search" cssClass="form-search">
                <form:input cssClass="span10" path="name" placeholder="What movie are you looking for?" />
                <input class="span2 btn btn-primary" type="submit" value="Search" />
            </form:form>
        </div>
    </div>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
