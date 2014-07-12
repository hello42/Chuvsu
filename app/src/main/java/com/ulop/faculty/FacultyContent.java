package com.ulop.faculty;

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
public class FacultyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<FacultyItem> ITEMS = new ArrayList<FacultyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static final Map<String, FacultyItem> ITEM_MAP = new HashMap<String, FacultyItem>();



    private static void addItem(FacultyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.fctName, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class FacultyItem {
        public String fctName;
        public String content;
        public String link;
        public String logo;
        public String id;


        public FacultyItem(String fctName, String content, String link, String logo) {
            this.fctName = fctName;
            this.content = content;
            this.link = link;
            this.logo = logo;
        }

        @Override
        public String toString() {
            return fctName;
        }
    }
}
