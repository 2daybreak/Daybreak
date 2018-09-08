import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class MainFrame : JFrame() {

    private val center = MainSplitPane()
    private val circle = ActionList("Circle", center, 0)
    private val cBspln = ActionList("Control Points", center, 1)
    private val iBspln = ActionList("Passing Points", center, 2)
    private val cNurbs = ActionList("Control Points", center, 3)
    private val iNurbs = ActionList("Passing Points", center, 4)

    init {

        title = "MainFrame"
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
        contentPane.add(center)
        contentPane.addKeyListener(KeyHandler())
        contentPane.requestFocus()

        menuBar()
        toolBar()
        setSize(1024, 600)
    }

    fun toolBar() {
        val bar = JToolBar()
        bar.add(cBspln.btm)
        bar.add(iBspln.btm)
        bar.addSeparator()
        bar.add(JTextField("Text Field"))

        val dialog = JDialog()
        dialog.title = "Setting"
        dialog.add(JButton("TODO"))
        dialog.setSize(200, 200)
        val setting = JButton("Setting")
        setting.addActionListener { e: ActionEvent ->
            dialog.isVisible = true
        }
        bar.add(setting)
    }

    fun menuBar() {

        this.jMenuBar = JMenuBar()

        val file = JMenu("File")
        jMenuBar.add(file)

        val new = JMenuItem("New")
        file.add(new)
        new.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)
        new.addActionListener { e: ActionEvent -> TODO() }

        val save = JMenuItem("Save")
        file.add(save)
        save.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)
        save.addActionListener { e: ActionEvent -> TODO() }

        val saveAs = JMenuItem("Save As")
        file.add(saveAs)
        saveAs.accelerator = KeyStroke.getKeyStroke("F12")
        saveAs.addActionListener { e: ActionEvent -> TODO() }

        val open = JMenuItem("Open")
        file.add(open)
        open.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
        open.addActionListener { e: ActionEvent ->
            val chooser = JFileChooser()
            chooser.fileFilter = FileNameExtensionFilter("JPG", "jpg")
            chooser.dialogTitle = "Open file"
            val flag = chooser.showOpenDialog(null)
            val filePath: String
            if (flag == JFileChooser.APPROVE_OPTION) {
                filePath = chooser.selectedFile.path
                println(filePath)
            }
        }

        file.addSeparator()
        val exit = JMenuItem("Exit")
        file.add(exit)
        exit.addActionListener { e: ActionEvent -> TODO() }

        val edit = JMenu("Edit")
        jMenuBar.add(edit)

        val undo = JMenuItem("Undo")
        edit.add(undo)
        undo.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)
        undo.addActionListener { e: ActionEvent -> TODO() }

        val redo = JMenuItem("Redo")
        edit.add(redo)
        redo.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK)
        redo.addActionListener { e: ActionEvent -> TODO() }

        edit.addSeparator()

        val cut = JMenuItem("Cut")
        edit.add(cut)
        cut.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK)
        cut.addActionListener { e: ActionEvent -> TODO() }

        val copy = JMenuItem("Copy")
        edit.add(copy)
        copy.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)
        copy.addActionListener { e: ActionEvent -> TODO() }

        val paste = JMenuItem("Paste")
        edit.add(paste)
        paste.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK)
        paste.addActionListener { e: ActionEvent -> TODO() }

        val join = JMenuItem("Join")
        edit.add(join)
        join.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK)
        join.addActionListener { e: ActionEvent -> TODO() }

        val split = JMenuItem("Split")
        edit.add(split)
        split.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK)
        split.addActionListener { e: ActionEvent -> TODO() }

        val curve = JMenu("Curve")
        jMenuBar.add(curve)

        val bspln = JMenu("B-Spline")
        val nurbs = JMenu("Nurbs")
        curve.add(circle.itm)
        curve.add(bspln)
        curve.add(nurbs)
        bspln.add(cBspln.itm)
        bspln.add(iBspln.itm)
        nurbs.add(cNurbs.itm)
        nurbs.add(iNurbs.itm)

        val surface = JMenu("Surface")
        jMenuBar.add(surface)

        val tools = JMenu("Tools")
        jMenuBar.add(tools)

        val window = JMenu("Window")
        jMenuBar.add(window)

        val dataTable = JMenuItem("DataTable")
        dataTable.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)
        dataTable.addActionListener { e: ActionEvent ->
            center.mainPanel.subFrame.isVisible = true
        }
        window.add(dataTable)

    }

}
