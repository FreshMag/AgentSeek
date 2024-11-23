package io.github.agentseek.components.common

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.Layer
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Config {

    private val properties: Properties = Properties()

    init {
        val file = File(Config::class.java.getResource("/config.properties")?.file!!)
        if (file.exists()) {
            FileInputStream(file).use { properties.load(it) }
            println(properties)
        } else {
            GameEngine.logError("Config file not found: ${file.absolutePath}")
        }
    }

     fun getColorByName(name: String): Color? {
        try {
            return Color::class.java.getField(name.uppercase()).get(null) as Color
        } catch (e: Exception) {
            e.printStackTrace();
            return null
        }
    }

    object Player {
        val noiseEmittingRadius: Double
            get() = properties.getProperty("player.noiseEmittingRadius", "3.0").toDouble()
        val size: Double
            get() = properties.getProperty("player.size", "1.5").toDouble()
        val speed: Double
            get() = properties.getProperty("player.speed", "2.0").toDouble()
        val name: String
            get() = properties.getProperty("player.name", "Player")
    }

    object Agents {
        val cameraColor: Color
            get() = getColorByName(properties.getProperty("agents.camera.color", "DARK_GRAY")) ?: Color.BLACK
        val cameraRectangleSize: Double
            get() = properties.getProperty("agents.camera.rectangleSize", "1.0").toDouble()
        val cameraCirclesRadius: Double
            get() = properties.getProperty("agents.camera.circlesRadius", "0.5").toDouble()

        val guardDefaultColor: Color
            get() = getColorByName(properties.getProperty("agents.guard.defaultColor", "BLACK")) ?: Color.BLACK
        val guardDefaultNoiseSensorRadius: Double
            get() = properties.getProperty("agents.guard.defaultNoiseSensorRadius", "4.0").toDouble()
        val guardDefaultSightConeLength: Double
            get() = properties.getProperty("agents.guard.defaultSightConeLength", "10.0").toDouble()
        val guardDefaultSightConeApertureRadians: Double
            get() = properties.getProperty("agents.guard.defaultSightConeApertureRadians", "1.2").toDouble()
        val guardDefaultDistanceSensorRadius: Double
            get() = properties.getProperty("agents.guard.defaultDistanceSensorRadius", "2.8").toDouble()
        val guardSize: Double
            get() = properties.getProperty("agents.guard.size", "1.5").toDouble()
    }

    object Physics {
        val defaultMass: Double
            get() = properties.getProperty("physics.defaultMass", "1.0").toDouble()
    }

    object GUI {
        val menuTitle: String
            get() = properties.getProperty("gui.menuTitle", "Agent Seek")
        val frameTitle: String
            get() = properties.getProperty("gui.frameTitle", "Agent Seek")
        val frameWidth: Int
            get() = properties.getProperty("gui.frameWidth", "1000").toInt()
        val frameHeight: Int
            get() = properties.getProperty("gui.frameHeight", "720").toInt()
    }

    object Camera {
        val viewPortWidth: Double
            get() = properties.getProperty("camera.viewPort.width", "50.0").toDouble()
    }

    object Rendering {
        val defaultLayer: Layer
            get() = Layer.valueOf(properties.getProperty("rendering.defaultLayer", "GENERIC"))
        val defaultColor: Color
            get() = getColorByName(properties.getProperty("rendering.defaultColor", "BLACK")) ?: Color.BLACK
        val backgroundColor: Color
            get() = getColorByName(properties.getProperty("rendering.backgroundColor", "WHITE")) ?: Color.WHITE
        val textFont: String
            get() = properties.getProperty("rendering.textFont", "Serif")
    }

    object Names {
        val playerName: String
            get() = properties.getProperty("player.name", "Player")
        val wallName: String
            get() = properties.getProperty("wall.name", "Wall")
        val doorName: String
            get() = properties.getProperty("door.name", "Door")
    }

    object Components {
        val mouseEmitterNoiseRadius: Double
            get() = properties.getProperty("mouseEmitter.noiseRadius", "5.0").toDouble()
        val mouseEmitterNoiseDurationMillis: Long
            get() = properties.getProperty("mouseEmitter.noiseDurationMillis", "1000").toLong()
        val mouseEmitterPlayerMaxDistance: Double
            get() = properties.getProperty("mouseEmitter.playerMaxDistance", "15.0").toDouble()
        val noiseEmitterSpeedThreshold: Double
            get() = properties.getProperty("noiseEmitter.speedThreshold", "2.0").toDouble()
    }

    object FieldMovement {
        val rotationDegrees: Double
            get() = properties.getProperty("fieldMovement.rotationDegrees", "115.0").toDouble()
        val defaultMaxVelocity: Double
            get() = properties.getProperty("fieldMovement.defaultMaxVelocity", "3.5").toDouble()
        val dangerCoefficient: Double
            get() = properties.getProperty("fieldMovement.dangerCoefficient", "0.75").toDouble()
    }

    object VisualComponents {
        val sightSensorDefaultSuspiciousTimeMillis: Long
            get() = properties.getProperty("sightSensor.visual.defaultSuspiciousTimeMillis", "1000").toLong()
        val sightSensorText: String
            get() = properties.getProperty("sightSensor.visual.text", "!")
        val sightSensorTextColor: Color
            get() = getColorByName(properties.getProperty("sightSensor.visual.textColor", "BLACK")) ?: Color.BLACK
        val sightSensorFontSize: Int
            get() = properties.getProperty("sightSensor.visual.fontSize", "40").toInt()

        val noiseSensorDefaultSuspiciousTimeMillis: Long
            get() = properties.getProperty("noiseSensor.defaultSuspiciousTimeMillis", "3000").toLong()
        val noiseSensorText: String
            get() = properties.getProperty("noiseSensor.visual.text", "?")
        val noiseSensorTextColor: Color
            get() = getColorByName(properties.getProperty("noiseSensor.visual.textColor", "BLACK")) ?: Color.BLACK
        val noiseSensorFontSize: Int
            get() = properties.getProperty("noiseSensor.visual.fontSize", "40").toInt()
        val noiseSensorColor: Color
            get() = getColorByName(properties.getProperty("noiseSensor.visual.color", "YELLOW")) ?: Color.YELLOW

        val noiseEmitterColor: Color
            get() = getColorByName(properties.getProperty("noiseEmitter.visual.color", "BLACK")) ?: Color.BLACK
        val noiseEmitterDurationMillis: Long
            get() = properties.getProperty("noiseEmitter.visual.durationMillis", "500").toLong()

        val mouseEmitterText: String
            get() = properties.getProperty("mouseEmitter.visual.text", "BUM")
        val mouseEmitterFontSize: Int
            get() = properties.getProperty("mouseEmitter.visual.fontSize", "40").toInt()
        val mouseEmitterTextColor: Color
            get() = getColorByName(properties.getProperty("mouseEmitter.visual.textColor", "BLACK")) ?: Color.BLACK
    }
}