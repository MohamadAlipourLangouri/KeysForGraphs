package BaseInfra;

import java.io.Serializable;
import java.util.*;

public abstract class Vertex implements Comparable<Vertex>, Serializable {

    private Set<String> types=new HashSet<>();

    private boolean isMarked = false;

    private Map<String, Attribute> attributes;

    // TODO: consider adding an id field (e.g. vertexURI from dataVertex) [2021-02-07]

    public Vertex(String type) {
        types.add(type);
        attributes= new HashMap<>();
    }


    // Getter functions

    public Map<String, Attribute> getAllAttributesHashMap() {
        return attributes;
    }

    public Collection<Attribute> getAllAttributesList() {
        return attributes.values();
    }

    public Set<String> getTypes() {
        return types;
    }

    public String getAttributeValueByName(String name)
    {
        try {
            return attributes.get(name).getAttrValue();
        }
        catch (Exception e)
        {
            System.out.println("Error retrieving attribute value for: " + name);
            System.out.println("Current attributes: ");
            for (Attribute attr:attributes.values()) {
                System.out.println(attr);
            }
            return "NULL";
        }
    }

    public String getAttributeValues(Collection<Attribute> attributes)
    {
        StringBuilder ret = new StringBuilder();
        for (Attribute attr:attributes) {
            ret.append(getAttributeValueByName(attr.getAttrName())).append(",");
        }
        return ret.toString();
    }

    public Collection<String> getAllAttributesNames() {
        return attributes.keySet();
    }

    // Setter Functions

    public void setAllAttributes(Collection<Attribute> attributes) {
        for (Attribute attr:attributes)
            this.attributes.put(attr.getAttrName(),attr);
    }

    public void addType(String type)
    {
        this.types.add(type);
    }


    public void addAttribute(String name, String value)
    {
        attributes.put(name.toLowerCase(),new Attribute(name.toLowerCase(),value.toLowerCase()));
    }

    public void setOrAddAttribute(Attribute attr)
    {
        if(attributes.containsKey(attr.getAttrName()))
            attributes.get(attr.getAttrName()).setAttrValue(attr.getAttrValue());
        else
            addAttribute(attr);
    }

    public void putAttributeIfAbsent(Attribute attr)
    {
        // Handles case where an attribute is a multi-value attribute
        if (attributes.containsKey(attr.getAttrName()) && attributes.get(attr.getAttrName()) != null) {
            if (attr.getAttrValue().compareTo(attributes.get(attr.getAttrName()).getAttrValue()) < 0) {
                attributes.put(attr.getAttrName(),attr);
            }
        } else {
            addAttribute(attr);
        }
    }

    public void deleteAllAttributes()
    {
        this.attributes= new HashMap<>();
    }

    public void deleteAttribute(Attribute attr)
    {
        attributes.remove(attr.getAttrName());
    }

    public void addAttribute(Attribute attr)
    {
        attributes.put(attr.getAttrName(),attr);
    }

    public boolean hasAttribute(String name)
    {
        return attributes.containsKey(name.toLowerCase());
    }

    // The function to check if two vertices can be mapped to each other in subgraph isomorphism
    // This needs to be overridden in DataVertex and PatternVertex
    public boolean isMapped(Vertex v)
    {
        return false;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

}
