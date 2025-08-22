import calico.*
import calico.html.io.{*, given}
import cats.effect.*
import fs2.dom.*
import fs2.concurrent.SignallingRef

def ref = SignallingRef[IO].of("Unknown").toResource.flatMap { nameSigRef =>
  div(
    label("Cat's name: "),
    input.withSelf { (self: HtmlInputElement[IO]) =>
      (
        placeholder := "What's the name of your cat?",
        onChange(self.value.get.flatMap(nameSigRef.set))
      )
    },
    span(" Hello ", nameSigRef.map(_.toUpperCase).map(_ + "!"))
  )
}

def antonmia = div(
  h2("Scala days was"),
  a(
    href := "https://scaladays.org",
    img(
      alt := "One cat in a box",
      src := "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQD6BE7wiyRHAaiCGCyap2zrSGw-fehcbho9w&s"
    )
  ),
  h1("great! :-)"),
  ref
)
