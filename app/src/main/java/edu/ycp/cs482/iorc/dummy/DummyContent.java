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
    public static final List<DummySkill> SKILLS = new ArrayList<DummySkill>();


    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static final Map<String, DummySkill> SKILL_MAP = new HashMap<String, DummySkill>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
            addSkill(createDummySkills(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static void addSkill(DummySkill skill){
        SKILLS.add(skill);
        SKILL_MAP.put(skill.id, skill);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Skill " + position, makeDetails(position));
    }

    private static DummySkill createDummySkills(int position){
        return new DummySkill(String.valueOf(position), "skill " + position, String.valueOf(5));
    }

    private static String makeDetails(int position) {
       /* StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();*/
       return "Level: 5";
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

    public static class DummySkill {
        public final String id;
        public final String name;
        public final String level;
        public DummySkill(String id, String name, String level){
            this.id = id;
            this.name = name;
            this.level = level;
        }
    }
}
