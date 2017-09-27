package com.example.app.Routes

import com.example.app.models.ResponseRequest
import com.example.app.{AuthenticationSupport, CookieStrategy, SlickRoutes}

trait AppRoutes extends SlickRoutes with AuthenticationSupport{


  get("/") {
    val authenticated = new CookieStrategy(this).checkAuthentication()

    //ResponseRequest.createResponseRequestLater()

    <html>
      <head>
        <link rel="stylesheet" href="/front-end/dist/main.css" />
        </head>
        <body>
          <div id="app"></div>
          <script>
            var CONFIG = new Object();
            CONFIG.auth = {authenticated.isDefined};
          </script>
          <script src="/front-end/dist/bundle.js"></script>
        </body>
      </html>
  }

}
