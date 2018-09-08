import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class SubFrame(jScrollPane: JComponent): JFrame() {

    init {

        title = "DataTable"
        isVisible = true
        contentPane.add(jScrollPane)

        MenuBar()

        setLocation(1024,0)
        setSize(250, 600)
    }
    private fun MenuBar() {

        this.jMenuBar = JMenuBar()

        val file    = JMenu("File")
        jMenuBar.add(file)

        val save = JMenuItem("Save")
        file.add(save)
        save.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK)
        save.addActionListener{e: ActionEvent -> {}}

    }
}