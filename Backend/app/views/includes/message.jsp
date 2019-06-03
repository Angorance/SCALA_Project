<%--
  Created by IntelliJ IDEA.
  User: james
  Date: 11/11/18
  Time: 3:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
    <c:remove var="error" scope="session" />
</c:if>
<c:if test="${info != null}">
    <div class="alert alert-success" role="alert">
            ${info}
    </div>
    <c:remove var="info" scope="session" />
</c:if>
<c:if test="${warning != null}">
    <div class="alert alert-warning">
        ${warning}
        <c:remove var="warning" scope="session" />
    </div>
</c:if>
