package vitaly

import calico.*
import calico.html.io.{*, given}
import cats.effect.*
import cats.implicits.{*, given}
import cats.syntax.all.{*, given}
import fs2.concurrent.SignallingRef.given
import fs2.concurrent.{*, given}
import calico.frp.given

def coolStuff() =
  SignallingRef[IO].of(false).product(SignallingRef[IO].of(""))
    .toResource
    .flatMap: (clicked, name) =>
      div(
        input.withSelf(self =>
          (
            placeholder := "Your name",
            onInput(e => self.value.get.flatMap(name.set))
          )
        ),
        button(
          (clicked: Signal[IO, Boolean]).product(name).map((isClicked, name) =>
            if isClicked then s"You did it, $name!" else "Click me!"
          ),
          onClick(IO.println("Clicked") >> clicked.set(true))
        )
      )
