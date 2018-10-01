import io.vertx.scala.core.{Vertx, VertxOptions}
import io.vertx.lang.scala.ScalaVerticle

object Main extends App {

  var vertx = Vertx.vertx()
  vertx.deployVerticle(ScalaVerticle.nameForVerticle[RequestController]);
}
