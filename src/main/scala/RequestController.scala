import scala.concurrent._
import io.vertx.scala.core.Vertx
import io.vertx.core.Future
import io.vertx.scala.ext.web.{Router, Route}
import io.vertx.core.http.HttpMethod
import io.vertx.lang.scala.json.{Json, JsObject, JsonArray}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.http.HttpServerRequest
import ExecutionContext.Implicits.global

class RequestController extends ScalaVerticle {

  val githubAPI = new GithubAPIController();

  override def start() : Unit = {

    println("RequestController starting");
    startServer();
  }

  def startServer() : Unit = {
    var server = vertx.createHttpServer();
    var router = Router.router(vertx);
    var route = createRoute(router);
    handleRoute(route)
    server.requestHandler(router.accept _).listen(8080)
  }

  def createRoute(router: Router) : Route = {

    val urlString = "/search/:language"
    println("use this path: localhost:8080" + urlString);
    var route = router.route(HttpMethod.GET, urlString)
    return route;
  }

  def handleRoute(route: Route) : Unit = {
    route.handler((routingContext: io.vertx.scala.ext.web.RoutingContext) => {

      val paramName = "language";
      val paramValue = routingContext.request().getParam(paramName).get;

      var response = routingContext.response();
      var responseMessage = createResponse(paramValue);
      response.setStatusCode(200);
      response.putHeader("content-type", "application/json");
      response.end(responseMessage.encodePrettily);
    })
  }

  def createResponse(paramLang: String) : JsObject = {
      val jsonData = githubAPI.retrieveUserData(paramLang);
      val responseMessage = Json.emptyObj();
      responseMessage.put("status", 200).put("data", jsonData);

      return responseMessage;
  }

  // TODO wird gar nicht benutzt
  def createErrorResponse() : JsObject = {
      val dataArray = githubAPI.retrieveUserData("");
      val responseMessage = Json.emptyObj();
      responseMessage.put("status", 400).put("data", dataArray);
      return responseMessage;
  }

  override def stop() : Unit = {
    println("RequestController stopping");
  }
}
