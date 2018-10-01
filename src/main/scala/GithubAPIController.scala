import scala.concurrent._
import io.vertx.scala.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsObject, JsonArray}
import io.vertx.scala.core.http.HttpServerRequest
import io.vertx.scala.ext.web.client.WebClient
import io.vertx.scala.ext.web.client.HttpResponse
import io.vertx.scala.ext.web.codec.BodyCodec
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

class GithubAPIController() {

  var vertx = Vertx.vertx()
  var client = WebClient.create(vertx);

  def retrieveUserData(paramLang: String) : JsonArray = { 
    val jsonResponse = Await.result(sendLanguageRequest(paramLang), Duration.Inf).body().getOrElse(new JsonObject());
    val itemArray = jsonResponse.getJsonArray("items");
    fillDataArray(itemArray);
  }

  def fillDataArray(items: JsonArray) : JsonArray = {
    val data = new JsonArray();
    for (i <- 0 until items.size) {
      var item = items.getJsonObject(i);
      val user = getString(item, "login")
      val userData = getUserData(user)
      data.add(userData)
    }
    return data;
  }

  def sendLanguageRequest(paramLang: String) : Future[HttpResponse[JsonObject]] = {
    client.get(443, "api.github.com", "/search/users?q=language:" + paramLang).as(BodyCodec.jsonObject()).ssl(true).sendFuture()
  }

  def getUserData(paramUser: String) : JsonObject = { 
    val jsonResponse = Await.result(sendUserRequest(paramUser), Duration.Inf).body().getOrElse(new JsonObject());
    return createUser(jsonResponse);
  }

  def createUser(response: JsonObject) : JsObject = {
    val jsonUser = Json.emptyObj();
    jsonUser
      .put("username", getString(response, "login"))
      .put("name", getString(response, "name"))
      .put("avatarUrl", getString(response, "avatar_url"))
      .put("numberOfFollowers", response.getInteger("followers"))
  }

  // TODO this could be moved into a Helper-Class
  def getString(json: JsonObject, key: String) : String = {
    json.getString(key) match { 
      case str:String => str
      case _ => ""
    }
  }

  def sendUserRequest(paramUser: String) : Future[HttpResponse[JsonObject]] = {
    client.get(443, "api.github.com", "/users/" + paramUser).as(BodyCodec.jsonObject()).ssl(true).sendFuture()
  }
}
