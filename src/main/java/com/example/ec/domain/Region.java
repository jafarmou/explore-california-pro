package com.example.ec.domain;

public enum Region {
    Northern_California("Northern California"), Central_Coast("Central Coast"), Southern_California("Southern California"), Varies("Varies");
    private String label;
    Region(String label) {
        this.label = label;
    }

    public static Region findByLabel(String byLabel) {
        for (Region region: Region.values()) {
            if (region.label.equalsIgnoreCase(byLabel)) {
                return region;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
