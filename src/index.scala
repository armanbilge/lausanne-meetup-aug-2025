//> using platform js
//> using dep com.armanbilge::calico::0.2.3
//> using dep com.armanbilge::calico-router::0.2.3
//> using dep org.typelevel::cats-effect-cps::0.5.0
//> using jsModuleKind es

import calico.*
import calico.router.*
import calico.html.io.{*, given}
import cats.data.*
import cats.syntax.all.*
import cats.effect.*
import fs2.dom.*
import org.http4s.syntax.all.*

object MeetupApp extends IOWebApp:

  def render =
    Router(Window[IO]).toResource.flatMap { router =>

      def widgetUri(id: String) = uri"" +? ("widget" -> id)

      def nav = ul(widgets.toList.map { case Widget(name, id, _) =>
        li(
          a(
            href := "#",
            onClick(router.navigate(widgetUri(id))),
            name
          )
        )
      })

      val routes = widgets.reduceMap { case Widget(name, id, content) =>
        Routes.one[IO] {
          case uri if uri.query.params.get("widget").contains(id) => ()
        } { _ => div(h3(name), content) }
      }

      div(
        h1("Typelevel Meetup Lausanne August 2025"),
        p(a(href := "https://github.com/armanbilge/lausanne-meetup-aug-2025", "Source code on GitHub")),
        h2("Choose a widget"),
        nav,
        routes.toResource.flatMap(router.dispatch)
      )
    }

  case class Widget(name: String, id: String, content: Resource[IO, HtmlElement[IO]])

  def widgets = NonEmptyList.of(
    Widget("Dorothy", "oz", oz),
    Widget("Hello World", "hello", div(())),
    Widget(
      "Vitaly",
      "vitaly",
      vitaly.coolStuff(),
    )
  )
