package tabletopfx.ui.location

import javafx.geometry.Point2D
import tabletopfx.gamelogic.component.Location
import tabletopfx.gamelogic.component.UnknownLoc

/**
 * ### Architectural Role: Abstract Spatial Translation Strategy
 * Defines the operational boundary contract separating rendering layouts (View space pixels)
 * from authoritative rule evaluations (Logic space indexing variables).
 * * Implementing this strategy pattern decouple variant view types entirely. You can re-scale graphics,
 * change layout textures, or migrate from standard 2D boards to isometric layers without altering
 * any rule validation behaviors inside the logic blocks.
 *
 * @see SquareGridTranslator
 * @see Location
 */
interface LocationTranslator {

    /**
     * Translates an abstract game logic location reference back into visual screen pixel coordinates.
     * Predominantly invoked across the read-path timeline loop to position asset sprites properly.
     *
     * @param location Authoritative logical snapshot area tracking where a piece resides.
     * @return A configured [Point2D] mapping layout screen coordinates.
     */
    fun toPixels(location: Location): Point2D

    /**
     * Translates raw visual application scene coordinates into a conceptual domain location context.
     * Invoked during mouse releases or drops to transform physical mouse clicks into user intent.
     *
     * @param pixel Raw scene coordinate hit frame intercepted by a hardware pointer handler.
     * @return A validated subclass model of [Location], or [UnknownLoc] if the gesture misses mapped boundaries.
     */
    fun toLocation(pixel: Point2D): Location
}