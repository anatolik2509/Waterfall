<%@tag description="background image" pageEncoding="UTF-16" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="url" required="true" %>
<%@attribute name="fragment_class" required="true" %>
<style>
    .${fragment_class}{
        background-image: url("<c:url value="${url}"/>");
    }
</style>