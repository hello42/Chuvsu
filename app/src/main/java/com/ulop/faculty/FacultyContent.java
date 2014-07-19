package com.ulop.faculty;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FacultyContent {

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
