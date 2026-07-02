package tabletopfx.ui.selection

import javafx.collections.FXCollections
import javafx.collections.ObservableSet
import java.util.UUID

/**
 * ### Architectural Role: Ephemeral Client State Container
 * Tracks the unique domain identifiers ([UUID]) of components that currently hold active user focus.
 * * Isolating selection data loops into this decoupled container ensures that transient client-side visual states
 * never pollute the authoritative data records inside the logic engine.
 * * Utilizes an underlying [ObservableSet] to support low-latency reactive tracking loops.
 *
 * @see SelectionOverlay
 */
class SelectionModel {

    /**
     * Highly responsive observable tracking set.
     * Monitored directly by presentation layers to trigger real-time canvas highlights.
     */
    val selectedIds: ObservableSet<UUID> = FXCollections.observableSet()

    /**
     * Replaces the current selection collection universally with a single component identifier.
     *
     * @param id The targeted unique [UUID] to focus on.
     */
    fun select(id: UUID) {
        selectedIds.clear()
        selectedIds.add(id)
    }

    /**
     * Alternates the focus state of a specific element context.
     * Adds the identifier if missing, or removes it if already present inside the tracking set.
     *
     * @param id The targeted unique [UUID] to toggle.
     */
    fun toggle(id: UUID) {
        if (selectedIds.contains(id)) {
            selectedIds.remove(id)
        } else {
            selectedIds.add(id)
        }
    }

    /**
     * Flushes the entire focus ledger, dropping active client tracking down to baseline values.
     */
    fun clear() {
        selectedIds.clear()
    }
}