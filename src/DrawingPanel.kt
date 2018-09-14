import geoModel.*
import linearAlgebra.Vector3
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.JScrollPane
import javax.swing.KeyStroke
import javax.swing.event.TableModelEvent
import javax.swing.table.DefaultTableModel


class DrawingPanel: JPanel() {

    // Mouse, Key, Logic
    var iCurve = 0
    var iPoint = 0
    var mode = Mode.View
    enum class Mode{View, Curve, Slope, Surf}

    // Geometry
    val point = mutableListOf<Vector3>()
    val curve = mutableListOf<ParametricCurve>()
    val clipBoard = mutableListOf<ParametricCurve>()

    // Table
    private val tableModel = DefaultTableModel()
    private val table = JTable(tableModel)
    val subFrame = SubFrame(JScrollPane(table))

    //Popup
    val pop = PopupMenu()

    // Viewport
    //var viewport = Viewport()
    val size = 20

    init {
        background = Color(30, 30, 30)
        table.background = Color(50, 50, 50)
        table.foreground = Color.lightGray

        addMouseListener()
        addTableListener()
        addPopListener()
    }

    private fun addMouseListener() {
        addMouseListener(object: MouseListener {
            override fun mouseClicked(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                when(e.button) {
                    MouseEvent.BUTTON1 -> {
                        when(mode) {
                            Mode.View -> {
                                if(!curve.isEmpty()) {
                                    for(c in curve) {
                                        if (c.distance(v) < size) {
                                            iCurve = curve.indexOf(c)
                                            break
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                    MouseEvent.BUTTON3 -> {
                        pop.slope.isEnabled = false
                        pop.del.isEnabled = false
                        if(!curve.isEmpty() && iCurve != -1) {
                            val c = curve[iCurve]
                            for (t in c.prm) {
                                if (Point3(size, c(t)).contains(e.x, e.y)) {
                                    pop.slope.isEnabled = true
                                    pop.del.isEnabled = true
                                    iPoint = c.prm.indexOf(t)
                                    break
                                }
                            }
                        }
                        pop.show(e.component, e.x, e.y)
                    }
                }
            }
            override fun mouseExited (e: MouseEvent) { }
            override fun mouseEntered(e: MouseEvent) { }
            override fun mousePressed(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                var clickPts = false
                when (e.button) {
                    MouseEvent.BUTTON1 -> {
                        when(mode) {
                            Mode.View -> {

                            }
                            Mode.Curve -> {
                                val c = curve[iCurve]
                                if(c is InterpolatedBspline || c is InterpolatedNurbs) {
                                    for(t in c.prm) {
                                        if (Point3(size, c(t)).contains(e.x, e.y)) {
                                            clickPts = true
                                            iPoint = c.prm.indexOf(t)
                                            break
                                        }
                                    }
                                }
                                else {
                                    for (p in c.ctrlPts) {
                                        if (Point3(size, p).contains(e.x, e.y)) {
                                            clickPts = true
                                            iPoint = c.ctrlPts.indexOf(p)
                                            break
                                        }
                                    }
                                }
                                if (!clickPts) {
                                    c.addPts(v)
                                    iPoint = c.prm.size - 1
                                }
                            }
                            Mode.Slope -> {
                                val c = curve[iCurve]
                                val t = c.prm[iPoint]
                                val slope = (v - c(t)).normalize()
                                c.addSlope(iPoint, slope)
                                repaint()
                                mode = Mode.Curve
                            }
                        }
                    }
                    MouseEvent.BUTTON2 -> {}
                    MouseEvent.BUTTON3 -> {}
                }
            }
            override fun mouseReleased(e: MouseEvent) { repaint() }
        })
        addMouseMotionListener(object: MouseMotionListener {
            override fun mouseDragged(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                when(mode) {
                    Mode.View -> {}
                    Mode.Curve -> {
                        if(!curve.isEmpty() && iCurve != -1) {
                            val c = curve[iCurve]
                            if(iPoint != -1)
                                c.modPts(iPoint, v)
                            repaint()
                        }
                    }
                }

            }
            override fun mouseMoved(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                if(!curve.isEmpty()) {
                    val c = curve[iCurve]
                    if(!c.prm.isEmpty()) {
                        val t = c.prm[iPoint]
                        when(mode) {
                            Mode.View -> {
                                /* //orthogonality check
                                point.clear()
                                point.add(c(v))
                                point.add(v)
                                repaint()*/
                            }
                            Mode.Slope -> {
                                point.clear()
                                point.add(c(t))
                                point.add(v)
                                repaint()
                            }
                            else -> {}
                        }
                    }
                }
            }
        })
        addMouseWheelListener(object: MouseWheelListener {
            override fun mouseWheelMoved(e: MouseWheelEvent?) { }

        })

        addKeyListener(object : KeyListener {
            override fun keyPressed(e: KeyEvent) {
                println("aaaa") // never work
                when(e) {
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0) -> {
                        println("esc")
                        mode = Mode.View
                    }
                    else -> {}
                }
            }
            override fun keyReleased(e: KeyEvent) {}
            override fun keyTyped(e: KeyEvent) {}
        })
    }

    private fun addTableListener() {
        tableModel.addTableModelListener { e: TableModelEvent ->
            val row = e.firstRow
            val v = DoubleArray(3)
            if (row > -1) {
                try {
                    val c = curve[iCurve]
                    for(i in 0..2) v[i] = table.getValueAt(row, i).toString().toDouble()
                    when(mode) {
                        Mode.Curve -> {
                            c.removePts(row)
                            c.addPts(row, Vector3(v[0], v[1], v[2]))
                        }
                    }
                } catch (e: NumberFormatException) {
                    println(e.message)
                } finally {
                    repaint()
                }
            }
        }
    }

    private fun addPopListener() {
        pop.edit.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.Curve
            repaint()
        }

        pop.slope.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.Slope
        }

        pop.del.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.Curve
            val c = curve[iCurve]
            c.removePts(iPoint)
            c.removeSlope(iPoint)
            repaint()
        }

        pop.copy.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.View
            val c = curve[iCurve]
            clipBoard.add(c)
        }

        pop.paste.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.View
            for(c in clipBoard) curve.add(c)
            clipBoard.clear()
        }
    }

