package friday.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Tests tasks that occur over a period. */
class EventTest {

    @Test
    void toString_validEvent_returnsFormattedDisplay() {
        Event event = new Event(new String[] {"meeting", "2pm", "3pm"});

        assertEquals("[E][ ] meeting (from: 2pm to: 3pm)", event.toString());
    }

    @Test
    void constructor_valuesWithPrefixes_removesPrefixes() {
        Event event = new Event(new String[] {"meeting", "from 2pm", "to 3pm"});

        assertEquals("[E][ ] meeting (from: 2pm to: 3pm)", event.toString());
    }

    @Test
    void constructor_missingEnd_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () ->
                new Event(new String[] {"meeting", "2pm"}));
    }
}
