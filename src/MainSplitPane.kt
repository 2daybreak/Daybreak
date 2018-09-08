import java.awt.Color
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class MainSplitPane: JSplitPane() {

    //Left
    val geometry = DefaultMutableTreeNode("Geometry")
    val grid = DefaultMutableTreeNode("Grid")
    val root = DefaultMutableTreeNode("Root")
    val model = DefaultTreeModel(root)
    val tree = JTree(model)
    val beginCurve = 1 // tunning

    //Right
    val mainPanel = DrawingPanel()
    val tabs = JTabbedPane()

    init {

        this.setLeftComponent(tree)
        this.setRightComponent(tabs)

        setDividerSize(3)
        dividerLocation = 150

        tabs.addTab("geometry",mainPanel)
        /*tabs.addTab("pitch", JLabel())
        tabs.addTab("chord",JLabel())
        tabs.addTab("skew",JLabel())
        tabs.addTab("rake",JLabel())
        tabs.addTab("camber",JLabel())
        tabs.addTab("thickness",JLabel())*/

        root.add(geometry)
        root.add(grid)
        tree.expandRow(0)
        tree.showsRootHandles = true
        tree.isRootVisible = false
        tree.background = Color(50, 50, 50)
        tree.addMouseListener(object: MouseListener {
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}
            override fun mousePressed(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseClicked(e: MouseEvent) {
                val row = tree.getRowForLocation(e.x, e.y)
                val name = tree.getPathForRow(row).toString()
                if(row != -1) mainPanel.ing = row - beginCurve
            }
        })

    }
}