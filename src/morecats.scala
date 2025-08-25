import calico.*
import calico.*
import calico.html.io.{*, given}
import calico.syntax.*
import calico.unsafe.given
import cats.effect.*
import cats.effect.std.Random
import cats.syntax.all.*
import fs2.*
import fs2.concurrent.*
import fs2.dom.*
import scala.concurrent.duration.DurationInt

val kittens = List(
  "https://typelevel.org/cats-effect/docs/assets/semaphore.png",
  "https://typelevel.org/cats-effect/docs/assets/hierarchy-impure.jpeg",
  "https://typelevel.org/cats-effect/docs/assets/dispatcher.jpeg",
  "https://imgur.com/c9IEZXn.png",
  "https://imgur.com/D4Gt7JP.png",
  "https://imgur.com/UHZmjKl.png"
)

def morecats =
  Stream
    .fixedRateStartImmediately[IO](2.second)
    .holdOptionResource
    .flatMap: tick =>
      div(
        img(
          src <--
            Random
              .scalaUtilRandom[IO]
              .toResource
              .flatMap: random =>
                tick.discrete
                  .evalMap: _ =>
                    random
                      .betweenInt(0, kittens.length)
                      .map(kittens.apply)
                  .holdOptionResource
        )
      )
