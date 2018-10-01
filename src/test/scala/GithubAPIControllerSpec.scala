import scala.io.Source
import scala.util.{Success, Failure}
import scala.concurrent._

import io.vertx.scala.core.{Vertx, VertxOptions}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsObject, JsonArray}
import io.vertx.scala.core.http.HttpServerRequest
import io.vertx.scala.ext.web.client.WebClient
import io.vertx.scala.ext.web.client.HttpResponse
import io.vertx.scala.ext.web.codec.BodyCodec
import org.specs2._;
import org.specs2.concurrent.ExecutionEnv;
import scala.concurrent.duration.Duration;

class GithubAPIControllerSpec(implicit ee: ExecutionEnv) extends mutable.Specification {

   val vertx = Vertx.vertx()
   val githubAPI = new GithubAPIController();
   var client = WebClient.create(vertx);

  "GithubAPIController test if user-request on github-api returns valid jsonObject" >> {
    val response = Await.result(githubAPI.sendUserRequest("rschardt"), Duration.Inf).body().getOrElse(new JsonObject());
    response.getString("login")  must_== "rschardt";
    response.getString("name")  must_== null;
    response.getString("avatar_url") must_== "https://avatars0.githubusercontent.com/u/42609861?v=4";
    response.getInteger("followers") must_== 0;
  }

  "GithubAPIController test if created user-data is correct" >> {
    githubAPI.getUserData("rschardt").encodePrettily must_== createFakeUserDataResponse.encodePrettily
  }

  def createFakeUserDataResponse() : JsObject = {

    val jsonUser = Json.emptyObj();
    jsonUser
      .put("username", "rschardt")
      .put("name", "")
      .put("avatarUrl", "https://avatars0.githubusercontent.com/u/42609861?v=4")
      .put("numberOfFollowers", 0);
    return jsonUser;
  }

  "GithubAPIController test if language-request on github-api returns valid jsonObject" >> {
    val response = Await.result(githubAPI.sendLanguageRequest("Nix"), Duration.Inf).body().getOrElse(new JsonObject());
    response.getInteger("total_count") must_!= (0);
    response.getBoolean("incomplete_results")  must_== false;
    response.getJsonArray("items").isEmpty() must_== false;
  }
}
