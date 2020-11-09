<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<t:layout title="Сохраненные статьи">
    <div class="column-container" id="article-appender">

    </div>
    <script src="<c:url value="/js/rateScript.js"/>"></script>
    <script src="<c:url value="/js/savedLoadScript.js"/>"></script>
    <script>
        context = "${pageContext.request.contextPath}";
        updated = ${now.time};
        feedType = '${feedType}';
    </script>
    <c:if test="${not (lastArticle eq null)}">
        <%--@elvariable id="savedOffset" type="java.lang.Integer"--%>
        <script>
            articleAmount = ${lastArticle};
            $.ajax({url:context + "/saved?begin=" + 0 + "&end=" + articleAmount + "&updated=" + updated,
                success:function (msg) {
                    requestSend = true
                    $("#article-appender").append(msg)
                    window.scrollTo(0, ${savedOffset})
                }});
        </script>
    </c:if>
    <script>
        scrollController()
    </script>
    <%--@elvariable id="feedType" type="java.lang.String"--%>
    <%--@elvariable id="lastArticle" type="java.lang.Integer"--%>
</t:layout>


