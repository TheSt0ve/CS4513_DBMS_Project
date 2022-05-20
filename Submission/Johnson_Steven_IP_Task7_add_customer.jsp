<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
    <body>
    <%@page import="jsp_azure_test.DataHandler"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <%
    // The handler is the one in charge of establishing the connection.
    DataHandler handler = new DataHandler();

    // Get the attribute values passed from the input form.
    String customerName = request.getParameter("name");
    String customerAddress = request.getParameter("address");
    String customerCategory = request.getParameter("category");

    /*
     * If the user hasn't filled out all the time, movie name and duration. This is very simple checking.
     */
    if (customerName.equals("") || customerAddress.equals("") || customerCategory.equals("")) {
        response.sendRedirect("Johnson_Steven_IP_Task7_add_customer_form.jsp");
    } else {
        int category = Integer.parseInt(customerCategory);
        
        // Now perform the query with the data from the form.
        boolean success = handler.queryOne(customerName, customerAddress, category);
        if (!success) { // Something went wrong
            %>
                <h2>There was a problem inserting the course</h2>
            <%
        } else { // Confirm success to the user
            %>
            <h2>New Customer</h2>

            <ul>
                <li>Name <%=customerName%></li>
                <li>Address <%=customerAddress%></li>
                <li>Category <%=category%></li>
            </ul>

            <h2>Was successfully inserted.</h2>
            
            <a href="Johnson_Steven_IP_Task7_get_customers_form.jsp">Call Query 13 here to find customers within a category range.</a>
            <%
        }
    }
    %>
    </body>
</html>
