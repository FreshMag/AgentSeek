package ryleh.controller.core.factories

/**
 * A factory class for enemy entities.
 */
object EnemyFactory {
    var instance: EnemyFactory? = null
        /**
         * Gets Singleton for EnemyFactory.
         */
        get() {
            if (field == null) {
                field = EnemyFactory
            }
            return field
        }
}