    override fun paintComponent(g: Graphics) {
        updatePaint(g)
        updateTable()
    }

    private fun updatePaint(g: Graphics) {
        super.paintComponent(g)
        g as Graphics2D

        //Draw Points for edit mode
        when(mode) {
            Mode.Curve, Mode.Slope ->
                curve[iCurve].drawPts(g, Color.YELLOW)
            else -> {}
        }

        //Draw Curves
        for(c in curve) when(curve.indexOf(c) == iCurve) {
                true -> c.drawCurve(g, Color.YELLOW)
                false -> c.drawCurve(g, Color.CYAN)
            }

        g.color = Color.WHITE
        for(i in 1 until point.size)
            g.drawLine(point[i-1].x.toInt(), point[i-1].y.toInt(), point[i].x.toInt(), point[i].y.toInt())
    }

    private fun updateTable() {
        val list = mutableListOf<Array<Double>>()
        when(mode) {
            Mode.View -> {}
            Mode.Curve -> {
                val c = curve[iCurve]
                if(c is InterpolatedBspline || c is InterpolatedNurbs)
                    for(t in c.prm)
                        list.add(arrayOf(c(t).x, c(t).y, c(t).z))
                else
                    for(p in c.ctrlPts)
                        list.add(arrayOf(p.x, p.y, p.z))
            }
            else -> {}
        }
        val data = list.toTypedArray()
        val head = arrayOf("x", "y", "z")
        tableModel.setDataVector(data,head)
    }

}