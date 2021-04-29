<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="pagination">
  <c:url var="firstUrl" value="?p=1" />
  <c:url var="lastUrl" value="?p=${page.totalPages}" />
  <c:url var="prevUrl" value="?p=${page.number - 1}" />
  <c:url var="nextUrl" value="?p=${page.number + 1}" />
  <c:choose>
    <c:when test="${page.hasPrevious}">
      <li><a href="${firstUrl}">&lt;&lt;</a></li>
      <li><a href="${prevUrl}">&lt;</a></li>
    </c:when>
    <c:otherwise>
      <li class="disabled"><a href="#">&lt;&lt;</a></li>
      <li class="disabled"><a href="#">&lt;</a></li>
    </c:otherwise>
  </c:choose>
  <c:forEach items="${page.items}" var="item">
    <c:choose>
      <c:when test="${item.current}">
        <li class="active"><a href="#">${item.number}</a></li>
      </c:when>
      <c:otherwise>
        <li><a href="?p=${item.number}">${item.number}</a></li>
      </c:otherwise>
    </c:choose>
  </c:forEach>
  <c:choose>
    <c:when test="${page.hasNext}">
      <li><a href="${nextUrl}">&gt;</a></li>
      <li><a href="${lastUrl}">&gt;&gt;</a></li>
    </c:when>
    <c:otherwise>
      <li class="disabled"><a href="#">&gt;</a></li>
      <li class="disabled"><a href="#">&gt;&gt;</a></li>
    </c:otherwise>
  </c:choose>
</ul>
