<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Web Checkers Sign-In</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
  <h2>Sign in:</h2>
  <p>
    <#if message??>
      <div class="message ${messageType}" style="color: red">${message}</div>
      <br>
    </#if>
    <form action="./signin" method="POST">
      <label for="signin">Enter a username:</label><br>
      <input type="text" id="signin" name="username"/><br>
      <input type=submit value="enter">
    </form>
  </p>
</body>

</html>
