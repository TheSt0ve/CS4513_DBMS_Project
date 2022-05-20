<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Input for Query 13</title>
    </head>
    <body>
        <h2>Input for query 13</h2>
        <!--
            Form for collecting user input for the new movie_night record.
            Upon form submission, add_movie.jsp file will be invoked.
        -->
        <form action="Johnson_Steven_IP_Task7_get_customers.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Desired Range:</th>
                </tr>
                <tr>
                    <td>Lower Bound</td>
                    <td><div style="text-align: center;">
                    <input type=text name=lowerBound>
                    </div></td>
                </tr>
                <tr>
                    <td>Upper Bound</td>
                    <td><div style="text-align: center;">
                    <input type=text name=upperBound>
                    </div></td>
                </tr>   
                <tr>
                    <td><div style="text-align: center;">
                    <input type=reset value=Clear>
                    </div></td>
                    <td><div style="text-align: center;">
                    <input type=submit value=GO>
                    </div></td>
                </tr>            
            </table>
        </form>
    </body>
</html>
