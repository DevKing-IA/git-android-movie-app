package com.evilkingmediabeta.utility;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root (name="ekm")
public class GetXmlInfo {

    @Element(name="version")
    private String version;
    @Element(name="message")
    private String message;
    @Element(name="harem-pass")
    private String haremPass;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHaremPass() {
        return haremPass;
    }

    public void setHaremPass(String haremPass) {
        this.haremPass = haremPass;
    }
}
