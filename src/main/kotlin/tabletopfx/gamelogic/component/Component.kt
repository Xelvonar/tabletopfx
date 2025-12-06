package tabletopfx.gamelogic.component

//@Serializable
//@Polymorphic
abstract class Component(
    var name: String,
    var imageRef: String,
    val id: Int = generateID()
) {
    companion object {
        private var nextID: Int = 0;
        private fun generateID() = nextID++;
    }
}