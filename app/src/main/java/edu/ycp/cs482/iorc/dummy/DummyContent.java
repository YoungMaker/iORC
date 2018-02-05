package edu.ycp.cs482.iorc.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final List<DummyClass> CLASSES = new ArrayList<DummyClass>();
    public static final List<DummyCharacter> CHARACTERS = new ArrayList<DummyCharacter>();
    public static final List<DummyAlignment> ALIGNMENTS = new ArrayList<DummyAlignment>();




    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static final Map<String, DummyClass> CLASS_MAP = new HashMap<String, DummyClass>();
    public static final Map<String, DummyCharacter> CHARACTER_MAP = new HashMap<String, DummyCharacter>();
    public static final Map<String, DummyAlignment> ALIGNMENT_MAP = new HashMap<String, DummyAlignment>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
            addClass(createDummyClasses(i));
            addCharacter(createDummyCharacters(i));
        }
        addAlignment(createDummyAlignment("1", "Lawful Good", "Characters that act within the law and follow it's processes."));
        addAlignment(createDummyAlignment("2", "True Good", "Character that does what they think is right, regardless of law."));
        addAlignment(createDummyAlignment("3", "Chaotic Good", "Characters that do what needs to be done for a good end result, regardless of means"));
        addAlignment(createDummyAlignment("4", "Lawful Neutral", "Characters that act as a neutral party in debates."));
        addAlignment(createDummyAlignment("5", "True Neutral", "Characters that act towards their own interests."));
        addAlignment(createDummyAlignment("6", "Chaotic Neutral", "Characters that act towards their own interests but are more likely to use criminal means to get what they desire."));
        addAlignment(createDummyAlignment("7", "Lawful Evil", "Characters that act within the law but use their power to subvert those under them."));
        addAlignment(createDummyAlignment("8", "True Evil", "Characters that only care about increasing their power and influence."));
        addAlignment(createDummyAlignment("9", "Chaotic Evil", "Characters that just want to watch the world burn."));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static void addClass(DummyClass charClass){
        CLASSES.add(charClass);
        CLASS_MAP.put(charClass.id, charClass);
    }
    private static void addCharacter(DummyCharacter character){
        CHARACTERS.add(character);
        CHARACTER_MAP.put(character.id, character);
    }
    private static void addAlignment(DummyAlignment alignment){
        ALIGNMENTS.add(alignment);
        ALIGNMENT_MAP.put(alignment.id, alignment);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }
    private static DummyClass createDummyClasses(int position){
        return new DummyClass(String.valueOf(position), "Ranger " + position, "Class that balances ranged and melee combat styles.");
    }
    private static DummyCharacter createDummyCharacters(int position){
        return new DummyCharacter(String.valueOf(position), "Hank the Tank " + position, "This character makes his way through forests, decimating the wildlife in the process.");
    }
    private static DummyAlignment createDummyAlignment(String id, String name, String content) {
        return new DummyAlignment(id, name, content);
    }


    private static String makeDetails(int position) {
       StringBuilder builder = new StringBuilder();
        builder.append("Item Details: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nThis item will aid you on your adventure.");
        }
        return builder.toString();
       //return "Level: 5";
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class DummyClass {
        public final String id;
        public final String name;
        public final String content;
        public DummyClass(String id, String name, String content){
            this.id = id;
            this.name = name;
            this.content = content;
        }
    }

    public static class DummyCharacter {
        public final String id;
        public final String name;
        public final String content;
        public DummyCharacter(String id, String name, String content){
            this.id = id;
            this.name = name;
            this.content = content;
        }
    }

    public static class DummyAlignment {
        public final String id;
        public final String name;
        public final String content;
        public DummyAlignment(String id, String name, String content){
            this.id = id;
            this.name = name;
            this.content = content;
        }
    }
}
