<html>
    <head>
        <script type="text/javascript">function submitform() { document.myform.submit(); }</script>
    </head>
    <body>
        <p>Hi ${firstName} ${lastName}, there is an upcoming ground school session:</p>
        <p>${event}</p>
        <hr>
        <p>Be sure to register for the session!</P
        <form name="register" action="${hostapp}/events/${eventId}/register/${userId}">
            <input type="hidden" name="userId" value="${userId}"/>
            <table>
                <tr>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <input type="button" name="register" value="REGISTER"/>
                    </td>
                    <td>
                        <input type="button" name="rsvp" value="DECLINE"/>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
