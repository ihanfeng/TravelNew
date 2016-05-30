package com.travelogue.post.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Post entity.
 */
public class PostDTO implements Serializable {

    private String id;

    @NotNull
    private String head;

    @NotNull
    private String content;

    private String users;

    private String places;

    private String resources;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }
    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostDTO postDTO = (PostDTO) o;

        if ( ! Objects.equals(id, postDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PostDTO{" +
            "id=" + id +
            ", head='" + head + "'" +
            ", content='" + content + "'" +
            ", users='" + users + "'" +
            ", places='" + places + "'" +
            ", resources='" + resources + "'" +
            '}';
    }
}
