package com.xinlan.meizitu.data;

import java.util.List;

/**
 * Created by panyi on 2017/8/9.
 */

public class Node {
    private String image;
    private String name;
    private String link;
    private String title;
    private String refer;
    private List<Node> childList;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Node> getChildList() {
        return childList;
    }

    public void setChildList(List<Node> childList) {
        this.childList = childList;
    }


    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public static Node createNode(String refer,String s){
        Node node = new Node();
        node.setRefer(refer);
        return node;
    }

}//end class
