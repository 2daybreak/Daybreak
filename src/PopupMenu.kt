import java.awt.event.ActionEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class PopupMenu: JPopupMenu() {

    val slope = JMenuItem("Slope")
    val lock = JMenuItem("Lock")
    val unlock = JMenuItem("Unlock")
    val del = JMenuItem("Delete")
    val cut = JMenuItem("Cut")
    val copy = JMenuItem("Copy")
    val paste = JMenuItem("Paste")
    val pts = JMenuItem("Show Passing Points")
    val ctp = JMenuItem("Show Control Points")

    init {

        /*slp.addActionListener { e: ActionEvent ->
            slp.isEnabled = false
        }*/

        this.add(slope)
        this.add(lock)
        this.add(unlock)
        this.addSeparator()
        this.add(del)
        this.add(cut)
        this.add(copy)
        this.add(paste)
        this.addSeparator()
        this.add(pts)
        this.add(ctp)
    }
}
