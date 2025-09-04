import calico.*
import calico.html.io.{*, given}
import cats.effect.*
import cats.implicits.{*, given}
import cats.syntax.all.{*, given}
import fs2.concurrent.SignallingRef.given
import fs2.concurrent.{*, given}
import calico.frp.given

private val guestsList: List[String] = List(
  "dominique",
  "will",
  "guillaume",
  "matej",
  "robert",
  "mihai",
  "seth",
  "agnès",
  "david",
  "sabrina",
  "anton",
  "artem",
  "ingemar",
  "asad",
  "mia",
  "lars",
  "paola",
  "vitaly",
  "kannupriya",
  "reda",
  "jose",
  "thanh",
  "arman",
  "antonio"
)

def meetup =
  SignallingRef[IO]
    .of(false)
    .product(SignallingRef[IO].of(""))
    .toResource
    .flatMap: (clicked, name) =>
      div(
        input.withSelf(self =>
          (
            styleAttr   := "width: 275px; flex-direction: row; gap: 16px;",
            placeholder := "Check if your name is on the guest list",
            onInput(e => self.value.get.flatMap(name.set))
          )
        ),
        button(
          "Check",
          onClick(IO.println("Clicked") >> clicked.set(true)),
          styleAttr := "background-color: #447E7E; color: white;"
        ),
        h4(
          (clicked: Signal[IO, Boolean])
            .product(name)
            .map((isClicked, name) =>
              val lowerCaseName = name.toLowerCase.trim
              (isClicked, guestsList.contains(lowerCaseName), lowerCaseName) match
                case (true, true, "arman") => s"$name was in the meetup! He presented Calico!! :)"
                case (true, true, _)       => s"$name was in the meetup! nice to meet you :)"
                case (true, false, _)      => s"$name was not in the meetup :( Hopefully next time!"
                case _                     => ""
            )
        )
      )
