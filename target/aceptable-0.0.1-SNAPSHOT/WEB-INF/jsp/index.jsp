<%@ taglib prefix="o" uri="http://openkm.com/tags/utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <jsp:include page="/header"/>
</head>
<body>
  <jsp:include page="/menu"/>
  <div class="container">
    <div class="jumbotron">
      <h1>Home</h1>
      <h2>Non authenticated user</h2>
      <p>This is the only page a non-authenticated user could see. If you want you authenticate please use Login option from top right corner.</p>
      <h2>Authenticated user</h2>
      <p>If the user has ROLE_ADMIN role he will be able to see Admin option in menu and access to these options.</p>
      <p>If the user has ROLE_CATALOG role then will see Catalog option in menu.</p>
    </div>
  </div>
  <jsp:include page="include/footer.jsp" />
</body>
</html>
