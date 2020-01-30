
package org.tim.boland.pojos.representative;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Official {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private List<Address> address = null;

    @SerializedName("party")
    @Expose
    private String party;

    @SerializedName("phones")
    @Expose
    private List<String> phones = null;

    @SerializedName("urls")
    @Expose
    private List<String> urls = null;

    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;

    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;

    @SerializedName("emails")
    @Expose
    private List<String> emails = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

}
