package io.github.agentseek

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.EngineTestingUtil
import io.github.agentseek.util.FastEntities.createComponent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ComponentSpec : FreeSpec({
    var scene = EngineTestingUtil.setUpEmptyScene()

    beforeSpec {
        scene = EngineTestingUtil.setUpEmptyScene()
    }
    afterSpec {
        GameEngine.reset()
    }

    "A component" - {
        "should update when the world calls the update event" - {
            var counter = 0
            scene.createComponent({}, { counter++ })
            repeat((1..10).count()) { GameEngine.doOne() }
            counter shouldBe 10
        }
        "should not be update if the game object is not added to the world" - {
            var counter = 0
            scene.createComponent({}, { counter++ }, addToWorld = false)
            repeat((1..10).count()) { GameEngine.doOne() }
            counter shouldBe 0
        }
        "should not be updated once its game object gets deleted" - {
            var counter = 0
            val (_, component) = scene.createComponent({}, { counter++ })
            repeat((1..5).count()) { GameEngine.doOne() }
            component.gameObject.delete()
            repeat((1..5).count()) { GameEngine.doOne() }
            counter shouldBe 5
        }
        "should not be updated once it is removed from its game object" - {
            var counter = 0
            val (_, component) = scene.createComponent({}, { counter++ })
            repeat((1..5).count()) { GameEngine.doOne() }
            component.gameObject.removeComponent(component)
            repeat((1..5).count()) { GameEngine.doOne() }
            counter shouldBe 5
        }
        "should call init only when added to the game object" - {
            var counter = 0
            val (gameObj, component) = scene.createComponent({ counter++ }, addToGameObject = false)
            counter shouldBe 0
            gameObj.addComponent(component)
            counter shouldBe 1
        }
        "should call onRemoved only when removed from the game object" - {
            var counter = 0
            val (gameObj, component) = scene.createComponent(onRemovedFun = { counter++ }, addToGameObject = false)
            counter shouldBe 0
            gameObj.addComponent(component)
            counter shouldBe 0
            gameObj.removeComponent(component)
            counter shouldBe 1
        }
        "should not be added to a game object twice" - {
            val (gameObj, component) = scene.createComponent()
            shouldThrow<IllegalStateException> {
                gameObj.addComponent(component)
            }
        }
        "should correctly notify events to the world" - {
            var counter = 0
            scene.createComponent(
                initFun = { notifyEvent { counter++ } },
                addToGameObject = true
            )
            counter shouldBe 0
            GameEngine.doOne()
            counter shouldBe 1
        }
    }
})