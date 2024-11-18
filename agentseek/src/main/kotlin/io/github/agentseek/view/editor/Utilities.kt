package io.github.agentseek.view.editor

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.serialization.loadGameObject
import io.github.agentseek.util.serialization.save
import io.github.agentseek.view.Renderer
import io.github.agentseek.view.SimpleRenderer
import io.github.agentseek.view.gui.EditorGui.selectedGo
import io.github.agentseek.world.World
import java.awt.Cursor
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*

object Utilities {

    fun loadGameObjectDialog(frame: JFrame, scene: Scene) {
        val fileChooser = JFileChooser(File("./agentseek/src/main/resources"))
        fileChooser.dialogTitle = "Load Game Object"
        val result = fileChooser.showOpenDialog(frame)

        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            try {
                scene.world.loadGameObject(selectedFile.absolutePath)?.let {
                    println(it.id)
                    scene.world.addGameObject(it)
                }

            } catch (e: Exception) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Error loading game object: ${e.message}",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }

    fun addGameObjectDialog(frame: JFrame, scene: Scene) {
        val addDialog = JDialog(frame, "Add New Item", true)
        addDialog.layout = GridLayout(0, 2)

        val nameLabel = JLabel("Name:")
        val nameField = JTextField()
        val rendererLabel = JLabel("Renderer class FQ name:")
        val rendererField = JTextField()
        val xLabel = JLabel("Position X:")
        val xField = JTextField()
        val yLabel = JLabel("Position Y:")
        val yField = JTextField()

        addDialog.add(nameLabel)
        addDialog.add(nameField)
        addDialog.add(rendererLabel)
        addDialog.add(rendererField)
        addDialog.add(xLabel)
        addDialog.add(xField)
        addDialog.add(yLabel)
        addDialog.add(yField)

        val okButton = JButton("OK")
        val cancelButton = JButton("Cancel")

        okButton.addActionListener {
            fun loadRenderer(rendererClassName: String): Renderer<*>? {
                return try {
                    val clazz = ClassLoader.getSystemClassLoader().loadClass(rendererClassName)
                    if (Renderer::class.java.isAssignableFrom(clazz)) {
                        clazz.getDeclaredConstructor().newInstance() as Renderer<*>
                    } else {
                        println("The class $rendererClassName does not extend Renderer.")
                        null
                    }
                } catch (e: ClassNotFoundException) {
                    println("Class not found: $rendererClassName")
                    null
                } catch (e: Exception) {
                    println("Error while loading the renderer: ${e.message}")
                    null
                }
            }

            val name = nameField.text
            val rendererClass = rendererField.text
            val xPos = xField.text.toDoubleOrNull()
            val yPos = yField.text.toDoubleOrNull()

            val go = scene.world.gameObjectBuilder()
                .name(name)
                .position(xPos ?: 0.0, yPos ?: 0.0)
                .renderer(loadRenderer(rendererClass) ?: SimpleRenderer())
                .build()
            println(go.id)
            scene.world.addGameObject(go)
            addDialog.dispose()
        }

        cancelButton.addActionListener {
            addDialog.dispose()
        }

        addDialog.add(okButton)
        addDialog.add(cancelButton)

        addDialog.setSize(400, 300)
        addDialog.isVisible = true
    }

    fun JPanel.addClickListener(scene: Scene) {
        addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                val x = e?.x ?: return
                val y = e.y
                GameEngine.view?.camera?.toWorldPoint(Point2d(x.toDouble(), y.toDouble()))?.let { point ->
                    val go = scene.gameObjects.firstOrNull { it.rigidBody.shape.contains(point) }
                    go?.let {
                        println("Selected ${it.id}")
                        selectedGo = it
                    }
                }
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}

        })
    }

    fun keyListener(jPanel: JPanel, jFrame: JFrame, scene: Scene): KeyListener =
        object : KeyListener {

            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_SPACE -> GameEngine.doOne()
                    KeyEvent.VK_M -> moveBehavior(jPanel, jFrame)
                    KeyEvent.VK_D ->
                        duplicateBehavior(
                            jPanel,
                            jFrame,
                            scene.world,
                        )

                    KeyEvent.VK_BACK_SPACE -> {
                        println("Deleting ${selectedGo?.id}")
                        selectedGo?.delete()
                    }
                }
            }

            override fun keyTyped(e: KeyEvent) {}
            override fun keyReleased(e: KeyEvent?) {}
        }

    fun moveBehavior(panel: JPanel, frame: JFrame) {
        val previous = panel.mouseListeners.first()
        panel.removeMouseListener(previous)
        frame.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)

        panel.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                val x = e?.x ?: return
                val y = e.y
                GameEngine.view?.camera?.toWorldPoint(Point2d(x.toDouble(), y.toDouble()))?.let {
                    println("Moved ${selectedGo?.id} to (${it.x}, ${it.y})")
                    selectedGo?.rigidBody?.shape?.center = it
                    panel.removeMouseListener(this)
                    panel.addMouseListener(previous)
                    frame.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
                }
            }

            override fun mousePressed(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}

        })
    }

    fun duplicateBehavior(panel: JPanel, frame: JFrame, world: World) {
        val previous = panel.mouseListeners.first()
        panel.removeMouseListener(previous)
        frame.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)

        panel.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                val x = e?.x ?: return
                val y = e.y
                GameEngine.view?.camera?.toWorldPoint(Point2d(x.toDouble(), y.toDouble()))?.let {
                    if (selectedGo != null) {
                        println("Duplicated ${selectedGo?.id} to (${it.x}, ${it.y})")
                        val go = duplicate(world, selectedGo!!)
                        go.rigidBody.shape.center = it
                        world.addGameObject(go)
                        panel.removeMouseListener(this)
                        panel.addMouseListener(previous)
                        frame.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
                    }
                }
            }

            override fun mousePressed(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}

        })

    }

    fun duplicate(world: World, original: GameObject): GameObject {
        val path = original.save("./", original.name)
        val go = world.loadGameObject(path)!!
        File(path).delete()
        return go
    }
}