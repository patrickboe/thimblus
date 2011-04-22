import scala.swing._

object App extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "Thimblr"
    contents = new BoxPanel(Orientation.Vertical) {
    }
  }
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
