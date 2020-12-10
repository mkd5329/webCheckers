<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <!-- Provide a message to the user, if supplied. -->
  <#include "message.ftl" />

  <div class="body">
    <h2>Player Lobby</h2>
    <#if currentUser??>
      <#list players as player>
        <form action="./" method="POST">
        <input type=hidden name="currentUser" value='${currentUser}'>
        <input type=hidden name="awaitingUser" value='${player}'>
          <h3><button type=submit>${player}</button></h3>
        </form>
      </#list><br>

      <h2>Start an AI Game</h2>
      <form action="./" method="POST">
              <input type=hidden name="currentUser" value='${currentUser}'>
              <input type=hidden name="awaitingUser" value='${currentUser}'>
                <h3><button type=submit>AI</button></h3>
      </form>
    <#else>
      <#if numberOfPlayers == 1>
        <p>There is ${numberOfPlayers} player waiting to play checkers.</p>
      <#else>
        <p>There are currently ${numberOfPlayers} players waiting to play checkers.</p>
      </#if>
    </#if>


    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
