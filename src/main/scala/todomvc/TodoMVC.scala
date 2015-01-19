package todomvc

import scala.scalajs.js.JSApp
import org.scalajs.jquery.JQuery
import org.scalajs.jquery.jQuery

object TodoMVC extends JSApp {

    def main(): Unit = {
        jQuery(Controller.setupUI _)
    }
}
