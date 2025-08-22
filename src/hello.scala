import calico.*
import calico.html.io.{*, given}
import cats.effect.*
import fs2.dom.*
import fs2.concurrent.SignallingRef

def hello = SignallingRef[IO].of("world").toResource.flatMap { nameSigRef =>
  div(
    label("Your name: "),
    input.withSelf { self =>
      (
        placeholder := "Enter your name here",
        onInput(self.value.get.flatMap(nameSigRef.set(_)))
      )
    },
    span(" Hello, ", nameSigRef.map(_.toUpperCase()))
  )
}
