import scala.io.Source
import scala.util.{Success, Failure}
import scala.concurrent._

import io.vertx.scala.core.{Vertx, VertxOptions}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.http.HttpServerRequest
import io.vertx.scala.ext.web.client.WebClient
import io.vertx.scala.ext.web.client.HttpResponse
import io.vertx.scala.ext.web.codec.BodyCodec
import org.specs2._;
import org.specs2.concurrent.ExecutionEnv;
import scala.concurrent.duration.Duration;

class RequestControllerSpec(implicit ee: ExecutionEnv) extends mutable.Specification {

  val vertx = Vertx.vertx(VertxOptions()
    .setBlockedThreadCheckInterval(200000000)
  )
  val requestController = new RequestController;
  var lang = "Nix";
  var client = WebClient.create(vertx);

  // start Server
  vertx.deployVerticle(ScalaVerticle.nameForVerticle[RequestController]);

  "RequestController should return a valid Json-Response" >> {
    val response = Await.result(sendFakeRequest, Duration.Inf).body().getOrElse("");
    val jsonResponse = new JsonObject(response)
    jsonResponse.getInteger("status")  must_== 200;
    jsonResponse.getJsonArray("data").isEmpty() must_== false;
  }

  def sendFakeRequest() : Future[HttpResponse[String]] = {
    client.get(8080, "localhost", "/search/" + lang).as(BodyCodec.string()).sendFuture() 
  }
}
