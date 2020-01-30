
package org.tim.boland.pojos.representative;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcdDivisionCountryUsStateCaSldl36 {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("officeIndices")
    @Expose
    private List<Integer> officeIndices = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getOfficeIndices() {
        return officeIndices;
    }

    public void setOfficeIndices(List<Integer> officeIndices) {
        this.officeIndices = officeIndices;
    }

}
