package pl.coddlers.core.models.other;

import lombok.Getter;

public class Invitation {

    @Getter
    private String link;

    public Invitation(String link) {
        this.link = link;
    }
}
