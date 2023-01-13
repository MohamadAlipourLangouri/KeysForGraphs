package BaseInfra;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class DataVertex extends Vertex implements Serializable {

    private String URI ="";

    private String uniqueID = null;

    public DataVertex(String uri, String type) {
        super(type.toLowerCase());
        this.URI =uri.toLowerCase();
        this.addAttribute("uri", URI);
        // ???: Is Integer large enough for our use case of possible 10+ million vertices? [2021-02-07]
//        this.hashValue=vertexURI.hashCode();
    }

    public String getURI() {
        return URI;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public String toString() {
        return "vertex{" +
                "type='" + getTypes() + '\'' +
                ", attributes=" + super.getAllAttributesList() +
                '}';
    }

    @Override
    public boolean isMapped(Vertex v) {
        if(v instanceof DataVertex)
            return false;
        if (super.getTypes().containsAll(v.getTypes()) || v.getTypes().iterator().next().equals("_")) {
            if (super.getAllAttributesNames().containsAll(v.getAllAttributesNames())) {
                for (Attribute attr : v.getAllAttributesList())
                    if (!attr.isNULL() && !super.getAttributeValueByName(attr.getAttrName()).equals(attr.getAttrValue())) {
                        return false;
                    }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NotNull Vertex o) {
        if(o instanceof DataVertex)
        {
            DataVertex v=(DataVertex) o;
            return this.URI.compareTo(v.URI);
        }
        else
            return 0;
    }
}
