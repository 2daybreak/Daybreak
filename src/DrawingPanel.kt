import geoModel.*
import linearAlgebra.Vector3
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JTable
import javax.swing.JScrollPane
import javax.swing.border.BevelBorder
import javax.swing.event.TableModelEvent
import javax.swing.table.DefaultTableModel


class DrawingPanel: JPanel() {

    // Mouse, Key, Logic
    var ing = 0
    var oldVector = Vector3()
    var oldIndex = 0
    var clickPts = false
    var mode = Mode.View
    enum class Mode{View, Curve, Slope, Surf}

    // Geometry
    val point = mutableListOf<Vector3>()
    val curve = mutableListOf<ParametricCurve>()

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
        addMouseListener(object: MouseListener {
            override fun mouseClicked(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                when (e.clickCount) {
                    2 -> println("DoubleClicked")
                    3 -> println("TripleClicked")
                    4 -> println("QuadrupleClicked")
                }
                when(e.button) {
                    MouseEvent.BUTTON1 -> {

                    }
                    MouseEvent.BUTTON3 -> {
                        pop.slope.isEnabled = false
                        pop.del.isEnabled = false
                        if(!curve.isEmpty()) {
                            val c = curve[ing]
                            for (t in c.prm) {
                                if (Point3(size, c(t)).contains(e.x, e.y)) {
                                    pop.slope.isEnabled = true
                                    pop.del.isEnabled = true
                                    oldIndex = c.prm.indexOf(t)
                                    break
                                }
                            }
                        }
                        pop.show(e.component, e.x, e.y)
                        oldVector = v
                    }
                }
            }
            override fun mouseExited (e: MouseEvent) { }
            override fun mouseEntered(e: MouseEvent) { }
            override fun mousePressed(e: MouseEvent) {
                when (e.button) {
                    MouseEvent.BUTTON1 -> {
                        val v = Vector3(e.x, e.y, 0)
                        when(mode) {
                            Mode.View -> {}
                            Mode.Curve -> {
                                val c = curve[ing]
                                if(c is InterpolatedBspline || c is InterpolatedNurbs) {
                                    for(t in c.prm) {
                                        if (Point3(size, c(t)).contains(e.x, e.y)) {
                                            clickPts = true
                                            oldIndex = c.prm.indexOf(t)
                                            break
                                        }
                                    }
                                }
                                else {
                                    for (p in c.ctrlPts) {
                                        if (Point3(size, p).contains(e.x, e.y)) {
                                            clickPts = true
                                            oldIndex = c.ctrlPts.indexOf(p)
                                            break
                                        }
                                    }
                                }
                                if (!clickPts) {
                                    c.addPts(v)
                                    oldIndex = c.prm.size - 1
                                }
                                clickPts = false
                                oldVector = v
                            }
                            Mode.Slope -> {
                                val c = curve[ing]
                                val t = c.prm[oldIndex]
                                val slope = (v - c(t)).normalize()
                                c.addSlope(oldIndex, slope)
                                repaint()
                                mode = Mode.Curve
                            }
                        }

                    }
                    MouseEvent.BUTTON2 -> {}
                    MouseEvent.BUTTON3 -> {

                    }
                }
            }
            override fun mouseReleased(e: MouseEvent) { repaint() }
        })
        addMouseMotionListener(object: MouseMotionListener {
            override fun mouseDragged(e: MouseEvent) {
                val c = curve[ing]
                when(mode) {
                    Mode.View -> {}
                    Mode.Curve -> {
                        val v = Vector3(e.x, e.y, 0)
                        c.modPts(oldIndex, v)
                        repaint()
                    }
                }

            }
            override fun mouseMoved(e: MouseEvent) {
                val v = Vector3(e.x, e.y, 0)
                if(!curve.isEmpty()) {
                    val c = curve[ing]
                    if(!c.prm.isEmpty()) {
                        val t = c.prm[oldIndex]
                        when(mode) {
                            Mode.View -> {
                                point.clear()
                                point.add(c(v))
                                point.add(v)
                                repaint()
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

        pop.slope.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            mode = Mode.Slope
        }

        pop.del.addActionListener{e: ActionEvent ->
            println("Popup menu item '${e.actionCommand}' was pressed.")
            val c = curve[ing]
            c.removePts(oldIndex)
            repaint()
            mode = Mode.Curve
        }
        
        table.background = Color(50, 50, 50)
        table.foreground = Color.lightGray
        tableModel.addTableModelListener { e: TableModelEvent ->
            val row = e.firstRow
            val v = DoubleArray(3)
            if (row > -1) {
                try {
                    val c = curve[ing]
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

    override fun paintComponent(g: Graphics) {
        updatePaint(g)
        updateTable()
    }

    private fun updatePaint(g: Graphics) {

        super.paintComponent(g)
        g as Graphics2D
        for(c in curve) c.draw(g)
        g.color = Color.WHITE
        for(i in 1 until point.size)
            g.drawLine(point[i-1].x.toInt(), point[i-1].y.toInt(), point[i].x.toInt(), point[i].y.toInt())
    }

    private fun updateTable() {
        val list = mutableListOf<Array<Double>>()
        when(mode) {
            Mode.View -> {}
            Mode.Curve -> {
                val c = curve[ing]
                if(c is InterpolatedBspline || c is InterpolatedNurbs)
                    for(t in c.prm)
                        list.add(arrayOf(c(t).x, c(t).y, c(t).z))
                    /*for(p in c.ctrlPts)
                        list.add(arrayOf(p.x, p.y, p.z))*/
                else
                    for(p in c.ctrlPts)
                        list.add(arrayOf(p.x, p.y, p.z))
            }
        }
        val data = list.toTypedArray()
        val head = arrayOf("x", "y", "z")
        tableModel.setDataVector(data,head)
    }

}
