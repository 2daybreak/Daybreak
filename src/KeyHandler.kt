import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class KeyHandler : KeyListener {
    override fun keyPressed(e: KeyEvent) {
        println("keyCode=${e.keyCode}")
        println("keyChar=${e.keyChar}")
        println("keyText=${KeyEvent.getKeyText(e.keyCode)}")
    }
    override fun keyReleased(e: KeyEvent) {}
    override fun keyTyped(e: KeyEvent) {}
}