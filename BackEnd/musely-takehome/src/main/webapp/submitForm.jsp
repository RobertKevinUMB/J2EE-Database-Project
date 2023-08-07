<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Form Submission Confirmation</title>
</head>
<body>
    <h1>Form Submission Confirmation</h1>
    
    <p>Thank you for submitting the form!</p>
    <p>We have received the following information:</p>
    
    <ul>
        <li><strong>Name:</strong> <%= request.getParameter("name") %></li>
        <li><strong>Email:</strong> <%= request.getParameter("email") %></li>
        <li><strong>Message:</strong> <%= request.getParameter("balance") %></li>
    </ul>
    
    <!-- Add additional content or logic as needed -->
</body>
</html>
