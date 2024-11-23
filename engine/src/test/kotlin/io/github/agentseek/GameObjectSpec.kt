package io.github.agentseek

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.EngineTestingUtil
import io.github.agentseek.util.EngineTestingUtil.createComponent
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class GameObjectSpec: FreeSpec({
    var scene = EngineTestingUtil.setUpEmptyScene()

    beforeAny {
        scene = EngineTestingUtil.setUpEmptyScene()
    }
    afterAny {
        GameEngine.reset()
    }

    "A GameObject" - {
        "should have a unique ID" - {
            repeat((1..100).count()) {
                scene.emptyGameObject()
            }
            scene.gameObjects.map { it.id }.toSet() shouldHaveSize 100
        }
        "should be able to spawn other GameObjects" - {
            val go = scene.emptyGameObject()
            scene.gameObjects shouldHaveSize 1
            val go2 = scene.emptyGameObject(addToWorld = false)
            scene.gameObjects shouldHaveSize 1
            go.spawn(go2)
            scene.gameObjects shouldHaveSize 2
        }
        "should be able to spawn other GameObjects during the onUpdate" - {
            val go = scene.emptyGameObject(false)
            scene.gameObjects shouldHaveSize 0
            scene.createComponent(updateFun = {
                gameObject.spawn(go)
            })
            scene.gameObjects shouldHaveSize 1
            GameEngine.doOne()
            scene.gameObjects shouldHaveSize 2
            GameEngine.doOne()
            scene.gameObjects shouldHaveSize 3
        }
        "should be able to delete other GameObjects" - {
            val go = scene.emptyGameObject()
            scene.gameObjects shouldHaveSize 1
            val go2 = scene.emptyGameObject()
            scene.gameObjects shouldHaveSize 2
            go.world.removeGameObject(go2)
            scene.gameObjects shouldHaveSize 1
        }
        "should be able to delete other GameObjects during the onUpdate" - {
            val go = scene.emptyGameObject()
            scene.createComponent(updateFun = {
                gameObject.world.removeGameObject(go)
            })
            scene.gameObjects shouldHaveSize 2
            GameEngine.doOne()
            scene.gameObjects shouldHaveSize 1
            GameEngine.doOne()
            scene.gameObjects shouldHaveSize 1
        }
        "should be able to delete itself" - {
            val go = scene.emptyGameObject()
            scene.gameObjects shouldHaveSize 1
            go.delete()
            scene.gameObjects shouldHaveSize 0
        }
        "should be able to delete itself during onUpdate" - {
            scene.createComponent(updateFun = {
                gameObject.delete()
            })
            scene.gameObjects shouldHaveSize 1
            GameEngine.doOne()
            scene.gameObjects shouldHaveSize 0
        }
        "should be able to modify its position through its components" - {
            val (go, _) = scene.createComponent(updateFun = {
                gameObject.position += Vector2d(1.0, 0.0) * it.toDouble(DurationUnit.SECONDS)
            })
            go.position shouldBe Point2d.origin()
            GameEngine.doOne(1.seconds)
            go.position shouldBe Point2d(1.0, 0.0)
            GameEngine.doOne(0.5.seconds)
            go.position shouldBe Point2d(1.5, 0.0)
        }

    }

})