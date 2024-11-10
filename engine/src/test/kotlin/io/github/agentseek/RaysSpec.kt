package io.github.agentseek

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.EngineTestingUtil
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class RaysSpec : FreeSpec({
    var scene = EngineTestingUtil.setUpEmptyScene()

    beforeAny {
        scene = EngineTestingUtil.setUpEmptyScene()
    }
    afterAny {
        GameEngine.reset()
    }

    val testedRigidBodies = table(
        headers("Rigid body setter"),
        row { go: GameObject -> RigidBody.RectangleRigidBody(go, 4.0, 4.0) },
        row { go: GameObject -> RigidBody.CircleRigidBody(go, 4.0) },
        row { go: GameObject -> RigidBody.ConeRigidBody(go, Math.PI, 4.0, 1.28) },
    )

    "A ray casted from a game object" - {
        forAll(testedRigidBodies) { rigidBody ->
            val startGo = scene.emptyGameObject()
                .also { it.rigidBody = RigidBody.RectangleRigidBody(it, 2.0, 2.0) }
            val firstCol = scene.emptyGameObject()
                .also { it.rigidBody = rigidBody(it); it.rigidBody.shape.center = Point2d(5.0, 5.0) }
            val secondCol = scene.emptyGameObject()
                .also { it.rigidBody = rigidBody(it); it.rigidBody.shape.center = Point2d(10.0, 10.0) }
            val thirdCol = scene.emptyGameObject()
                .also { it.rigidBody = rigidBody(it); it.rigidBody.shape.center = Point2d(15.0, 15.0) }

            "should return all game objects with rigid body ${rigidBody(firstCol)::class.simpleName} it intersects" - {
                var intersecting = startGo.castRay(thirdCol).allIntersecting
                intersecting.map { it.gameObject.id } shouldBe listOf(firstCol.id, secondCol.id, thirdCol.id)

                intersecting = firstCol.castRay(thirdCol).allIntersecting
                intersecting.map { it.gameObject.id } shouldBe listOf(secondCol.id, thirdCol.id)

                intersecting = firstCol.castRay(secondCol).allIntersecting
                intersecting.map { it.gameObject.id } shouldBe listOf(secondCol.id, thirdCol.id)

                intersecting = secondCol.castRay(thirdCol).allIntersecting
                intersecting.map { it.gameObject.id } shouldBe listOf(thirdCol.id)
            }

            "should return the first game object with rigid body ${rigidBody(firstCol)::class.simpleName} it intersects" - {
                var intersecting = startGo.castRay(firstCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe firstCol.id
                intersecting = startGo.castRay(secondCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe firstCol.id
                intersecting = startGo.castRay(thirdCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe firstCol.id

                intersecting = firstCol.castRay(thirdCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe secondCol.id
                intersecting = firstCol.castRay(secondCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe secondCol.id

                intersecting = secondCol.castRay(thirdCol).firstIntersecting
                intersecting?.gameObject?.id shouldBe thirdCol.id
            }
        }


    }
})