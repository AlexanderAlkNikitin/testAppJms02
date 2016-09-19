<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Jms Websphere App</h1>
        <div id="tabs">
            <%--<ul>--%>
                <%--<li><a href="#simple">Simple</a></li>--%>
            <%--</ul>--%>
            <div id="simple">
                <ul>
                    <li>
                        <a id="firstScenario" class="textLink" href="<c:url value="/scenarios/firstScenario"/>">First scenario send and receive message</a>
                    </li>
                    <li>
                        <a id="secondScenario" class="textLink" href="<c:url value="/scenarios/secondScenario"/>">Second scenario send to topic and drop</a>
                    </li>
                    <li>
                        <a id="thirdScenario" class="textLink" href="<c:url value="/scenarios/thirdScenario"/>">Third scenario send to topic and receive after stop</a>
                    </li>
                    <li>
                        <a id="fourthScenario" class="textLink" href="<c:url value="/scenarios/fourthScenario"/>">Fourth scenario send and receive</a>
                    </li>
                    <li>
                        <a id="fifthScenario" class="textLink" href="<c:url value="/scenarios/fifthScenario"/>">Fifth scenario send and receive rollback</a>
                    </li>
                </ul>
            </div>
            </div>
    </body>
</html>
