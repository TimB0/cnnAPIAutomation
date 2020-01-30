
package org.tim.boland.pojos.representative;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Office {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;
    @SerializedName("levels")
    @Expose
    private List<String> levels = null;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("officialIndices")
    @Expose
    private List<Integer> officialIndices = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Integer> getOfficialIndices() {
        return officialIndices;
    }

    public void setOfficialIndices(List<Integer> officialIndices) {
        this.officialIndices = officialIndices;
    }

}
