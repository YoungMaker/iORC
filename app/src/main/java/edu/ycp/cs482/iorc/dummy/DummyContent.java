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
    public static final List<DummyRace> RACE = new ArrayList<DummyRace>();
    public static final List<DummyReligion> RELIGIONS = new ArrayList<DummyReligion>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static final Map<String, DummyClass> CLASS_MAP = new HashMap<String, DummyClass>();
    public static final Map<String, DummyCharacter> CHARACTER_MAP = new HashMap<String, DummyCharacter>();
    public static final Map<String, DummyAlignment> ALIGNMENT_MAP = new HashMap<String, DummyAlignment>();
    public static final Map<String, DummyRace> RACE_MAP = new HashMap<String, DummyRace>();
    public static final Map<String, DummyReligion> RELIGION_MAP = new HashMap<String, DummyReligion>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/

        //items
        addItem(createDummyItem("1", "Water Skin","Used to store water for adventurers.","1 GP"));
        addItem(createDummyItem("2", "Bedroll","Useful for sleeping on during times.","15 GP"));
        addItem(createDummyItem("3", "Standard adventurer gear","Starting gear including a backpack, sunrods and rations.","30 GP"));
        addItem(createDummyItem("4", "Rations (7days)","7 days worth of dried nuts fruits and meats.","50 SP"));
        addItem(createDummyItem("5", "Sunrod","Rod that produces light for 6 hours.","10 GP"));
        addItem(createDummyItem("6", "Lock picking kit","Tools used for picking locks.","25 GP"));
        addItem(createDummyItem("7", "Alchemy Kit","Contains the gear needed to grind and mix ingredients for spells and rituals.","15 GP"));
        addItem(createDummyItem("8", "Longsword","Military style sword","10 GP"));
        addItem(createDummyItem("9", "Longbow","Military style ranged weapon","15 GP"));
        addItem(createDummyItem("10", "Leather Armor","Light armor that offers basic protection while allowing for agile movement.","20 GP"));
        addItem(createDummyItem("11","light shield","Small but sturdy shield","10 GP"));
        addItem(createDummyItem("12","Climber's Kit","All of the equipment needed for most climbs.","25 GP"));
        addItem(createDummyItem("13","Poisoner's Kit","Equipment needed to mix and create a variety of poisons","35 GP"));

        //characters
        addCharacter(createDummyCharacters("1","Hank the Tank","Enjoys the front line","Paladin","Goliath", "23","9","9","25","3","3","13","10","3"));
        addCharacter(createDummyCharacters("2","Gorden the Warden","Stuff","Warden","Half Elf","16", "15","15","12","18","11","14","16","17"));
        addCharacter(createDummyCharacters("3","Piper the Sniper","Pew pew","Ranger","Elf","12", "16","14","13","15","11","13","15","16"));
        addCharacter(createDummyCharacters("4","Craig","Good old Craig","Wizard","Human", "8","11","10","19","17","14","16","11","10"));
        //addCharacter(createDummyCharacters("","","","","","", "","","","","","","",""));

        //classes
        addClass(createDummyClasses("1", "Ranger","Agile combatant that uses both melee and ranged attacks." , "+2 Ref"));
        addClass(createDummyClasses("2", "Fighter","Person who whacks people over the head with a various assortment of large melee weapons.", "+2 Fort"));
        addClass(createDummyClasses("3", "Wizard", "User of magic who focuses on spells that have both utility and combat capabilities.","+2 Will"));
        addClass(createDummyClasses("4", "Rogue", "A class that relies on cunning and stealth.", "+1 Ref, +1 Will"));
        addClass(createDummyClasses("5", "Cleric", "A class that focuses on the healing arts","+1 Will, +1 Fort"));
        addClass(createDummyClasses("6", "Paladin", "Heavily armoured frontline class", "+2 Fort"));
        addClass(createDummyClasses("7", "Warlock","A mage that focuses on the darker arts, more aggressive type of wizard","+1 Will, +1 Ref"));
        addClass(createDummyClasses("8", "Bard","Motivates the group through song.", "+2 Ref"));
        addClass(createDummyClasses("9", "Barbarian", "Loves hitting things, think of an angrier fighter.", "+2 Fort"));
        addClass(createDummyClasses("10", "Monk", "Calm and dedicated to good monks are known for their superior unarmed combat skills.", "+1 Ref, +1 Will"));
        addClass(createDummyClasses("11", "Druid", "Uses the power of nature to support themselves both in and out of combat.", "+2 Will"));
        addClass(createDummyClasses("12", "Warden", "Defender of nature, able to hold their own and avoid damage despite having light armour.", "+1 Ref, +1 Will"));
        addClass(createDummyClasses("13", "Warlord", "Leaders who inspire and give opportunities to their comrades.", "+1 Fort, +1 Will"));

        //alignments
        addAlignment(createDummyAlignment("1", "Lawful Good", "Characters that act within the law and follow it's processes."));
        addAlignment(createDummyAlignment("2", "True Good", "Character that does what they think is right, regardless of law."));
        addAlignment(createDummyAlignment("3", "Chaotic Good", "Characters that do what needs to be done for a good end result, regardless of means"));
        addAlignment(createDummyAlignment("4", "Lawful Neutral", "Characters that act as a neutral party in debates."));
        addAlignment(createDummyAlignment("5", "True Neutral", "Characters that act towards their own interests."));
        addAlignment(createDummyAlignment("6", "Chaotic Neutral", "Characters that act towards their own interests but are more likely to use criminal means to get what they desire."));
        addAlignment(createDummyAlignment("7", "Lawful Evil", "Characters that act within the law but use their power to subvert those under them."));
        addAlignment(createDummyAlignment("8", "True Evil", "Characters that only care about increasing their power and influence."));
        addAlignment(createDummyAlignment("9", "Chaotic Evil", "Characters that just want to watch the world burn."));

        addRace(createDummyRace("1", "Human","Your standard everyday person.", "+2 to any one ability score"));
        addRace(createDummyRace("2", "Elf","Like humans but are more graceful and live longer lives.", "+2 DEX +2 WIS"));
        addRace(createDummyRace("3","Dwarf","Short and hardy people that enjoy burrowing.","+2 STR, +2 CON"));
        addRace(createDummyRace("4","Dragonborn"," Dragonborn are humanoid dragons. They are strong and wise with a deep culture and the blood of dragons still flowing through their veins.","+2 CON +2 WIS"));
        addRace(createDummyRace("5","Half-Elf","Half-elves are half-human half-elf. Because of this middle ground of race, they have become accustomed to being consulted on matters of diplomacy. As such, they have a knack for talking to people and for leading.", "+2 CHA +2 WIS"));
        addRace(createDummyRace("6","Shifter","Shifters are humans with animalistic features. Possibly descendant from lycanthropes, every Shifter can generally be identified by their animal-like eyes, long teeth, sharp claws and strong muscles.", "+2 DEX +2 WIS"));
        addRace(createDummyRace("7","Shardmind","Shardminds are sentient framents of the Living Gate, which once stood at the pinnacle of the intricate lattace of the Astral Sea. Beyond the gate lay the alien Far Realm, and the gate's destruction during the Dawn War resulted in the rise of the mind flayer empire. Though Ioun's power holds the portal closed, shardminds seek to rebuild the gate and forever cut off the Far Realm's ability to influence the world.", "+2 INT +2 WIS"));
        addRace(createDummyRace("8","Halfling","Halflings are like hobbits, only more slender. Instead of being stout and plucky, they’re agile and nimble little guys who seem to be extra lucky.","+2 DEX +2 CHA"));

        addReligion(createDummyReligion("1", "Raven Queen","Unaligned Goddess of Death, Fate and Doom. Seasonal Goddess of Winter."));
        addReligion(createDummyReligion("2", "Melora","Unaligned Goddess of Wilderness, Nature and the Sea."));
        addReligion(createDummyReligion("3","Bahamut","Lawful Good God of Justice, Protection and Nobility. Patron of Dragonborn."));
        addReligion(createDummyReligion("4","Moradin","Lawful Good God of Family, Community and Creation (as in smithing). Patron of Dwarves."));
        addReligion(createDummyReligion("5","Tharizdun","The Chained God, also known as the Elder Elemental Eye, creator of the Abyss."));
        addReligion(createDummyReligion("6","Vecna","Evil God of the Undead and Necromancy. Lord of Secrets."));
        //addReligion(createDummyReligion("","",""));
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

    private static void addRace(DummyRace race){
        RACE.add(race);
        RACE_MAP.put(race.id, race);
    }

    private static void addReligion(DummyReligion religion){
        RELIGIONS.add(religion);
        RELIGION_MAP.put(religion.id, religion);
    }

    private static DummyItem createDummyItem(String id, String content, String details, String price) {
        return new DummyItem(id, content, details, price);
    }
    private static DummyClass createDummyClasses(String id, String name, String description, String defBonus){
        return new DummyClass(id, name, description, defBonus);
    }
    private static DummyCharacter createDummyCharacters(String id, String name, String content, String charClass, String race, String str, String dex, String con, String intel, String wis, String cha, String will, String fort, String ref){
        return new DummyCharacter(id, name, content,charClass, race, str, dex, con, intel, wis, cha, will, fort, ref);
    }
    private static DummyAlignment createDummyAlignment(String id, String name, String content) {
        return new DummyAlignment(id, name, content);
    }
    private static DummyRace createDummyRace(String id, String name, String content, String bonus){
        return new DummyRace(id, name, content, bonus);
    }

    private static DummyReligion createDummyReligion(String id, String name, String content){
        return new DummyReligion(id, name, content);
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
        public final String price;

        public DummyItem(String id, String content, String details, String price) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.price = price;
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
        public final String defBonus;
        public DummyClass(String id, String name, String content , String defBonus){
            this.id = id;
            this.name = name;
            this.content = content;
            this.defBonus = defBonus;
        }
    }

    public static class DummyCharacter {
        public final String id;
        public final String name;
        public final String content;
        public final String charClass;
        public final String race;
        public final String str;
        public final String dex;
        public final String con;
        public final String intel;
        public final String wis;
        public final String cha;
        public final String will;
        public final String fort;
        public final String ref;
        public DummyCharacter(String id, String name, String content, String charClass, String race, String str, String dex, String con, String intel, String wis, String cha, String will, String fort, String ref){
            this.id = id;
            this.name = name;
            this.content = content;
            this.charClass = charClass;
            this.race = race;
            this.str = str;
            this.dex = dex;
            this.con = con;
            this.intel = intel;
            this.wis = wis;
            this.cha = cha;
            this.will = will;
            this.fort = fort;
            this.ref = ref;
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

    public static class DummyRace {
        public final String id;
        public final String name;
        public final String content;
        public final String bonus;
        public DummyRace(String id, String name, String content, String bonus){
            this.id = id;
            this.name = name;
            this.content = content;
            this.bonus = bonus;
        }
    }

    public static class DummyReligion{
        public final String id;
        public final String name;
        public final String content;
        public DummyReligion(String id, String name, String content){
            this.id = id;
            this.name = name;
            this.content = content;
        }
    }
}
